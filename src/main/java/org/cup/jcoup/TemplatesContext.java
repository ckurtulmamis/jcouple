/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cup.jcoup;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.reflect.Reflection;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Deque;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author caner
 */
public class TemplatesContext {

    private Properties basicProperties = new Properties();

    private BasicTemplates basics;

    private SeperatorTemplates seperators;
    private WrapperTemplates wrappers;
    private final Writer writer;
    private final boolean autoFlash;
    private int block = 0;
    private int blockStep = 2;

    private interface Context extends BasicTemplates, FunctionTemplates {

    }

    private Deque<Runnable> delegates = Queues.newLinkedBlockingDeque();
    private Deque<Runnable> seperatorQueue = Queues.newLinkedBlockingDeque();

    private class SeperatorInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Preconditions.checkArgument(Runnable.class.isAssignableFrom(args[0].getClass()));
            Consumer<Object> r = methods(method.getName());
            final Runnable runnable = new Runnable() {
                int times = 0;
                {
                    String s = basicProperties.getProperty(method.getName() + ".start");
                    if(s != null){
                        int ii = Integer.parseInt(s);
                        if(ii == 1)
                            ++times;
                    }
                }

                @Override
                public void run() {
                    if (times > 0) {
                        r.accept(basics);
                    }
                    ++times;
                }
            };
            seperatorQueue.addFirst(runnable);
            ((Runnable) args[0]).run();
            seperatorQueue.pollFirst();

            return proxy;
        }

    }

    private FunctionTemplates functions = new FunctionTemplates() {
        @Override
        public void delegate() {
            delegates.peekFirst().run();
        }

        @Override
        public void beginBlock() {
            ++block;
        }
        @Override
        public void endBlock() {
            --block;
        }

        @Override
        public void newLine() {
            //append("\n");
            StringBuilder sb = new StringBuilder("\n");
            for (int i = 0; i < (block * blockStep); i++) {
                sb.append(" ");
            }
            append(sb.toString());
        }

        @Override
        public void seperator() {
            seperatorQueue.getFirst().run();
        }
    };

    private class BasicTemplatesHandler implements InvocationHandler {

        public BasicTemplatesHandler() throws IOException {
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Class<?> dc = method.getDeclaringClass();

            if (dc == BasicTemplates.class) {
                final String property = basicProperties.getProperty(method.getName());
                writer.append(property);
                if (autoFlash) {
                    writer.flush();
                }
            } else if (dc == FunctionTemplates.class) {
                return method.invoke(functions, args);
            }

            return proxy;
        }
    }

    private class WrapperTemplatesHandler implements InvocationHandler {

        Cache<Method, Consumer<Object>> mustaches = CacheBuilder.newBuilder().build();

        public WrapperTemplatesHandler() throws IOException {
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Consumer mm = mustaches.get(method, () -> {
                return methods(method.getName());
            });
            final Runnable delegate = (Runnable) args[0];
            delegates.addFirst(delegate);
            mm.accept(basics);
            Runnable res = delegates.pollFirst();
            Preconditions.checkState(res == delegate, "Not same");
            return proxy;
        }
    }

    protected Consumer<Object> methods(String name) throws SecurityException, NoSuchMethodException {
        String p = basicProperties.getProperty(name);
        String[] ps = p.split(",");
        List<Method> methods = Lists.newArrayList();
        for (String p1 : ps) {
            try {
                Method r = Context.class.getMethod(p1.trim(), new Class[]{});
                methods.add(r);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return new Consumer<Object>() {
            @Override
            public void accept(Object t) {
                for (Method m : methods) {
                    try {
                        m.invoke(t, new Object[]{});
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(TemplatesContext.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
    }

    public TemplatesContext(Writer writer, boolean autoFlash) throws IOException {
        this.writer = writer;
        this.autoFlash = autoFlash;

        InputStream r = TemplatesContext.class.getResourceAsStream("/Basic.properties");
        basicProperties.load(r);

        this.basics = Reflection
                .newProxy(Context.class, new BasicTemplatesHandler());
        this.wrappers = Reflection
                .newProxy(WrapperTemplates.class, new WrapperTemplatesHandler());
        this.seperators = Reflection
                .newProxy(SeperatorTemplates.class, new SeperatorInvocationHandler());

    }

    public BasicTemplates getBasics() {
        return basics;
    }

    public SeperatorTemplates getSeperators() {
        return seperators;
    }

    public WrapperTemplates getWrappers() {
        return wrappers;
    }

    public TemplatesContext append(String text) {
        try {
            writer.append(text);
            if (autoFlash) {
                writer.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(TemplatesContext.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this;
    }
}

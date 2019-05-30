/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cup.jcoup;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 *
 * @author caner
 */
public class HeaderVisitor<T extends TemplatesContext> extends VoidVisitorAdapter<T> {

    @Override
    public void visit(ClassOrInterfaceDeclaration n, T arg) {
        arg.getWrappers().classHeader(() -> {
            n.getName().accept(this, arg);
        });
        arg.getWrappers().classExtend(() -> {
            arg.getSeperators().classExtendSeperator(() -> {
                n.getExtendedTypes().forEach(p -> {
                    arg.getWrappers().extendClass(() -> {
                        p.accept(this, arg);
                    });
                });
            });
            arg.getSeperators().classImplementSeperator(() -> {
                n.getImplementedTypes().forEach((t) -> {
                    arg.getWrappers().implementClass(() -> {
                        t.accept(this, arg);
                    });
                });
            });

        });
        arg.getWrappers().classBody(() -> {
            n.getMembers().forEach((t) -> {
                t.accept(this, arg);
            });
        });
        /*n.getExtendedTypes().forEach(p -> p.accept(this, arg));
        n.getImplementedTypes().forEach(p -> p.accept(this, arg));
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        n.getMembers().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));*/
        //super.visit(n, arg); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(VariableDeclarator n, T arg) {
        
        arg.getWrappers().variableDeclarationType(() -> {
            n.getType().accept(this, arg);
        });
        //n.getInitializer().ifPresent(l -> l.accept(this, arg));
        arg.getWrappers().variableDeclarationName(() -> {
            n.getName().accept(this, arg);
        });
        //n.getComment().ifPresent(l -> l.accept(this, arg));
        //super.visit(n, arg); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(ClassOrInterfaceType n, T arg) {
        n.getName().accept(this, arg);
        n.getTypeArguments().ifPresent((t) -> {
            arg.getWrappers().variableTemplate(() -> {
                arg.getSeperators().variableTemplateSeperator(() -> {
                    t.forEach((tt) -> {
                        arg.getWrappers().typeParameter(() -> {
                            tt.accept(this, arg);
                        });
                    });
                });
            });
        });
        //n.getTypeArguments().ifPresent(l -> l.forEach(v -> v.accept(this, arg)));
        /*n.getName().accept(this, arg);
        n.getScope().ifPresent(l -> l.accept(this, arg));
        n.getTypeArguments().ifPresent(l -> l.forEach(v -> v.accept(this, arg)));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));*/
        //super.visit(n, arg); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(TypeParameter n, T arg) {
        /*arg.getWrappers().typeParameter(() -> {
            n.getName().accept(this, arg);
        });*/
        //super.visit(n, arg); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    @Override
    public void visit(PrimitiveType n, T arg) {
        arg.append(n.asString());
        super.visit(n, arg); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(FieldDeclaration n, T arg) {
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getVariables().forEach(p -> p.accept(this, arg));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));

        //super.visit(n, arg); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(SimpleName n, T arg) {
        arg.append(n.asString());
    }

}

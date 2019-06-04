/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jcouple.jcouple;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author caner
 */
public class Main implements Runnable {

    @Override
    public void run() {
        try {
            File f = new File("src\\test\\java\\org\\jcouple\\jcouple\\test\\Simple.java");
            CompilationUnit cu = StaticJavaParser.parse(f);
            Writer w = new PrintWriter(System.out);
            TemplatesContext tc = new TemplatesContext(w, true);
            HeaderVisitor<TemplatesContext> v = new HeaderVisitor<TemplatesContext>();
            cu.accept(v, tc);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void main(String[] args) {
        new Main().run();
    }
}

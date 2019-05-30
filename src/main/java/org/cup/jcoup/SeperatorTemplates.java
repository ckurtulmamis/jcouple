/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cup.jcoup;

import com.github.javaparser.ast.Node;
import java.util.function.Consumer;

/**
 *
 * @author caner
 */
public interface SeperatorTemplates {
    void classExtendSeperator(Runnable r);
    void classImplementSeperator(Runnable r);
    void variableTemplateSeperator(Runnable r);
}

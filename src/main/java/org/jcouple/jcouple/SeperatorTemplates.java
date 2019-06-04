/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jcouple.jcouple;

import com.github.javaparser.ast.Node;
import java.util.function.Consumer;

/**
 *
 * @author caner
 */
public interface SeperatorTemplates<T> {
    void classExtendSeperator(T r);
    void classImplementSeperator(T r);
    void variableTemplateSeperator(T r);
    void methodKeywordsSeperator(T r);
    void fieldKeywordsSeperator(T r);
}

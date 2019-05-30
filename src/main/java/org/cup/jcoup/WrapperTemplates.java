/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cup.jcoup;

import com.github.javaparser.ast.Node;

/**
 *
 * @author caner
 */
public interface WrapperTemplates {
    void classHeader(Runnable r);
    void classExtend(Runnable r);
    void extendClass(Runnable r);
    void implementClass(Runnable r);
    void variableDeclarationType(Runnable r);
    void variableDeclarationName(Runnable r);

    void classBody(Runnable r);
    void variableTemplate(Runnable r);
    void typeParameter(Runnable r);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cup.jcoup;

/**
 *
 * @author caner
 */
public interface BasicTemplates {
    BasicTemplates classKeyword();
    BasicTemplates ws();
    BasicTemplates comma();
    BasicTemplates colon();
    BasicTemplates greater();
    BasicTemplates lesser();
    BasicTemplates semicolon();
    BasicTemplates jobject();
    BasicTemplates virtualKeyword();
    
    
    BasicTemplates classDefinitionPrefix();
    BasicTemplates classDefinitionSuffix();
    BasicTemplates keywordCurlyOpen();
    BasicTemplates keywordCurlyClose();
    
    BasicTemplates holder();
}

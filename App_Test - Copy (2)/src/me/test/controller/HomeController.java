/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.test.controller;

import esale.frontend.common.EsaleFEConfig;
import esale.frontend.common.Utils;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class HomeController extends HttpServlet{
    private Logger logger = Logger.getLogger(HomeController.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            TemplateDataDictionary myDic = TemplateDictionary.create();
            myDic.setVariable("path", EsaleFEConfig.path);
            String template = Utils.renderTemplate("Template/home.html", myDic);
            Utils.out(template, resp);
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        doGet(req, resp);
    }
}

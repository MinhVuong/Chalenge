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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class InsertController extends HttpServlet{
    private Logger logger = Logger.getLogger(InsertController.class);
    private String GET_URL = "http://localhost:6969/insert?size=";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            TemplateDataDictionary myDic = TemplateDictionary.create();
            myDic.setVariable("path", EsaleFEConfig.path);
            String template="";
            String size = req.getParameter("size");
            if(size != null){
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(GET_URL+size);
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                switch(httpResponse.getStatusLine().getStatusCode()){
                    case 200:{
                        myDic.setVariable("result", "Thành Công!!!");
                        myDic.setVariable("size", size);
                        template = Utils.renderTemplate("Template/insert.html", myDic);
                        break;
                    }
                    case 500:{
                        myDic.setVariable("result", "Thất bại!!!");
                        myDic.setVariable("size", size);
                        template = Utils.renderTemplate("Template/insert.html", myDic);
                        break;
                    }
                }
                Utils.out(template, resp);
            }else{
                resp.setStatus(400);
                Utils.out("size ko dc rong", resp);
            }
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        doGet(req, resp);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.test.controller;

import com.google.gson.Gson;
import esale.frontend.common.EsaleFEConfig;
import esale.frontend.common.Utils;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.test.model.GetPakage;
import me.test.model.News;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class UpdateController extends HttpServlet{
    private Logger logger = Logger.getLogger(UpdateController.class);
    private String GET_URL = "http://localhost:6969/update?id=";
    private Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            TemplateDataDictionary myDic = TemplateDictionary.create();
            myDic.setVariable("path", EsaleFEConfig.path);
            String template = "";
            String id = req.getParameter("id");
            String status = req.getParameter("status");
            String from = req.getParameter("from");
            String to = req.getParameter("to");
            if((id!=null && status!=null) || (!id.equals("") && !status.equals(""))){
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(GET_URL + id + "&status=" + status + "&from=" + from + "&to=" + to);
                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                switch (httpResponse.getStatusLine().getStatusCode()) {
                     case 200: {
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                        String body = "";
                        String response="";
                        while ((body = br.readLine()) != null) {
                            response = body;
                        }
                        GetPakage pakage = gson.fromJson(response, GetPakage.class);
                        body = bodyTable(pakage.getNewss(), from, to);
                        myDic.setVariable("body", body);
                         break;
                     }
                     case 500: {
                         myDic.setVariable("error", "Không thành công.");
                         BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                        String body = "";
                        String response="";
                        while ((body = br.readLine()) != null) {
                            response = body;
                        }
                        GetPakage pakage = gson.fromJson(response, GetPakage.class);
                        body = bodyTable(pakage.getNewss(), from, to);
                        myDic.setVariable("body", body);
                         break;
                     }
                     case 400: {
                         myDic.setVariable("error", "");
                         break;
                     }
                }
                myDic.setVariable("from", from);
                myDic.setVariable("to", to);
                template = Utils.renderTemplate("Template/get.html", myDic);
                Utils.out(template, resp);
            }else{
               
                myDic.setVariable("error", "Vui lòng nhập 2 số!!!");
                template = Utils.renderTemplate("Template/get.html", myDic);
                Utils.out(template, resp);
            }
            
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        doGet(req, resp);
    }
    
    private String bodyTable(List<News> newss, String from, String to){
        String result="";
        for(News news : newss){
            result += "<tr>";
            result += "<td>"+news.getId()+"</td>";
            result += "<td>"+news.getContent()+"</td>";
            result += "<td><select style=\"width: 100px;\" id = \"status"+news.getId()+"\" onchange=\"myFunction("+news.getId()+")\">"; 
            if(news.getStatus()==1){
                result += "<option value=\"1\">1</option>";
                result += "<option value=\"0\">0</option>";
            }else{
                result += "<option value=\"0\">0</option>";
                result += "<option value=\"1\">1</option>";
            }
            result += "</select></td>";
            
            result += "<td>"+news.getTime()+"</td>";
            result += "<td ><form action=\"update\" method=\"get\">";
            result += "<input type=\"hidden\" name=\"from\" value="+from+">";
            result += "<input type=\"hidden\" name=\"to\" value="+to+">";
            result += "<input type=\"hidden\" name=\"id\" value="+news.getId()+">";
            result += "<input id=\"update"+news.getId()+"\" type=\"hidden\" name=\"status\" value=\"\">";
            result += "<button id=\"btnSub\" class=\"\" type=\"submit\">Cập nhật</button>";
            result += "</form></td>";
            result += "</tr>";
        }
        return result;
    }
}

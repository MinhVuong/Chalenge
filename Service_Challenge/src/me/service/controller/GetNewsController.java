/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.controller;

import com.google.gson.Gson;
import esale.frontend.common.Utils;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.service.model.News;
import me.service.myservice.MongoNewsService;
import me.service.pakage.GetPakage;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class GetNewsController extends HttpServlet{
    private Logger logger = Logger.getLogger(GetNewsController.class);
    private Gson gson = new Gson();
    MongoNewsService mongoS = new MongoNewsService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            String from = req.getParameter("from");
            String to = req.getParameter("to");
            if((from!=null && to!=null) || (from.equals("") && to.equals(""))){
                List<News> newss = mongoS.GetNewFromTo(Integer.parseInt(from), Integer.parseInt(to));
                GetPakage pakage = new GetPakage(newss);
                resp.setStatus(200);
                Utils.out(gson.toJson(pakage), resp);
            }else{
                resp.setStatus(400);
                Utils.out("ID user ko dc rong", resp);
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

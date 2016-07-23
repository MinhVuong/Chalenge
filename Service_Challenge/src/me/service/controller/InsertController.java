/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.controller;

import com.google.gson.Gson;
import esale.frontend.common.Utils;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Kuti
 */
public class InsertController extends HttpServlet{
    private static Logger logger = Logger.getLogger(InsertController.class);
    private Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            String size = req.getParameter("size");
            if(size != null){
                resp.setStatus(200);
                Utils.out("OK", resp);
            }
            else{
                resp.setStatus(400);
                Utils.out("ID user ko dc rong", resp);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        doGet(req, resp);
    }
}

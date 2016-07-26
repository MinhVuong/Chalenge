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
import me.service.helper.NotSaveMySqlMemcached;
import me.service.myservice.DBService;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class UpdateController extends HttpServlet{
    private static Logger logger = Logger.getLogger(UpdateController.class);
    private Gson gson = new Gson();
    private DBService dbS = new DBService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            String id = req.getParameter("id");
            if(id != null){
                NotSaveMySqlMemcached notSaveMySqlMemcached = new NotSaveMySqlMemcached();
                resp.setStatus(200);
                if(!dbS.UpdateStatus2DB(Integer.parseInt(id), notSaveMySqlMemcached))
                    resp.setStatus(500);
                
                Utils.out("OK", resp);
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

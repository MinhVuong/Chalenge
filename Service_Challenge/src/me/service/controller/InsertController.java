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
import me.service.helper.SizeIndexMemcached;
import me.service.helper.TimeHelper;
import me.service.model.News;
import me.service.myservice.DBService;
import org.apache.log4j.Logger;

/**
 *
 * @author Kuti
 */
public class InsertController extends HttpServlet{
    private static Logger logger = Logger.getLogger(InsertController.class);
    private Gson gson = new Gson();
    private DBService dbS = new DBService();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            String size = req.getParameter("size");
            if(size != null){
                NotSaveMySqlMemcached notSaveMySqlMemcached = new NotSaveMySqlMemcached();
                resp.setStatus(200);
                for(int i=0; i<Integer.parseInt(size); i++){
                    int sizeR = SizeIndexMemcached.GetAndSaveSizeIndex();
                    News news = new News(sizeR, "Noi dung "+sizeR, 1, TimeHelper.GetTimeCurrent());
                    if(!dbS.InsertNewsTo2DB(news, notSaveMySqlMemcached)){
                        logger.info("Don't insert record news: "+ gson.toJson(news));
                        resp.setStatus(500);
                    }
                }
                
                Utils.out("OK", resp);
            }
            else{
                resp.setStatus(400);
                Utils.out("size ko dc rong", resp);
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

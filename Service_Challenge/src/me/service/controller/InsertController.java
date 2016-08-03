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
import me.service.helper.SizeIndexMemcached;
import me.service.helper.TimeHelper;
import me.service.model.News;
import me.service.myservice.DBService;
import org.apache.log4j.Logger;

/**
 *
 * @author Kuti
 */
public class InsertController extends HttpServlet {

    private static Logger logger = Logger.getLogger(InsertController.class);
    private Gson gson = new Gson();
    private DBService dbS = new DBService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            long start = System.currentTimeMillis();
            int size = Integer.parseInt(req.getParameter("size"));
            String result = "";
            if (size > 0) {
                try {
                    resp.setStatus(200);
                    for (int i = 0; i < size; i++) {
                        int sizeR = SizeIndexMemcached.GetAndSaveSizeIndex();
                        while (sizeR == -1) {
                            Thread.sleep(100);
                            sizeR = SizeIndexMemcached.GetAndSaveSizeIndex();
                        }
                        News news = new News(sizeR, "Noi dung " + sizeR, 1, TimeHelper.GetTimeCurrent());
                        if (!dbS.InsertNewsTo2DB(news)) {
                            logger.info("Don't insert record news: " + gson.toJson(news));
                            resp.setStatus(500);
                            result += "Request: " + i + " is status: 500";
                        } else {
                            result += "Request: " + i + " is status: 200";
                        }
                    }
                    Utils.out(result, resp);
                    logger.info("Status: " + resp.getStatus() + ". Thoi gian thuc hien insert size n=" + size + " la: " + (System.currentTimeMillis() - start));
                } catch (Exception ex) {
                    resp.setStatus(500);
                    Utils.out(result, resp);
                    logger.error("InsertController error: " + ex.getMessage(), ex);
                }
            } else {
                resp.setStatus(400);
                Utils.out("size ko dc rong", resp);
                logger.error(result);
            }
        } catch (Exception ex) {
            logger.error("InsertController error: " + ex.getMessage(), ex);
            resp.setStatus(400);
            Utils.out("size ko dc rong", resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }
}

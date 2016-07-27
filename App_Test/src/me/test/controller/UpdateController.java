/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.test.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class UpdateController extends HttpServlet{
    private Logger logger = Logger.getLogger(UpdateController.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        doGet(req, resp);
    }
}

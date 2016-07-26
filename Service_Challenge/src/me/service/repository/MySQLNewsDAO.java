/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import me.service.helper.MySQLHelper;
import me.service.model.News;
import org.apache.log4j.Logger;

/**
 *
 * @author Kuti
 */
public class MySQLNewsDAO {
    private Logger logger = Logger.getLogger(MySQLNewsDAO.class);
    public boolean InsertNews(News news){
        try{
            Connection conn = MySQLHelper.getInstance();
            String sql = "insert into news(id, content, status, time) values(%d, '%s',%d, '%s')";
            sql = String.format(sql, news.getId(), news.getContent(), news.getStatus(), news.getTime());
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            return true;
        }catch(Exception ex){
            logger.error("InsertNews errors: "+ex.getMessage());
            MySQLHelper.connect = false;
            return false;
        }
    }
    public boolean CheckInsertRecord(News news){
        try{
            Connection conn = MySQLHelper.getInstance();
            String sql = "select * from news where id=%d and time=%s";
            sql = String.format(sql, news.getId(), news.getTime());
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                return true;
            }
            return false;
        }catch(Exception ex){
            logger.error("CheckInsertRecord errors: "+ex.getMessage());
            MySQLHelper.connect = false;
            return false;
        }
    }
    
    public boolean UpdateStatus(int id){
        try{
            Connection conn = MySQLHelper.getInstance();
            String sql = "update news set status=0 where id=%d";
            sql = String.format(sql, id);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            return true;
        }catch(Exception ex){
            logger.error("UpdateStatus errors: "+ex.getMessage());
            MySQLHelper.connect = false;
            return false;
        }
    }
}

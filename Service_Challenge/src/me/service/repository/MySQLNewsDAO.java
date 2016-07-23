/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.repository;

import java.sql.Connection;
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
            String sql = "insert into news values('%s',%d, '%s')";
            sql = String.format(sql, news.getContent(), news.getStatus(), news.getTime());
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            return true;
        }catch(Exception ex){
            logger.error("InsertNews errors: "+ex.getMessage());
            return false;
        }
    }
    
}

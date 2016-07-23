/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.myservice;

import me.service.model.News;
import me.service.repository.MySQLNewsDAO;

/**
 *
 * @author Kuti
 */
public class MySqlNewsService {
    MySQLNewsDAO mysqlNewsD = new MySQLNewsDAO();
    public boolean InsertNews(News news){
        return mysqlNewsD.InsertNews(news);
    }
}

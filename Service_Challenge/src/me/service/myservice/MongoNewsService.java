/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.myservice;

import me.service.model.News;
import me.service.repository.NewsDAO;

/**
 *
 * @author Kuti
 */
public class MongoNewsService {
    NewsDAO mongoNewsD = new NewsDAO();
    public boolean InsertNews(News news){
        return mongoNewsD.InsertNews(news);
    }
    
    
}

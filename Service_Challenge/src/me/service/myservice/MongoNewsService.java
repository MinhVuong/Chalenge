/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.myservice;

import java.util.List;
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
    public boolean CheckInsertRecord(News news){
        return mongoNewsD.CheckInsertRecord(news);
    }
    public boolean UpdateStatus(int id){
        return mongoNewsD.UpdateStatus(id);
    }
    public List<News> GetNewFromTo(int from, int to){
        return mongoNewsD.GetNewFromTo(from, to);
    }
}

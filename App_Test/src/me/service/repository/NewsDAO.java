/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import me.service.helper.MongoDBHelper;
import me.service.helper.TimeHelper;
import me.service.model.News;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class NewsDAO {
    private Logger logger = Logger.getLogger(NewsDAO.class);
    public boolean Insert(News news){
        try{
            DBCollection dBCollection = MongoDBHelper.GetDBCollection();
            BasicDBObject bdb = new BasicDBObject();
            bdb.put("id", news.getId());
            bdb.put("content", news.getContent());
            bdb.put("status", news.getStatus());
            bdb.put("time", TimeHelper.GetTimeCurrent());
            dBCollection.insert(bdb);
            return true;
        }catch(Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }
    
    public News GetNewsByID(int id){
        try{
            return null;
        }catch(Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }
}

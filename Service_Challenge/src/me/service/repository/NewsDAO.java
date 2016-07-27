/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
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
    public boolean InsertNews(News news){
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
    
    public static int GetSizeIndexNews(){
        try{
            DBCollection dBCollection = MongoDBHelper.GetDBCollection();
            BasicDBObject sortObject = new BasicDBObject().append("_id", -1);
            DBCursor cur = dBCollection.find(new BasicDBObject()).sort(sortObject).limit(1);
            DBObject obj = cur.one();
            int size = (int)obj.get("id");
            return size;
        }catch(Exception e){
            return 0;
        }
    }
    
    public boolean CheckInsertRecord(News news){
        try{
            DBCollection dBCollection = MongoDBHelper.GetDBCollection();
            BasicDBObject bdb = new BasicDBObject();
            bdb.put("id", news.getId());
            DBCursor cur = dBCollection.find(bdb);
            while(cur.hasNext()){
                return true;
            }
            return false;
        }catch(Exception e){
            return false;
        }
    }
    
    public boolean UpdateStatus(int id){
        try{
            DBCollection dBCollection = MongoDBHelper.GetDBCollection();
            BasicDBObject bdb = new BasicDBObject();
            bdb.append("$set", new BasicDBObject().append("status", 0));
            
            BasicDBObject searchQuery = new BasicDBObject().append("id", id);
            dBCollection.update(searchQuery, bdb);
            
            return true;
        }catch(Exception e){
            logger.error(e.getMessage());
            return false;
        }    
    }
    
    public List<News> GetNewFromTo(int from, int to){
        try{
            List<News> result = new ArrayList<News>();
            DBCollection dBCollection = MongoDBHelper.GetDBCollection();
            BasicDBObject inQuery = new BasicDBObject();
            List<Integer> list = new ArrayList<Integer>();
            for(int i=from; i<=to; i++){
                list.add(i);
            }
            inQuery.put("id", new BasicDBObject("$in", list));
            DBCursor cursor = dBCollection.find(inQuery);
            while(cursor.hasNext()) {
                DBObject obj = cursor.next();
                News news = new News((int)obj.get("id"), (String)obj.get("content"), (int)obj.get("status"), (String)obj.get("time"));
                result.add(news);
            }
            return result;
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ArrayList<News>();
        }
    }
}

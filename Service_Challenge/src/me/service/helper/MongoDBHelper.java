/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.helper;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import java.util.Arrays;
import me.service.model.News;
import me.service.myservice.MongoNewsService;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class MongoDBHelper {
    private static Logger logger = Logger.getLogger(MongoDBHelper.class);
    private static DBCollection dbCollection;
    private MongoDBHelper(){}
    public static DBCollection GetDBCollection(){
        return dbCollection;
    }
    
    public static boolean ConnectionDatabase(){
        try{
            //DB db = (new  MongoClient("localhost", 27017)).getDB("demo");
            DB db = (new MongoClient(Arrays.asList(
                    new ServerAddress("localhost", 27017),
                    new ServerAddress("localhost", 27018)))).getDB("demo");
            dbCollection = db.getCollection("tabledemo");
            
            return true;
        }catch(Exception e){
            logger.info("Connect MongoDB Error: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean TestConnectionDatabase()
    {
        try{
            News news = new News(0, "", 1, "");
            MongoNewsService mongoS = new MongoNewsService();
            if(!mongoS.InsertNews(news) || !mongoS.DeleteNews(news))
                return false;
            return true;
        }catch(Exception e){
            logger.info("TestConnectionDatabase Error: " + e.getMessage());
            return false;
        }
    }
}

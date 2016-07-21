/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import me.service.helper.MongoDBHelper;

/**
 *
 * @author CPU01661-local
 */
public class InsertDB {
    public void TestInsert(){
        DBCollection dBCollection = MongoDBHelper.GetDBCollection();
        BasicDBObject bdb = new BasicDBObject();
        bdb.put("name", "vuong");
        bdb.put("time", "now()");
        dBCollection.insert(bdb);
    }
    
    public String RetreviewDB(){
        DBCollection dBCollection = MongoDBHelper.GetDBCollection();
        BasicDBObject bdb = new BasicDBObject();
        bdb.put("name", "vuong");
        DBCursor dbCursor = dBCollection.find(bdb);
        String result="";
        while(dbCursor.hasNext()){
            result += dbCursor.next();
        }
        return result;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.helper;

import com.google.gson.Gson;
import java.util.LinkedList;
import java.util.Queue;
import me.service.model.NotSaveMySql;
import me.service.myservice.DBService;
import me.service.myservice.MongoNewsService;
import me.service.myservice.MySqlNewsService;
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class QueueAfterMemcached {
    static Gson gson = new Gson();
    static int time = 60*60*24;
    private static Logger logger = Logger.getLogger(QueueAfterMemcached.class);
    public static boolean AddObjectToQueue(NotSaveMySql notSave){
        try{
            Queue queue;
            MemcachedClient mem = MemcacheHelper.GetInstance();
            CASValue casValue = mem.gets("queue_after");
             if(casValue == null || casValue.getValue().equals("")){
                queue = new LinkedList();
                queue.add(gson.toJson(notSave));
                CASResponse casresp = mem.cas("queue_after", casValue.getCas(), time, gson.toJson(queue));
                while(!casresp.toString().equals("OK")){
                    casValue = mem.gets("queue_after");
                    queue = gson.fromJson(casValue.getValue().toString(), Queue.class);
                    queue.add(gson.toJson(notSave));
                    casresp = mem.cas("queue_after", casValue.getCas(), time, gson.toJson(queue));
                }
            }else{
                queue = gson.fromJson(casValue.getValue().toString(), Queue.class);
                queue.add(gson.toJson(notSave));
                CASResponse casresp = mem.cas("queue_after", casValue.getCas(), time, gson.toJson(queue));
                while(!casresp.toString().equals("OK")){
                    casValue = mem.gets("queue_after");
                    queue = gson.fromJson(casValue.getValue().toString(), Queue.class);
                    queue.add(gson.toJson(notSave));
                    casresp = mem.cas("queue_after", casValue.getCas(), time, gson.toJson(queue));
                }
            }
            return true;
        }catch(Exception ex){
            logger.error("AddObjectToQueue error: " + ex.getMessage());
            return false;
        }
        
    }
    
    public static boolean SubObjectFromQueue(){
        try{
            Queue queue = new LinkedList();
            MemcachedClient mem = MemcacheHelper.GetInstance();
            CASValue casValue = mem.gets("queue_after");
             if(casValue == null || casValue.getValue().equals("")){
                queue = new LinkedList();
                CASResponse casresp = mem.cas("queue_after", casValue.getCas(), time, gson.toJson(queue));
                while(!casresp.toString().equals("OK")){
                    casValue = mem.gets("queue_after");
                    queue = gson.fromJson(casValue.getValue().toString(), Queue.class);
                    if(!queue.isEmpty()){
                        queue.poll();
                        casresp = mem.cas("queue_after", casValue.getCas(), time, gson.toJson(queue));
                    }else{
                        break;
                    }
                }
            }else{
                queue = gson.fromJson(casValue.getValue().toString(), Queue.class);
                if(!queue.isEmpty()){
                    queue.poll();
                    CASResponse casresp = mem.cas("queue_after", casValue.getCas(), time, gson.toJson(queue));
                    while(!casresp.toString().equals("OK")){
                        casValue = mem.gets("queue_after");
                        queue = gson.fromJson(casValue.getValue().toString(), Queue.class);
                        if(!queue.isEmpty()){
                            queue.poll();
                            casresp = mem.cas("queue_after", casValue.getCas(), time, gson.toJson(queue));
                        }else{
                            break;
                        }
                    }
                }
            }
            return true;
        }catch(Exception ex){
            logger.error("SubObjectFromQueue error: " + ex.getMessage());
            return false;
        }
    }
    
    public static boolean SynTwoDataAfterStart(){
        try{
            Queue queue;
            MemcachedClient mem = MemcacheHelper.GetInstance();
            String out = (String)mem.get("queue_after");
            mem.delete("queue_after");
            if(out==null || out.equals("")){
                return true;
            }else{
                queue = gson.fromJson(out, Queue.class);
                if(!queue.isEmpty()){
                    String str = (String)queue.poll();
                    NotSaveMySql notSave = gson.fromJson(str, NotSaveMySql.class);
                    
                    MongoNewsService mongoS = new MongoNewsService();
                    MySqlNewsService mySqlS = new MySqlNewsService();
                    NotSaveMySqlMemcached notSaveMySqlMemcached = new NotSaveMySqlMemcached();
                    DBService dbS = new DBService();
                    while(notSave != null){
                        switch(notSave.getCategory()){
                            case 1:{
                                if(!mongoS.CheckInsertRecord(notSave.getNews())){       // Mongo CHUA thuc hien query
                                    dbS.InsertNewsTo2DB(notSave.getNews(), notSaveMySqlMemcached);
                                }else if(!mySqlS.CheckInsertRecord(notSave.getNews())){                                                  // Mongo da thuc hien Quey
                                    mySqlS.InsertNews(notSave.getNews());
                                }
                                break;
                            }
                            case 2:{
                                dbS.UpdateStatus2DB(notSave.getNews().getId(), notSaveMySqlMemcached);
                                break;
                            }
                        }
                        str = (String)queue.poll();
                        notSave = gson.fromJson(str, NotSaveMySql.class);
                    }
                }
            }
            return true;
        }catch(Exception ex){
            logger.error("SynTwoDataAfterStart error: " + ex.getMessage());
            return false;
        }
    
    }
}

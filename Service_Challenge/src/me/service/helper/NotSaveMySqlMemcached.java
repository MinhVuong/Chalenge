/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.helper;

import com.google.gson.Gson;
import java.util.LinkedList;
import java.util.Queue;
import net.spy.memcached.MemcachedClient;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class NotSaveMySqlMemcached {
    Gson gson = new Gson();
    int time = 60*60*24;
    private static Logger logger = Logger.getLogger(NotSaveMySqlMemcached.class);
    
    public boolean SaveNotSaveMySqlQueue(Queue queue){
        try{
            MemcachedClient mem = MemcacheHelper.GetInstance();
            mem.set("queue", time, gson.toJson(queue));
            return true;
        }catch(Exception ex){
            logger.error("SaveNotSaveMySqlQueue error: " + ex.getMessage());
            return false;
        }
    }
    
    public Queue GetNotSaveMySqlQueue(){
        try{
            MemcachedClient mem = MemcacheHelper.GetInstance();
            String out = (String)mem.get("queue");
            if(out==null || out.equals(""))
                return new LinkedList();
            else{
                return gson.fromJson(out, Queue.class);
            }
        }catch(Exception ex){
            logger.error("NotSaveMySqlMemcached error: " + ex.getMessage());
            return new LinkedList();
        }
    }
}

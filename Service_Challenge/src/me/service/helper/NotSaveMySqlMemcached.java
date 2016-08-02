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
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
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
    public boolean SaveAfterSynMySqlQueue(Queue queue){
        if(queue.isEmpty())
            return true;
        else{
        try{
            MemcachedClient mem = MemcacheHelper.GetInstance();
            CASValue casValue = mem.gets("queue");
            if(casValue == null || casValue.getValue().equals("")){
                mem.set("queue", time, gson.toJson(queue));
            }else{
                Queue temp = gson.fromJson(casValue.getValue().toString(), Queue.class);
                queue = MergerTwoQueue(queue, temp);
                CASResponse casresp = mem.cas("queue", casValue.getCas(), time, gson.toJson(queue));
                while(!casresp.toString().equals("OK")){
                    temp = gson.fromJson(casValue.getValue().toString(), Queue.class);
                    queue = MergerTwoQueue(queue, temp);
                    casresp = mem.cas("queue", casValue.getCas(), time, gson.toJson(queue));
                }
            }
            return true;
        }catch(Exception ex){
            logger.error("SaveAfterSynMySqlQueue error: " + ex.getMessage());
            return false;
        }}
    }
    public Queue MergerTwoQueue(Queue a, Queue b){
        Queue result = a;
        while(!b.isEmpty()){
            String str = (String)b.poll();
            NotSaveMySql notSave = gson.fromJson(str, NotSaveMySql.class);
            result.add(gson.toJson(notSave));
        }
        return result;
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
            logger.error("GetNotSaveMySqlQueue error: " + ex.getMessage());
            return new LinkedList();
        }
    }
    
    public Queue GetBeforeAsynNotSaveMySqlQueue(){
        try{
            MemcachedClient mem = MemcacheHelper.GetInstance();
            CASValue casValue = mem.gets("queue");
            if(casValue == null || casValue.getValue().equals("")){
                return new LinkedList();
            }else{
                Queue empty = new LinkedList();
                CASResponse casresp = mem.cas("queue", casValue.getCas(), time, gson.toJson(empty));
                while(!casresp.toString().equals("OK")){
                    casValue = mem.gets("queue");
                    casresp = mem.cas("queue", casValue.getCas(), time, gson.toJson(empty));
                }
                //mem.delete("queue");
                return gson.fromJson(casValue.getValue().toString(), Queue.class);
            }
        }catch(Exception ex){
            logger.error("GetBeforeAsynNotSaveMySqlQueue error: " + ex.getMessage());
            return new LinkedList();
        }
    }
    
    public boolean AddNotSaveMySql(NotSaveMySql notSave){
        try{
            MemcachedClient mem = MemcacheHelper.GetInstance();
            
            Queue queue;
            CASValue casValue = mem.gets("queue");
            if(casValue == null || casValue.getValue().equals("")){
                queue = new LinkedList();
                queue.add(gson.toJson(notSave));
                mem.set("queue", time, gson.toJson(queue));
            }else{
                queue = gson.fromJson(casValue.getValue().toString(), Queue.class);
                queue.add(gson.toJson(notSave));
                CASResponse casresp = mem.cas("queue", casValue.getCas(), time, gson.toJson(queue));
                while(!casresp.toString().equals("OK")){
                    casValue = mem.gets("queue");
                    queue = gson.fromJson(casValue.getValue().toString(), Queue.class);
                    queue.add(gson.toJson(notSave));
                    casresp = mem.cas("queue", casValue.getCas(), time, gson.toJson(queue));
                }
            }
            return true;
        }catch(Exception ex){
            logger.error("AddNotSaveMySql error: " + ex.getMessage(), ex);
            return false;
        }
    }
}

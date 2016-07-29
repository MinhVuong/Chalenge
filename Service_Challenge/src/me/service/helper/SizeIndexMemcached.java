/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.helper;

import java.util.concurrent.Future;
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class SizeIndexMemcached {
    static int time = 60*60*24;
    private static Logger logger = Logger.getLogger(SizeIndexMemcached.class);
    
    
    public static boolean SaveSizeIndex(int size){
        try{
            MemcachedClient mem = MemcacheHelper.GetInstance();
            Future fo = mem.set("indexR", time, size);
            return true;
        }catch(Exception ex){
            logger.error("SaveSizeIndex error: " + ex.getMessage());
            return false;
        }
    }
    
    public static int GetAndSaveSizeIndex(){
        try{
            MemcachedClient mem = MemcacheHelper.GetInstance();
            CASValue casValue = mem.gets("indexR");
            int size = (int)casValue.getValue();
            CASResponse casresp = mem.cas("indexR", casValue.getCas(), time, ++size);
            while(!casresp.toString().equals("OK")){
                casValue = mem.gets("indexR");
                size = (int)casValue.getValue();
                casresp = mem.cas("indexR", casValue.getCas(), time, ++size);
            }
            return size;            
        }catch(Exception ex){
            logger.error("GetAndSaveSizeIndex error: " + ex.getMessage());
            return 0;
        }
    }
    
}

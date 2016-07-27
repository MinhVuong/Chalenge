/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.helper;

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
            mem.delete("indexR");
            mem.add("indexR", time, size);
            return true;
        }catch(Exception ex){
            logger.error("SaveSizeIndex error: " + ex.getMessage());
            return false;
        }
    }
    
    public static int GetAndSaveSizeIndex(){
        try{
            MemcachedClient mem = MemcacheHelper.GetInstance();
            int size = (int)mem.get("indexR");
            /*while(size==0){
                Thread.sleep(10);
                size = (int)mem.get("indexR");
            }*/
            mem.delete("indexR");
            mem.add("indexR", time, ++size);
            return size;
        }catch(Exception ex){
            logger.error("SaveSizeIndex error: " + ex.getMessage());
            return 0;
        }
    }
}

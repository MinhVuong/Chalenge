/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.helper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class MemcacheHelper {
    //create an object of SingleObject
    private static MemcachedClient memcache;
    private static Logger logger = Logger.getLogger(MemcacheHelper.class);
    private MemcacheHelper() {
    }

    public static MemcachedClient GetInstance() {
        return memcache;
    }

    public static boolean ConnectMemcache() throws InterruptedException, ExecutionException {
        try {
            memcache = new MemcachedClient(AddrUtil.getAddresses("127.0.0.1:11211"));
            if(memcache == null)
                return false;
            
            memcache.set("vuong", 0, "vuong");
            String str = (String)memcache.get("vuong");
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("Connect Memcached error: "+e.getMessage());
            return false;
        }
    }
}

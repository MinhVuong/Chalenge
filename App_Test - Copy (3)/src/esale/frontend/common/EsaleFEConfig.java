/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esale.frontend.common;

import com.vng.jcore.common.Config;
import esale.frontend.entity.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author sonnh4
 */
public class EsaleFEConfig {
    
    private static Logger logger_ = Logger.getLogger(EsaleFEConfig.class);
    
    public static JettyThreadPool gJettyThreadPool;
    public static AddressService gMemcache;
    public static String gSiteURL;
    public static String timeDeploy;
    
    //permission
    public final static int CONFIG = 1;
    public final static int REPORT = 2;
    public final static int RETRYSMS = 4;
    // path css/js/image
    public static String path;
    public static void loadConfig(){
        
        try{
        
            gJettyThreadPool = new JettyThreadPool();
            String jettyMinPool = Config.getParam("jetty_threadpool", "minthread");
            String jettyMaxPool = Config.getParam("jetty_threadpool", "maxthread");
            String acceptors = Config.getParam("jetty_threadpool", "acceptors");
            gJettyThreadPool.setMaxPool(jettyMaxPool);
            gJettyThreadPool.setMinPool(jettyMinPool);
            gJettyThreadPool.setAcceptors(acceptors);
            
            gMemcache = new AddressService();
            String memcacheHost = Config.getParam("Memcache", "host");
            String memcachePort = Config.getParam("Memcache", "port");
            gMemcache.setHost(memcacheHost);
            gMemcache.setPort(memcachePort);
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            timeDeploy = dateFormat.format(cal.getTime());
            
            // get path
            path = Config.getParam("urlpath", "urlpath");
        }
        catch(Exception ex){
            logger_.error("Read config file fail", ex);
            System.exit(3);
        }
        
    }
    
}

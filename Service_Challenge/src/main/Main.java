/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.vng.jcore.common.LogUtil;
import esale.frontend.common.EsaleFEConfig;
import httpservice.WebServer;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import me.service.helper.MemcacheHelper;
import me.service.helper.MongoDBHelper;
import me.service.helper.MySQLHelper;
import me.service.helper.NotSaveMySqlMemcached;
import me.service.helper.QueueAfterMemcached;
import me.service.helper.SizeIndexMemcached;
import me.service.helper.SynchThread;
import me.service.helper.ThreadQueue;
import me.service.model.News;
import me.service.model.NotSaveMySql;
import me.service.myservice.MongoNewsService;
import me.service.myservice.MySqlNewsService;
import me.service.repository.NewsDAO;
import org.apache.log4j.Logger;

/**
 *
 * @author vuongtm
 */
public class Main {
    
    private static Logger logger_ = Logger.getLogger(Main.class);
    static Gson gson = new Gson();
    public static void main(String[] args) {
        
        
        LogUtil.init();
        EsaleFEConfig.loadConfig();
        String pidFile = System.getProperty("pidfile");

        try {

            if (pidFile != null) {
                new File(pidFile).deleteOnExit();
            }
            
            if (System.getProperty("foreground") == null) {
                System.out.close();
                System.err.close();
            }

            // Start REST service
            WebServer webserver = new WebServer();
            webserver.start();
            if(!MongoDBHelper.ConnectionDatabase() || !MongoDBHelper.TestConnectionDatabase()){
                logger_.error("Exception at startup: Don't connect to NoSQL MongoDB");
                System.exit(3);
            }
            
            if(!MySQLHelper.StartConnectDatabase() || !MySQLHelper.TestConnectionDatabase()){
                logger_.error("Exception at startup: Don't connect to MySQL");
                System.exit(3);
            }
            if(!MemcacheHelper.ConnectMemcache()){
                logger_.error("Exception at startup: Don't connect to Memcached");
                System.exit(3);
            }
            
            
            int size = NewsDAO.GetSizeIndexNews();            // Lay kich thuoc cua Table de lam khoa chinh--Fail
            SizeIndexMemcached.SaveSizeIndex(size);
            logger_.info("Size Request: "+size);
            
            
            // Start Thread Queue sau 30p chay 1 lan, de thuc hien cac thao tac khong dc voi MySql va dong bo Database.
           
            ThreadQueue threadQueue = new ThreadQueue();   
            threadQueue.setPriority(5);
            threadQueue.start();
            
            // Asyn 2 DB after Start
            if(!QueueAfterMemcached.SynTwoDataAfterStart()){
                logger_.error("Exception at startup: Don't Synchronous Database 2 Database!!!");
                System.exit(3);
            }
            logger_.info("Started Application!!!");
            //MySqlNewsService mongoS = new MySqlNewsService();
           // mongoS.DeleteNews(new News(1, "", 1, ""));
        } catch (Throwable e) {
            logger_.error("Exception at startup: " + e.getMessage());
            System.exit(3);
        }
        
    }
    
}

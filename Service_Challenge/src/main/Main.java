/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.vng.jcore.common.LogUtil;
import esale.frontend.common.EsaleFEConfig;
import httpservice.WebServer;
import java.io.File;
import me.service.controller.InsertDB;
import me.service.helper.MongoDBHelper;
import org.apache.log4j.Logger;

/**
 *
 * @author sonnh4
 */
public class Main {
    
    private static Logger logger_ = Logger.getLogger(Main.class);
    
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
            if(!MongoDBHelper.ConnectionDatabase()){
                logger_.error("Exception at startup: Don't connect to NoSQL MongoDB");
                System.exit(3);
            }
            InsertDB insert = new InsertDB();
            insert.TestInsert();
            String temp = insert.RetreviewDB();
            logger_.info("insert db: "+temp);

        } catch (Throwable e) {
            logger_.error("Exception at startup: " + e.getMessage());
            System.exit(3);
        }
        
    }
    
}

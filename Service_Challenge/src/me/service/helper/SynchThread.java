/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.helper;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import me.service.model.NotSaveMySql;
import me.service.myservice.MySqlNewsService;

/**
 *
 * @author Kuti
 */
public class SynchThread extends Thread {

    private Logger logger = Logger.getLogger(SynchThread.class);
    Gson gson = new Gson();
    public static Queue synchDB = new LinkedList();
    MySqlNewsService mySqlS = new MySqlNewsService();
    //NotSaveMySqlMemcached notSaveMySqlMemcached = new NotSaveMySqlMemcached();
    public void run() {
        while(true){
            try {
                //logger.info("SynchThread is running...");
                while (!synchDB.isEmpty()) {
                    NotSaveMySql notSave = (NotSaveMySql) synchDB.poll();
                    //NotSaveMySql notSave = gson.fromJson(str, NotSaveMySql.class);
                    //logger.info("SynchThread not empty");
                    switch (notSave.getCategory()) {
                        case 1: {
                            //Queue notSaveMySql = notSaveMySqlMemcached.GetNotSaveMySqlQueue();
                            //if (notSaveMySql.isEmpty() && mySqlS.InsertNews(notSave.getNews())){ // Neu khong thuc hien duoc thi phai luu lai de lan sau thuc hien
                            //}else{
                            //    notSaveMySql.add(gson.toJson(notSave));
                            //    notSaveMySqlMemcached.SaveNotSaveMySqlQueue(notSaveMySql);
                            //}
                            break;
                        }
                        case 2: {
                            //Queue notSaveMySql = notSaveMySqlMemcached.GetNotSaveMySqlQueue();
                            //if (notSaveMySql.isEmpty() && mySqlS.UpdateStatus(notSave.getNews().getId())) {
                            //}else{
                            //    notSaveMySql.add(gson.toJson(notSave));
                            //    notSaveMySqlMemcached.SaveNotSaveMySqlQueue(notSaveMySql);
                            //}
                            break;
                        }
                    }
                }
                sleep(60*60);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(SynchThread.class.getName()).log(Level.SEVERE, null, ex);
            }
    
        }
    }
}
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
import me.service.myservice.MongoNewsService;
import me.service.myservice.MySqlNewsService;
import org.apache.log4j.Logger;

/**
 *
 * @author Kuti
 */
public class ThreadQueue extends Thread{
    private Logger logger = Logger.getLogger(ThreadQueue.class);
    Gson gson = new Gson();
    //public static Queue notSaveMySql = new LinkedList();
    public void run(){
        while(true){
            try{
                logger.info("thread is running...");
                NotSaveMySqlMemcached notSaveMySqlMemcached = new NotSaveMySqlMemcached();
                Queue notSaveMySql = notSaveMySqlMemcached.GetBeforeAsynNotSaveMySqlQueue();
                if(!notSaveMySql.isEmpty()){
                    if(MySQLHelper.StartConnectDatabase()){
                        Queue temp = new LinkedList();      // De luu lai cac thao tac van thuc hien khong duoc.
                        String str = (String)notSaveMySql.poll();
                        NotSaveMySql notSave = gson.fromJson(str, NotSaveMySql.class);       // Get phan tu dau tien trong Queue va xoa luon trong Queue
                        while(notSave != null && MySQLHelper.connect==true){             // Thuc hien cho den khi nao trong Queue het phan tu.
                            MySqlNewsService mySqlS = new MySqlNewsService();
                            switch(notSave.getCategory()){
                                case 1:{
                                    if(!mySqlS.InsertNews(notSave.getNews()))       // Neu khong thuc hien duoc thi phai luu lai de lan sau thuc hien
                                        temp.add(gson.toJson(notSave));
                                    break;
                                }
                                case 2:{
                                    if(!mySqlS.UpdateStatus(notSave.getNews().getId()))
                                        temp.add(gson.toJson(notSave));
                                    break;
                                }
                            }
                            str = (String)notSaveMySql.poll();
                            notSave = gson.fromJson(str, NotSaveMySql.class);           // Lay phan tu dau tien cua Queue va xoa luon trong Queue
                        }
                        if(!notSaveMySql.isEmpty()){
                            temp = MergerTwoQueue(temp, notSaveMySql);
                        }
                        if(!notSaveMySqlMemcached.SaveAfterSynMySqlQueue(temp))
                            DeleteMySqlQueueNotSynMySql(temp);
                    }else{
                        if(!notSaveMySqlMemcached.SaveAfterSynMySqlQueue(notSaveMySql))
                            DeleteMySqlQueueNotSynMySql(notSaveMySql);
                        logger.info("Thread: Don't connect to MySQL Database!!!");
                    }
                }else{
                    logger.info("Not Save MySql Queue Empty!!!");
                }
                sleep(1000*60);         // Demo sau 1p thi thread chay 1 lan.
            }catch(Exception ex){
                logger.error("Thread Queue error: "+ex.getMessage());
            }
        }
        
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
    
    public void DeleteMySqlQueueNotSynMySql(Queue queue){
        MongoNewsService mongoS = new MongoNewsService();
        MySqlNewsService mySqlS = new MySqlNewsService();
        while(!queue.isEmpty()){
            String str = (String)queue.poll();
            NotSaveMySql notSave = gson.fromJson(str, NotSaveMySql.class);
            switch(notSave.getCategory()){
                case 1:{
                    mongoS.DeleteNews(notSave.getNews());
                    //mySqlS.DeleteNews(notSave.getNews());
                    break;
                }
                case 2:{
                    mongoS.RetryUpdateStatus(notSave.getNews().getId());
                    break;
                }
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.helper;

import com.google.gson.Gson;
import java.io.File;
import java.util.Queue;
import java.util.regex.Pattern;
import me.service.model.News;
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
                File[] files = FileThreadHelper.ReadAllFileNewsNotSaveMySql();
                if(files != null && files.length>0){
                    if(MySQLHelper.StartConnectDatabase()){
                        MySqlNewsService mySqlS = new MySqlNewsService();
                        for(File file : files){
                            NotSaveMySql notS = GetNotSaveMySqlFromFile(file.getName());
                            if(notS != null && MySQLHelper.connect){
                                switch(notS.getCategory()){
                                    case 1:{
                                        if(mySqlS.InsertNews(notS.getNews())){       // Neu khong thuc hien duoc thi phai luu lai de lan sau thuc hien
                                            if(!FileThreadHelper.SubFileNewsNotSaveMySql(notS)){
                                                mySqlS.DeleteNews(notS.getNews());
                                            }
                                        }
                                        break;
                                    }
                                    case 2:{
                                        if(mySqlS.UpdateStatus(notS.getNews().getId())){
                                            FileThreadHelper.SubFileNewsNotSaveMySql(notS);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }else{
                        logger.info("Thread Queue: Don't connect to MySQL Database!!!");
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
    
    private NotSaveMySql GetNotSaveMySqlFromFile(String str){
        try{
            NotSaveMySql notSave = new NotSaveMySql();
            String[] arr = str.split(Pattern.quote("."));
            String[] arr1 = arr[0].split("_");
            String temp = arr1[2];
            temp = temp.replace("-", "/");
            temp = temp.replace("+", ":");
            switch(Integer.parseInt(arr1[0])){
                case 1:{
                    News news = new News(Integer.parseInt(arr1[1]), "Noi dung "+arr1[1], 1, temp);
                    notSave = new NotSaveMySql(1, news);
                    break;
                }
                case 2:{
                    News news = new News(Integer.parseInt(arr1[1]), "Noi dung "+arr1[1], 0, temp);
                    notSave = new NotSaveMySql(2, news);
                    break;
                }
            }
            return notSave;
        }catch(Exception ex){
            logger.error("CutContentGetCutDynch error: " + ex.getMessage(), ex);
            return null;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.helper;

import java.util.LinkedList;
import java.util.Queue;
import me.service.model.NotSaveMySql;
import me.service.myservice.MySqlNewsService;
import org.apache.log4j.Logger;

/**
 *
 * @author Kuti
 */
public class ThreadQueue extends Thread{
    private Logger logger = Logger.getLogger(ThreadQueue.class);
    public static Queue notSaveMySql = new LinkedList();
    public void run(){
        while(true){
            try{
                logger.info("thread is running...");
                if(!notSaveMySql.isEmpty()){
                    if(MySQLHelper.StartConnectDatabase()){
                        Queue temp = new LinkedList();      // De luu lai cac thao tac van thuc hien khong duoc.
                        NotSaveMySql notSave = (NotSaveMySql)notSaveMySql.poll();       // Get phan tu dau tien trong Queue va xoa luon trong Queue
                        while(notSave != null){     // Thuc hien cho den khi nao trong Queue het phan tu.
                            MySqlNewsService mySqlS = new MySqlNewsService();
                            switch(notSave.getCategory()){
                                case 1:{
                                    if(!mySqlS.InsertNews(notSave.getNews()))       // Neu khong thuc hien duoc thi phai luu lai de lan sau thuc hien
                                        temp.add(notSave);
                                    break;
                                }
                                case 2:{
                                    break;
                                }
                            }
                            notSave = (NotSaveMySql)notSaveMySql.poll();            // Lay phan tu dau tien cua Queue va xoa luon trong Queue
                        }
                        notSaveMySql = temp;
                    }else{
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
}

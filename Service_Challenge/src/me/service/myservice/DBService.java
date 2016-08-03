/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.myservice;

import com.google.gson.Gson;
import me.service.helper.FileHelper;
import me.service.helper.MySQLHelper;
import me.service.model.News;
import me.service.model.NotSaveMySql;
import org.apache.log4j.Logger;

/**
 *
 * @author Kuti
 */
public class DBService {

    private Logger logger = Logger.getLogger(DBService.class);
    MongoNewsService mongoS = new MongoNewsService();
    MySqlNewsService mysqlS = new MySqlNewsService();
    Gson gson = new Gson();

    public boolean InsertNewsTo2DB(News news) {
        NotSaveMySql notSave = new NotSaveMySql(1, news);
        if(FileHelper.AddFileNews(notSave)){
            if (mongoS.InsertNews(news)) {
                if (mysqlS.InsertNews(news)) {
                    if(!FileHelper.SubFileNews(notSave)){
                        //mongoS.DeleteNews(news);
                        //mysqlS.DeleteNews(news);
                        return false;
                    }
                }
                return true;
            } else {
                FileHelper.SubFileNews(notSave);                                           // Xoa doi tuong da luu trong Cache vi 2 DB da Sync
                logger.info("InsertNewsTo2DB error: don't insert data!!!");
                return false;
            }
        }else{
            FileHelper.SubFileNews(notSave);
            logger.info("InsertNewsTo2DB error: don't insert data!!!");
            return false;
        }
    }

    public boolean UpdateStatus2DB(int id) {
        NotSaveMySql notSave = new NotSaveMySql(2, new News(id, "", 0, ""));
        if(FileHelper.AddFileNews(notSave)){
            if (mongoS.UpdateStatus(id)) {
                if (MySQLHelper.connect && mysqlS.UpdateStatus(id)) {
                    if(!FileHelper.SubFileNews(notSave)){
                        mongoS.RetryUpdateStatus(id);
                        mysqlS.RetryUpdateStatus(id);
                        return false;
                    }
                }
                return true;
            }else {
                FileHelper.SubFileNews(notSave);                                           // Xoa doi tuong da luu trong Cache vi 2 DB da Sync
                logger.info("UpdateStatus2DB error: don't update data!!!");
                return false;
            }
        }else{
            FileHelper.SubFileNews(notSave);
            logger.info("UpdateStatus2DB error: don't update data!!!");
            return false;
        }
    }
}

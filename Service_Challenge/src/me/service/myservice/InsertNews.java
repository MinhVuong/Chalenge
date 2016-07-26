/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.myservice;

import com.google.gson.Gson;
import java.util.Queue;
import java.util.logging.Level;
import me.service.helper.NotSaveMySqlMemcached;
import me.service.helper.QueueAfterMemcached;
import me.service.model.News;
import me.service.model.NotSaveMySql;
import org.apache.log4j.Logger;

/**
 *
 * @author Kuti
 */
public class InsertNews {

    private Logger logger = Logger.getLogger(InsertNews.class);
    MongoNewsService mongoS = new MongoNewsService();
    MySqlNewsService mysqlS = new MySqlNewsService();
    Gson gson = new Gson();

    public boolean InsertNewsTo2DB(News news, NotSaveMySqlMemcached notSaveMySqlMemcached) {
        NotSaveMySql notSave = new NotSaveMySql(1, news);
        QueueAfterMemcached.AddObjectToQueue(notSave);          // Luu vao Cache de phong cup dien mk chua Sync
        if (mongoS.InsertNews(news)) {
            Queue notSaveMySql = notSaveMySqlMemcached.GetNotSaveMySqlQueue();
            if (notSaveMySql.isEmpty() && mysqlS.InsertNews(news)) {
            } else {        // Neu insert MySql khong thanh cong thi se luu lai vao Queue de Sau nay co the thao tac lai.
                notSaveMySql.add(gson.toJson(notSave));
                notSaveMySqlMemcached.SaveNotSaveMySqlQueue(notSaveMySql);
            }
            QueueAfterMemcached.SubObjectFromQueue();           // Xoa doi tuong da luu trong Cache vi 2 DB da Sync
            return true;

        } else {
            return false;
        }
    }
}

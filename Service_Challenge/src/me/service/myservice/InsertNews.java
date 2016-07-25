/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.myservice;

import me.service.helper.ThreadQueue;
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

    public boolean InsertNewsTo2DB(News news) {
        if (mongoS.InsertNews(news)) {
            if (ThreadQueue.notSaveMySql.isEmpty() && mysqlS.InsertNews(news)) {
            } else {        // Neu insert MySql khong thanh cong thi se luu lai vao Queue de Sau nay co the thao tac lai.
                NotSaveMySql notSave = new NotSaveMySql();
                notSave.setCategory(1);
                notSave.setNews(news);
                ThreadQueue.notSaveMySql.add(notSave);
            }
            return true;
        } else {
            return false;
        }
    }
}

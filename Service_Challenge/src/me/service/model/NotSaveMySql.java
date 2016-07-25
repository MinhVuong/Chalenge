/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.model;

/**
 *
 * @author Kuti
 */
public class NotSaveMySql implements java.io.Serializable{
    private int category;
    private News news;

    public NotSaveMySql() {
    }

    public NotSaveMySql(int category, News news) {
        this.category = category;
        this.news = news;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
    
}

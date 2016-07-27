/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.pakage;

import java.util.List;
import me.service.model.News;

/**
 *
 * @author CPU01661-local
 */
public class GetPakage {
    private List<News> newss;

    public GetPakage() {
    }

    public GetPakage(List<News> newss) {
        this.newss = newss;
    }

    public List<News> getNewss() {
        return newss;
    }

    public void setNewss(List<News> newss) {
        this.newss = newss;
    }
    
}

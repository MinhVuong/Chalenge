/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.model;

/**
 *
 * @author CPU01661-local
 */
public class CutSynch {
    private int cate;
    private int id;

    public CutSynch() {
    }

    public CutSynch(int cate, int id) {
        this.cate = cate;
        this.id = id;
    }

    public int getCate() {
        return cate;
    }

    public void setCate(int cate) {
        this.cate = cate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}

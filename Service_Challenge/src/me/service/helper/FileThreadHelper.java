/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.service.model.NotSaveMySql;
import org.apache.log4j.Logger;

/**
 *
 * @author Kuti
 */
public class FileThreadHelper {
    private static Logger logger = Logger.getLogger(FileHelper.class);
    private static String folderName_thread = "synch_thread";
    public static boolean AddFileNewsNotSaveMySql(NotSaveMySql notSave) {
        try {
            long start = System.currentTimeMillis();
            String str = notSave.getNews().getTime();
            str = str.replace("/", "-");
            str = str.replace(":", "+");
            String path = folderName_thread + "/" + notSave.getCategory() + "_" + notSave.getNews().getId() + "_" + str + ".txt";
            File file = new File(path);
            if (file.createNewFile()) {
                //logger.info("FileHelper: Thoi gian tao file: " + (System.currentTimeMillis()-start));
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            logger.error("AddFileNewsNotSaveMySql error: " + ex.getMessage(), ex);
            return false;
        }
    }
    
    public static boolean SubFileNewsNotSaveMySql(NotSaveMySql notSave) {
        try {
            long start = System.currentTimeMillis();
            String str = notSave.getNews().getTime();
            str = str.replace("/", "-");
            str = str.replace(":", "+");
            String path = folderName_thread + "/" + notSave.getCategory() + "_" + notSave.getNews().getId() + "_" + str + ".txt";
            File file = new File(path);
            if (file.delete()) {
                //logger.info("FileHelper: Thoi gian xoa file: " + (System.currentTimeMillis()-start));
                return true;
            } else {
                logger.info("Khong xoa duoc file----------------------------------");
                
                if(file.exists()){
                    return false;
                }else
                    return true;
                
            }
        } catch (Exception ex) {
            logger.error("SubFileNewsNotSaveMySql error: " + ex.getMessage(), ex);
            return false;
        }
    }
    
    public static File[] ReadAllFileNewsNotSaveMySql() {
        try {
            List<String> files = new ArrayList<String>();
            File folder = new File(folderName_thread);
            File[] listOfFiles = folder.listFiles();
            return listOfFiles;
        } catch (Exception ex) {
            logger.error("ReadAllFileNewsNotSaveMySql error: " + ex.getMessage(), ex);
            return null;
        }
    }
}

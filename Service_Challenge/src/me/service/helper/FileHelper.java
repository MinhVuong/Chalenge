/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.helper;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import me.service.model.News;
import me.service.model.NotSaveMySql;
import me.service.myservice.DBService;
import me.service.myservice.MongoNewsService;
import me.service.myservice.MySqlNewsService;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class FileHelper {

    private static Logger logger = Logger.getLogger(FileHelper.class);
    private static String folderName = "synch";

    public static boolean AddFileNews(NotSaveMySql notSave) {
        try {
            long start = System.currentTimeMillis();
            String str = notSave.getNews().getTime();
            str = str.replace("/", "-");
            str = str.replace(":", "+");
            String path = folderName + "/" + notSave.getCategory() + "_" + notSave.getNews().getId() + "_" + str + ".txt";
            File file = new File(path);
            if (file.createNewFile()) {
                //logger.info("FileHelper: Thoi gian tao file: " + (System.currentTimeMillis()-start));
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            logger.error("AddFileNews error: " + ex.getMessage(), ex);
            return false;
        }
    }

    public static boolean SubFileNews(NotSaveMySql notSave) {
        try {
            long start = System.currentTimeMillis();
            String str = notSave.getNews().getTime();
            str = str.replace("/", "-");
            str = str.replace(":", "+");
            String path = folderName + "/" + notSave.getCategory() + "_" + notSave.getNews().getId() + "_" + str + ".txt";
            File file = new File(path);
            if (file.delete()) {
                //logger.info("FileHelper: Thoi gian xoa file: " + (System.currentTimeMillis()-start));
                return true;
            } else {
                logger.info("Khong xoa duoc file----------------------------------");
                return false;
            }
        } catch (Exception ex) {
            logger.error("SubFileNews error: " + ex.getMessage(), ex);
            return false;
        }
    }

    public static File[] ReadAllFileNews() {
        try {
            List<String> files = new ArrayList<String>();
            File folder = new File(folderName);
            File[] listOfFiles = folder.listFiles();

            return listOfFiles;
        } catch (Exception ex) {
            logger.error("ReadAllFileNews error: " + ex.getMessage(), ex);
            return null;
        }
    }

    public static boolean SynTwoDataAfterStart() {
        try {
            boolean flag = true;
            File[] files = ReadAllFileNews();
            if (files != null && files.length > 0) {
                MySqlNewsService mySqlS = new MySqlNewsService();
                for (File file : files) {
                    NotSaveMySql notS = GetNotSaveMySqlFromFile(file.getName());
                    MongoNewsService mongoS = new MongoNewsService();
                    DBService dbS = new DBService();
                    if (notS != null && MySQLHelper.connect) {
                        switch (notS.getCategory()) {
                            case 1: {
                                if(mongoS.CheckInsertRecord(notS.getNews())){
                                    if (mySqlS.InsertNews(notS.getNews())) {       // Neu khong thuc hien duoc thi phai luu lai de lan sau thuc hien
                                        if (!FileHelper.SubFileNews(notS)) {
                                            mySqlS.DeleteNews(notS.getNews());
                                            mongoS.DeleteNews(notS.getNews());
                                            logger.error("Khong the xoa file synch!!!!");
                                            flag = false;
                                        }
                                    }else{
                                        logger.error("Khong insert duoc MySql!!!!");
                                        flag = false;
                                    }
                                }else{
                                    if (!FileHelper.SubFileNews(notS)) {
                                        logger.error("Khong the xoa file synch!!!!");
                                        flag = false;
                                    }
                                }
                                
                                break;
                            }
                            case 2: {
                                dbS.UpdateStatus2DB(notS.getNews().getId());
                                break;
                            }
                        }
                    }else{
                        logger.error("Khong ket noi duoc den MySql!!!!");
                        flag = false;
                    }
                }
            } else {
                logger.error("Khong co du lieu de dong do Data!!!!");
            }
            return flag;
        } catch (Exception ex) {
            logger.error("SynTwoDataAfterStart error: " + ex.getMessage());
            return false;
        }
    }

    private static NotSaveMySql GetNotSaveMySqlFromFile(String str) {
        try {
            NotSaveMySql notSave = new NotSaveMySql();
            String[] arr = str.split(Pattern.quote("."));
            String[] arr1 = arr[0].split("_");
            String temp = arr1[2];
            temp = temp.replace("-", "/");
            temp = temp.replace("+", ":");
            switch (Integer.parseInt(arr1[0])) {
                case 1: {
                    News news = new News(Integer.parseInt(arr1[1]), "Noi dung " + arr1[1], 1, temp);
                    notSave = new NotSaveMySql(1, news);
                    break;
                }
                case 2: {
                    News news = new News(Integer.parseInt(arr1[1]), "Noi dung " + arr1[1], 0, temp);
                    notSave = new NotSaveMySql(2, news);
                    break;
                }
            }
            return notSave;
        } catch (Exception ex) {
            logger.error("CutContentGetCutDynch error: " + ex.getMessage(), ex);
            return null;
        }
    }
}

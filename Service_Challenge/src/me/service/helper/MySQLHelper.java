/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.service.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author CPU01661-local
 */
public class MySQLHelper {

    // JDBC driver name and database URL

    private static Logger logger = Logger.getLogger(MySQLHelper.class);
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/challenge?useUnicode=true&characterEncoding=UTF-8";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";

    //create an object of SingleObject
    private static Connection instance;
    public static boolean connect=false;

    private MySQLHelper() {
    }

    //Get the only object available

    public static Connection getInstance() throws ClassNotFoundException, SQLException {
        return instance;
    }

    public static boolean StartConnectDatabase() throws ClassNotFoundException, SQLException {
        if(!connect){
            try {
                Class.forName(JDBC_DRIVER);
                instance = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);

                if (instance == null) {
                    connect = false;
                    return false;
                } else {
                    connect = true;
                    return true;
                }
            } catch (Exception ex) {
                logger.error("Connect MySql fail: " + ex.getMessage());
                connect = false;
                return false;
            }
        }else{
            return true;
        }
    }
}

package com.revature.ticketer.utils;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/*
 * Bridges the DAO classes with the database
 * Singleton design pattern (prevents multiple connections
 * at the same time since that may cause issues)
 */
public class ConnectionFactory {
    //This field stores the singleton object connectionFactory
    private static ConnectionFactory connectionFactory;

    // Used to load in the jdbc
    //Since it is a static only method, this means it is loaded
    //before anything needs to be initialized
    static {
        try {
            //loading the jdbc driver to establish a connection
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    //This class reads the properties file. Ensures the right database is selected
    private final Properties props = new Properties();

    private ConnectionFactory(){
        //Attempts to open the dp.properties
        try {
            props.load(new FileReader("src/main/resources/db.properties"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /*
     * Controls access to the singelton instance
     * by ensuring the instance hasn't been initialized
     */
    public static ConnectionFactory getInstance(){
         if (connectionFactory == null) {
            connectionFactory = new ConnectionFactory();
        }
        return connectionFactory;
    }

    //Checks to make sure we establish a valid connection with our database
    public Connection getConnection() throws SQLException{
        Connection con = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"),
            props.getProperty("password"));
        if (con == null) throw new RuntimeException("ERROR: Failed to establish a connection with the database");
        return con;
    }
}

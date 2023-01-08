package com.eventlogmonitor.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionSingleton {
    private final static Logger logger =
            LoggerFactory.getLogger(ConnectionSingleton.class);
    private static ConnectionSingleton instance;
    private final String db = "jdbc:hsqldb:file:db/testdb;ifexists=false";
    private Connection connection;

    private ConnectionSingleton() {

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            this.connection = DriverManager.getConnection(db, "SA", "");

        } catch (Exception e) {

        }
    }

    public static ConnectionSingleton getInstance() {
        if (instance == null) {
            instance = new ConnectionSingleton();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

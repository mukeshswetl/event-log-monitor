package com.eventlogmonitor.db;

import org.hsqldb.HsqlException;
import org.hsqldb.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eventlogmonitor.models.AlertEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseConnection implements DatabaseInterface {
    private final static Logger logger =
            LoggerFactory.getLogger(DatabaseConnection.class);
    private static final String DB_NAME = "eventlogmonitor";
    private static final String DB_PATH = "data/file:events";
    private final static String sql = "INSERT INTO events (logId, duration, alertType, host, alert)  VALUES (?, ?, ?, ?, ?);";
    private final static String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS events(id INTEGER IDENTITY PRIMARY KEY, logId VARCHAR(50) NOT NULL, duration BIGINT, alertType VARCHAR(50), host VARCHAR(50), alert BOOLEAN DEFAULT TRUE NOT NULL);";
    private final Server hsqlServer = new Server();
    public ConnectionSingleton connectionSingleton;


    public void startServer() {
        connectionSingleton = ConnectionSingleton.getInstance();
        hsqlServer.setLogWriter(null);
        hsqlServer.setSilent(true);
        hsqlServer.setDatabaseName(0, DB_NAME);
        hsqlServer.setDatabasePath(0, DB_PATH);
        hsqlServer.start();

    }

    public boolean createTable() throws HsqlException {
        Statement statement;
        try {

            statement = connectionSingleton.getConnection().createStatement();
            statement.executeUpdate(CREATE_TABLE_SQL);
            logger.info("TABLE events created");

            return true;

        } catch (SQLException e) {
            logger.error("SQL EXCEPTION ", e);
            return false;
        }

    }

    public void select() {


        try {
            ResultSet result = connectionSingleton.getConnection().prepareStatement("SELECT * FROM events").executeQuery();
            while (result.next()) {
                logger.debug(result.getString("logId") + " | " + result.getInt("duration") + " | " + result.getString("alertType") + " | " + result.getString("host") + " | " + result.getBoolean("alert"));
            }

        } catch (SQLException e) {
            logger.error("exception occurred: ", e);
        }
    }

    public boolean save(List<AlertEvent> alertEventList) {
        int rowNum = 0;
        try {


            PreparedStatement statement = connectionSingleton.getConnection().prepareStatement(sql);

            for (AlertEvent a : alertEventList) {

                statement.setString(1, a.getId());
                statement.setLong(2, a.getDuration());
                statement.setString(3, a.getType());
                statement.setString(4, a.getHost());
                statement.setBoolean(5, a.isAlert());
                logger.info("Row inserted");
                statement.executeUpdate();
                rowNum++;

            }
            logger.info(rowNum + " rows inserted");
            connectionSingleton.getConnection().commit();
            return true;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return false;
        }
    }

    public void stopServer() {
        if (connectionSingleton.getConnection() != null) {
            try {
                connectionSingleton.getConnection().close();
                hsqlServer.stop();
                logger.info("HSQL Server was stopped");
            } catch (SQLException e) {
                logger.error("SQL Exception", e);
            }
        }
    }

    public Server getHsqlServer() {
        return hsqlServer;
    }

}

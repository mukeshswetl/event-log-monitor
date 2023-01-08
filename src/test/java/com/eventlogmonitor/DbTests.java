package com.eventlogmonitor;
import org.junit.Test;

import com.eventlogmonitor.db.DatabaseConnection;

import java.io.File;

import static org.junit.Assert.*;


public class DbTests {

    private static final String filePath = "../src/main/resources/logfile.txt";

    @Test
    public void testDBconnection() throws InterruptedException {
        DatabaseConnection db = new DatabaseConnection();
        db.startServer();
        Thread.sleep(500);

        assertNotNull(db.connectionSingleton.getConnection());
    }

    @Test
    public void checkServerRunning() throws InterruptedException {
        DatabaseConnection db = new DatabaseConnection();
        db.startServer();
        Thread.sleep(500);

        // getState() returns 1 if ONLINE, 16 if SHUTDOWN
        assertEquals(1, db.getHsqlServer().getState());

    }

    @Test
    public void checkStopServer() throws InterruptedException {
        DatabaseConnection db = new DatabaseConnection();

        db.startServer();
        db.stopServer();
        assertNotNull(db.connectionSingleton.getConnection());
        //16 is shutdown, 8 is stopping. Thread sleep was added because it takes some time for server to stop completely
        Thread.sleep(500);
        assertEquals(16, db.getHsqlServer().getState());
    }

    @Test
    public void testInsertIntoTable() {

        LogReader reader = new LogReader(filePath);
        reader.readFile(new File(filePath));
        DatabaseConnection db = new DatabaseConnection();
        db.startServer();

        db.createTable();
        assertTrue(db.save(reader.getFlaggedEvents()));
    }


}

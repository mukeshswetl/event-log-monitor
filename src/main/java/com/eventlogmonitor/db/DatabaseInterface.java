package com.eventlogmonitor.db;

import java.util.List;

import com.eventlogmonitor.models.AlertEvent;

public interface DatabaseInterface {

    void startServer();

    void select();

    boolean createTable();

    boolean save(List<AlertEvent> list);


}

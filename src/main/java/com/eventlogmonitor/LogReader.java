package com.eventlogmonitor;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eventlogmonitor.db.DatabaseConnection;
import com.eventlogmonitor.enums.State;
import com.eventlogmonitor.models.AlertEvent;
import com.eventlogmonitor.models.Event;
import com.eventlogmonitor.utils.EventFlag;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogReader {
    private final static Logger logger =
            LoggerFactory.getLogger(LogReader.class);
    private String path;
    private List<AlertEvent> flaggedEvents = new ArrayList<>();
    private EventFlag eventFlagger = new EventFlag();

    public LogReader(String path) {
        this.path = path;
    }


    public Map<String, List<Event>> readFile(File file) {
        Map<String, List<Event>> eventMap = new HashMap<>();
        JSONParser jsonParser = new JSONParser();

        try {
            Reader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String currentLine;
            // Read line by line
            while ((currentLine = bufferedReader.readLine()) != null) {

                JSONObject jsonLine = (JSONObject) jsonParser.parse(currentLine);

                Event event = new Event(jsonLine);
                String id = event.getId();

                // add another event to a list if ID already exists, otherwise create a new list and add a new ID and
                // created list to a map

                if (eventMap.containsKey(id)) {
                    eventMap.get(id).add(event);
                } else {
                    List<Event> eventList = new ArrayList<>();
                    eventList.add(event);
                    eventMap.put(id, eventList);
                }

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return eventMap;
    }


    public void findStartEndEvents(File file) {

        // iterate through the list for each key in map and find start and finish events

        for (List<Event> eventList : readFile(file).values()) {
            Event startedEvent = null;
            Event finishedEvent = null;
            for (Event event : eventList) {
                if (event.getState().equals(State.STARTED)) {
                    startedEvent = event;
                } else if (event.getState().equals(State.FINISHED)) {
                    finishedEvent = event;
                }

                if (startedEvent != null && finishedEvent != null) {
                    flaggedEvents.add(eventFlagger.createFlagEvent(startedEvent, finishedEvent));
                }
            }

        }
    }

    public void processEvents() {

        findStartEndEvents(new File(path));
        // only process the events if the flag events List is not empty
        if (!flaggedEvents.isEmpty()) {
            DatabaseConnection dbCon = new DatabaseConnection();
            dbCon.startServer();
            dbCon.createTable();
            dbCon.save(flaggedEvents);
            dbCon.select();
            dbCon.stopServer();

        }
    }


    public List<AlertEvent> getFlaggedEvents() {
        return flaggedEvents;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
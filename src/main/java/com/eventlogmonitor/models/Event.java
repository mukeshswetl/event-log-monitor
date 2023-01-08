package com.eventlogmonitor.models;

import org.json.simple.JSONObject;

import com.eventlogmonitor.enums.State;

public class Event {
    public static final String JSON_ID = "id";
    public static final String JSON_STATE = "state";
    public static final String JSON_TIMESTAMP = "timestamp";
    public static final String JSON_TYPE = "type";
    public static final String JSON_HOST = "host";


    private final String id;
    private final State state;
    private final Long timestamp;
    private final String type;
    private final String host;

    public Event(JSONObject jsonObject) {
        this.id = (String) jsonObject.get(JSON_ID);
        this.state = State.valueOf((String) jsonObject.get(JSON_STATE));
        this.timestamp = (Long) jsonObject.get(JSON_TIMESTAMP);
        this.host = (String) jsonObject.get(JSON_HOST);
        this.type = (String) jsonObject.get(JSON_TYPE);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", state=" + state +
                ", timestamp=" + timestamp +
                ", type=" + type +
                ", host=" + host +
                '}';
    }

    public String getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }
}

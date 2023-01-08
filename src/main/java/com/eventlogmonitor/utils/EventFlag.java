package com.eventlogmonitor.utils;

import com.eventlogmonitor.models.AlertEvent;
import com.eventlogmonitor.models.Event;

public class EventFlag {


    public AlertEvent createFlagEvent(Event startedEvent, Event finishedEvent) {
        boolean isAlert;
        long duration = finishedEvent.getTimestamp() - startedEvent.getTimestamp();
        isAlert = duration > 4;
        return new AlertEvent.Builder()
                .setId(startedEvent.getId())
                .setDuration(duration)
                .setHost(startedEvent.getHost())
                .setType(startedEvent.getType())
                .setAlert(isAlert)
                .build();
    }
}

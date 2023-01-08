package com.eventlogmonitor.models;

public class AlertEvent {

    private final String id;
    private final Long duration;
    private final String type;
    private final String host;
    private final Boolean isAlert;


    public AlertEvent(String id, Long duration, String type, String host, Boolean isAlert) {
        this.id = id;
        this.duration = duration;
        this.type = type;
        this.host = host;
        this.isAlert = isAlert;
    }


    public String getId() {
        return id;
    }

    public Long getDuration() {
        return duration;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public boolean isAlert() {
        return isAlert;
    }

    @Override
    public String toString() {
        return "AlertEvent{" +
                "id='" + id + '\'' +
                ", duration=" + duration +
                ", type=" + type +
                ", host=" + host +
                ", isAlert=" + isAlert +
                '}';
    }

    public static class Builder {
        private String id;
        private Long duration;
        private String type;
        private String host;
        private boolean isAlert;

        public Builder setId(String id) {
            this.id = id;
            return this;

        }

        public Builder setDuration(Long duration) {
            this.duration = duration;
            return this;

        }

        public Builder setType(String type) {
            this.type = type;
            return this;

        }

        public Builder setHost(String host) {
            this.host = host;
            return this;

        }

        public Builder setAlert(boolean isAlert) {
            this.isAlert = isAlert;
            return this;
        }

        public AlertEvent build() {
            return new AlertEvent(id, duration, type, host, isAlert);
        }
    }
}

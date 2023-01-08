package com.eventlogmonitor;


public class TestMain {


    private static final String DEFAULT_FILE = "E:/git/event-log-monitor/src/main/resources/logfile.txt";


    public static void main(String[] args) {

        String path = DEFAULT_FILE;
        LogReader reader = new LogReader(path);

        if (args.length == 1) {
            path = args[0];
        }

        reader.setPath(path);
        reader.processEvents();


    }

}

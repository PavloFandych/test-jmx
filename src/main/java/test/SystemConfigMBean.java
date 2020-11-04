/* Copyright 2020 by Avid Technology, Inc. */
package test;

/**
 * @author Pavlo.Fandych
 */
public interface SystemConfigMBean {

    void setThreadCount(int noOfThreads);

    int getThreadCount();

    /**
     * Any method starting with get and set are considered as attributes getter and setter methods, do - for operation
     */
    String doConfig();
}
/* Copyright 2020 by Avid Technology, Inc. */
package test;

/**
 * @author Pavlo.Fandych
 */
public final class SystemConfig implements SystemConfigMBean {

    private int threadCount;

    public SystemConfig(int numThreads) {
        this.threadCount = numThreads;
    }

    @Override
    public void setThreadCount(int noOfThreads) {
        this.threadCount = noOfThreads;
    }

    @Override
    public int getThreadCount() {
        return this.threadCount;
    }

    @Override
    public String doConfig() {
        return "No of Threads=" + this.threadCount;
    }
}
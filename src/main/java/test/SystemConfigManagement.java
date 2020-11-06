/* Copyright 2020 by Avid Technology, Inc. */
package test;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * @author Pavlo.Fandych
 */
public final class SystemConfigManagement {

    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException,
                                                  InstanceAlreadyExistsException, MBeanRegistrationException,
                                                  InterruptedException {
        final SystemConfigMBean mBean = new SystemConfig(10);
        ManagementFactory.getPlatformMBeanServer().registerMBean(mBean, new ObjectName("test:type=SystemConfigMBean"));
        do {
            Thread.sleep(2000);
            System.out.println("Thread Count=" + mBean.getThreadCount());
        } while (mBean.getThreadCount() != 0);
    }
}
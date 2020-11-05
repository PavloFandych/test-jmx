/* Copyright 2020 by Avid Technology, Inc. */
package test;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavlo.Fandych
 */
public final class DemoServer {

    public static void main(String[] args) throws IOException, InterruptedException, MalformedObjectNameException,
                                                  NotCompliantMBeanException, InstanceAlreadyExistsException,
                                                  MBeanRegistrationException {
        final int port = 9005;
        LocateRegistry.createRegistry(port);
        final Map<String, String[]> environment = new HashMap<>();
        environment.put(JMXConnector.CREDENTIALS, new String[]{"myrole", "1234567890"});
        final JMXConnectorServer server = JMXConnectorServerFactory
                .newJMXConnectorServer(new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:" + port + "/jmxrmi"), environment,
                        ManagementFactory.getPlatformMBeanServer());
        server.start();
        final SystemConfigMBean mBean = new SystemConfig(10);
        ManagementFactory.getPlatformMBeanServer().registerMBean(mBean, new ObjectName("test:type=SystemConfigMBean"));
        do {
            Thread.sleep(2000);
            System.out.println("Thread Count=" + mBean.getThreadCount());
        } while (mBean.getThreadCount() != 0);
        server.stop();
    }
}
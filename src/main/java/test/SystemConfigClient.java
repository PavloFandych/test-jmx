/* Copyright 2020 by Avid Technology, Inc. */
package test;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavlo.Fandych
 */
public final class SystemConfigClient {

    public static void main(String[] args) throws IOException, MalformedObjectNameException {
        final JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9005/jmxrmi");
        try (final JMXConnector jmxConnector = JMXConnectorFactory.connect(url, getEnvironment())) {
            final MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();
            final ObjectName name = new ObjectName("test:type=SystemConfigMBean");
            final SystemConfigMBean proxy = MBeanServerInvocationHandler
                    .newProxyInstance(connection, name, SystemConfigMBean.class, true);
            System.out.println("Current: " + proxy.doConfig());
            proxy.setThreadCount(5);
            System.out.println("New: " + proxy.doConfig());
            //should trigger the end loop condition
            proxy.setThreadCount(0);
        }
    }

    private static Map<String, String[]> getEnvironment() {
        final Map<String, String[]> environment = new HashMap<>();
        environment.put(JMXConnector.CREDENTIALS, new String[]{"myrole", "1234567890"});
        return environment;
    }
}
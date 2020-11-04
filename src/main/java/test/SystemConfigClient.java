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
public class SystemConfigClient {

    public static void main(String[] args) throws IOException, MalformedObjectNameException {
        final JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9005/jmxrmi");
        final Map<String, String[]> environment = new HashMap<>();
        environment.put(JMXConnector.CREDENTIALS, new String[]{"myrole", "1234567890"});
        try (final JMXConnector jmxConnector = JMXConnectorFactory.connect(url, environment)) {
            final SystemConfigMBean proxy = MBeanServerInvocationHandler
                    .newProxyInstance(jmxConnector.getMBeanServerConnection(),
                            new ObjectName("test:type=SystemConfigMBean"), SystemConfigMBean.class, true);
            System.out.println("Current: " + proxy.doConfig());
            proxy.setThreadCount(5);
            System.out.println("New: " + proxy.doConfig());

            //should trigger the end loop condition
            proxy.setThreadCount(0);
        }
    }
}
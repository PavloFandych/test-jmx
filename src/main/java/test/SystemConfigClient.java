/* Copyright 2020 by Avid Technology, Inc. */
package test;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Pavlo.Fandych
 */
public final class SystemConfigClient {

    public static void main(String[] args) throws IOException, MalformedObjectNameException {
        final JMXServiceURL url = new JMXServiceURL(generateUrl());
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

    private static String generateUrl() throws IOException {
        final Properties properties = new Properties();
        final InputStream resourceAsStream = SystemConfigClient.class.getClassLoader()
                .getResourceAsStream("client.properties");
        properties.load(resourceAsStream);
        return Boolean.parseBoolean(properties.getProperty("localhost.connect")) ?
                "service:jmx:rmi:///jndi/rmi://:9005/jmxrmi" :
                "service:jmx:rmi:///jndi/rmi://" + properties.getProperty("remote.connect.ip") + ":9005/jmxrmi";
    }
}
/* Copyright 2020 by Avid Technology, Inc. */
package test;

import javax.management.*;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Pavlo.Fandych
 */
public final class DemoServer {

    public static void main(String[] args) throws IOException, InterruptedException, NotCompliantMBeanException,
                                                  InstanceAlreadyExistsException, MalformedObjectNameException,
                                                  MBeanRegistrationException, URISyntaxException {
        final int port = 9005;
        LocateRegistry.createRegistry(port);
        final Map<String, String> environment = getEnvironment();
        final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        final JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:" + port + "/jmxrmi");
        final JMXConnectorServer server = JMXConnectorServerFactory.newJMXConnectorServer(url, environment, mBeanServer);
        server.start();
        injectMBean(mBeanServer);
        server.stop();
    }

    private static void injectMBean(final MBeanServer mBeanServer) throws InstanceAlreadyExistsException,
                                                                          MBeanRegistrationException,
                                                                          NotCompliantMBeanException,
                                                                          MalformedObjectNameException,
                                                                          InterruptedException {
        final SystemConfigMBean mBean = new SystemConfig(10);
        mBeanServer.registerMBean(mBean, new ObjectName("test:type=SystemConfigMBean"));
        int counter = 0;
        do {
            Thread.sleep(1000);
            System.out.println("Thread Count=" + mBean.getThreadCount() + " counter=" + counter);
            counter++;
        } while (mBean.getThreadCount() != 0);
    }

    private static Map<String, String> getEnvironment() throws URISyntaxException {
        final Map<String, String> environment = new HashMap<>();
        final URL passwordUrl = Objects.requireNonNull(DemoServer.class.getClassLoader().getResource("jmxremote.password"));
        final URL accessUrl = Objects.requireNonNull(DemoServer.class.getClassLoader().getResource("jmxremote.access"));
        environment.put("jmx.remote.x.password.file", Paths.get(passwordUrl.toURI()).toString());
        environment.put("jmx.remote.x.access.file", Paths.get(accessUrl.toURI()).toString());
        return environment;
    }
}
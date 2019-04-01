package com.cloudera.jmxlogger;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.management.*;
import javax.management.*;
import javax.management.remote.*;
import org.junit.jupiter.api.Test;

public class AppTest {
  private static final String ATTRIBUTE = "Hello world!";

  @Test
  public void testApp() throws Exception {
    String objects = "com.cloudera.jmxlogger:type=HelloWorld";
    String attributeName = "Text";
    String attribute = "Hello world!";

    MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    HelloWorldMBean mBean = new HelloWorld();
    ObjectName objectName = new ObjectName(objects);
    server.registerMBean(mBean, objectName);

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(stream);
    JmxLogger jmxLogger = new JmxLogger();
    jmxLogger.setObjects(objects);
    jmxLogger.setLocal(true);
    jmxLogger.setOut(out);
    jmxLogger.printOnce();
    String output = stream.toString();
    assertTrue(output.endsWith(null + "\t" + objects + "\t" + attributeName + "\t" + ATTRIBUTE + "\n"), output);
  }

  public static interface HelloWorldMBean {
    String getText();
  }

  public static class HelloWorld implements HelloWorldMBean {
    @Override
    public String getText() {
      return ATTRIBUTE;
    }
  }
}

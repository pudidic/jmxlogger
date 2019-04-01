package com.cloudera.jmxlogger;

import javax.management.*;
import javax.management.remote.*;
import java.lang.management.*;
import java.io.*;
import java.net.MalformedURLException;
import java.time.LocalDateTime;

public class JmxLogger {
  private String[] urls;
  private String[] objects;
  private long interval;
  private PrintStream out;
  private boolean isLocal;

  public JmxLogger() {
    out = System.out;
  }

  public boolean isLocal() {
    return isLocal;
  }

  public void setLocal(boolean isLocal) {
    this.isLocal = isLocal;
  }

  public PrintStream getOut() {
    return out;
  }

  public void setOut(PrintStream out) {
    this.out = out;
  }

  public String[] getUrls() {
    return urls;
  }

  public void setUrls(String... urls) {
    this.urls = urls;
  }

  public String[] getObjects() {
    return objects;
  }

  public void setObjects(String... objects) {
    this.objects = objects;
  }

  public long getInterval() {
    return interval;
  }

  public void setInterval(long interval) {
    this.interval = interval;
  }

  public void printLoop() throws Exception, InterruptedException {
    do {
      printOnce();
      Thread.sleep(interval);
    } while (!Thread.interrupted());
  }

  public void printOnce() throws Exception, InterruptedException {
    if (isLocal) {
      for (String object : objects) {
        printObject(null, object);
      }
    } else {
      for (String url : urls) {
        for (String object : objects) {
          printObject(url, object);
        }
      }
    }
  }

  private void printObject(String url, String object) throws Exception {
    MBeanServerConnection mbsc;
    JMXConnector connector;

    if (isLocal) {
      connector = null;
      mbsc = ManagementFactory.getPlatformMBeanServer();
    } else {
      JMXServiceURL serviceURL = new JMXServiceURL(url);
      connector = JMXConnectorFactory.connect(serviceURL);
      mbsc = connector.getMBeanServerConnection();
    }

    ObjectName objectName = new ObjectName(object);
    MBeanInfo mBeanInfo = mbsc.getMBeanInfo(objectName);
    for (MBeanAttributeInfo attributeInfo : mBeanInfo.getAttributes()) {
      String attributeName = attributeInfo.getName();
      Object attribute = mbsc.getAttribute(objectName, attributeName);
      out.printf("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL\t%2$s\t%3$s\t%4$s\t%5$s\n",
          LocalDateTime.now(), url, object, attributeName, attribute);
    }
    if (connector != null) {
      connector.close();
    }
  }
}

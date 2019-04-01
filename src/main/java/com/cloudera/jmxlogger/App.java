package com.cloudera.jmxlogger;

import org.apache.commons.cli.*;

public class App {
  private static final long DEFAULT_INTERVAL = 1000;

  public static void main(String[] args) throws Exception {
    Options options = new Options();
    options.addOption(Option.builder("u").longOpt("urls")
        .desc("JMX URLs.")
        .required().numberOfArgs(Option.UNLIMITED_VALUES).build());
    options.addOption(Option.builder("o").longOpt("objects")
        .desc("JMX object names.")
        .required().numberOfArgs(Option.UNLIMITED_VALUES).build());
    options.addOption(Option.builder("i").longOpt("interval").numberOfArgs(1)
        .desc("Interval in milliseconds. Default value is " + DEFAULT_INTERVAL + ".")
        .type(Long.class).build());
    options.addOption(Option.builder("l").longOpt("local").build());
    options.addOption(Option.builder("h").longOpt("help")
        .desc("Show this message.").build());

    if (args.length == 0 || "-h".equals(args[0]) || "--help".equals(args[0])) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("java -jar jmxlogger.jar", options, true);
      return;
    }

    CommandLineParser parser = new DefaultParser();
    CommandLine commandLine = parser.parse(options, args);
    String[] urls = commandLine.getOptionValues("u");
    String[] objects = commandLine.getOptionValues("o");
    boolean local = commandLine.hasOption("l");

    long interval = DEFAULT_INTERVAL;
    if (commandLine.hasOption("i")) {
      interval = Long.parseLong(commandLine.getOptionValue("i"));
    }

    JmxLogger jmxLogger = new JmxLogger();
    jmxLogger.setUrls(urls);
    jmxLogger.setObjects(objects);
    jmxLogger.setInterval(interval);
    jmxLogger.setLocal(local);
    jmxLogger.printLoop();
  }
}

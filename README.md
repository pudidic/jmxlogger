# jmxlogger
Type following commands to download, compile, and execute the jmxlogger.
```
git clone https://github.com/pudidic/jmxlogger.git
cd jmxlogger
mvn clean package
java -jar target/jmxlogger-1.0-SNAPSHOT.jar
```

It will show following message.
```
usage: java -jar jmxlogger.jar [-h] [-i <arg>] [-l] -o <arg> -u <arg>
 -h,--help             Show this message.
 -i,--interval <arg>   Interval in milliseconds. Default value is 1000.
 -l,--local
 -o,--objects <arg>    JMX object names.
 -u,--urls <arg>       JMX URLs.
```

You can pass interval, objects, urls arguments to log as following example.
```
java -jar jmxlogger.jar -u service:jmx:rmi://host1:1111 service:jmx:rmi://host2:2222 -o java.lang:type=Memory -i 100
```

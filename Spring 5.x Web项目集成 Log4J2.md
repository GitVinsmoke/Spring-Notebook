# Spring 5.x Web项目集成 Log4J2

在Spring 5.x 版本中已经抛弃了以前的 Log4J 日志配置监听器，我们如果还使用以前的配置方法的话，会报 ClassNotFoundException 异常。

#### 1. 添加maven依赖

需要三个 Log4J2 开发JAR包：log4j-core、log4j-api 和 log4j-web

```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.11.1</version>
</dependency>

<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.11.1</version>
</dependency>

<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-web</artifactId>
    <version>2.11.1</version>
</dependency>
```

#### 2. 配置web.xml

```xml
<!-- log4j 系统日志-->
<context-param>
    <param-name>log4jContextName</param-name>
    <param-value>star</param-value>
</context-param>
<context-param>
    <param-name>log4jConfiguration</param-name>
    <param-value>classpath:log4j2.xml</param-value>
</context-param>
```

#### 3. log4j.xml

Log4J2配置文件有所改变，使用 YXML或XML或JSON的配置文件方式，我们使用XML配置文件的方式：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="DEBUG">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Logger name="org.springframework.beans.factory" level="DEBUG"/>
        <Logger name="java.sql.Connection" level="DEBUG"/>
        <Logger name="java.sql.Statement" level="DEBUG"/>
        <Logger name="java.sql.PreparedStatement" level="DEBUG"/>
        <Logger name="java.sql.ResultSet" level="DEBUG"/>
        <Root level="DEBUG">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>

```

因为我在应用中使用的ORM框架是MyBatis，所以为了方便通过SQL语句的执行来调试BUG，我们将SQL语句也作为日志的输出点。
# classpath

因为在应用中加载文件时很多时候用到这个，所以今天想仔细了解一下 classpath 究竟是哪一个路径？

我探究这个秘密是通过在Web项目中使用 Spring 5.x 配合 Log4J2 查看日志观察 classpath路径的。

我在Web项目中的配置文件 web.xml 中配置如下：

```xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
  <display-name>Archetype Created Web Application</display-name>

  	<!-- 配置Spring -->
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<!-- 默认寻找WEB-INF下的资源，如果在类路径下需指定为classpath -->
		<param-value>classpath:spring/applicationContext-*.xml</param-value>
	</context-param>
	
	<!-- log4j 系统日志-->
    <context-param>
        <param-name>log4jContextName</param-name>
        <param-value>star</param-value>
    </context-param>
    <context-param>
        <param-name>log4jConfiguration</param-name>
        <param-value>classpath:log4j2.xml</param-value>
    </context-param>
	
	<!-- 配置过滤器，解决post的乱码问题 -->
	<filter>
		<filter-name>encoding</filter-name>
		<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>encoding</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

	<!-- 配置监听器加载spring -->
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>  
	
</web-app>

```

其中配置了Spring的配置文件（配置文件名使用模板pattern）以及Log4J2的配置文件，使用的路径都是classpath。至于如何在Web项目中配置Spring框架，以及如何在Spring5.x版本中集成Log4J2，我就不赘述，因为这一部分内容不是这篇文章的核心。

顺便呈上我的Log4J2的配置吧：

```language
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

## Web项目

我使用的是Maven工程，Spring的配置文件放在 src/main/resources/spring 目录下。

启动Web项目，截取出其中一部分的日志出来：

```language
Searching directory [D:\Java\apache-tomcat-8.5.30\wtpwebapps\SpringWeb\WEB-INF\classes\spring] for files matching pattern [D:/Java/apache-tomcat-8.5.30/wtpwebapps/SpringWeb/WEB-INF/classes/spring/applicationContext-*.xml]
```

从这一段日志可以看出，Web项目启动时要加载Spring的配置文件，而开始就要去寻找我们配置的模板，貌似它已经知道我们使用的是模板匹配，因为它使用到了pattern这个词。下一段日志：

```language
Resolved location pattern [classpath:spring/applicationContext-*.xml] to resources [file [D:\Java\apache-tomcat-8.5.30\wtpwebapps\SpringWeb\WEB-INF\classes\spring\applicationContext-dao.xml], file [D:\Java\apache-tomcat-8.5.30\wtpwebapps\SpringWeb\WEB-INF\classes\spring\applicationContext-service.xml], file [D:\Java\apache-tomcat-8.5.30\wtpwebapps\SpringWeb\WEB-INF\classes\spring\applicationContext-transaction.xml]]
```

从这一段日志中我们可以看到，应用容器已经找到了匹配的所有Spring配置文件，路径也很明显，在我们Tomcat的安装目录下的wtpwebapps中，找到发布的项目，在其WEB-INF下面的classes目录便能够找到。实际上，当我们去查看该目录时，我们发现在WEB-INF目录中，有classes目录，还有lib目录，还有我们的页面，classes目录存放了我们资源文件（即source folder，如src）中java、xml、properties等文件编译后的文件。

这样一来，classpath就很明了。那么有时候我们会使用到 classpath*，这又是什么呢？

因为有时候，可能在WEB-INF下可以会有多个classpath，用classpath*:需要遍历所有的classpath，所以加载速度是很慢的，因此，在规划的时候，应该尽可能规划好资源文件所在的路径，尽量避免使用 classpath*。

**如果我们不使用classpath，将默认加载WEB-INF目录下的文件。**
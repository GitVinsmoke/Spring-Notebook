# Spring使用JNDI数据源连接池配置

在使用Tomcat服务器+SpringFramework进行JavaEE项目的开发部署时，可以在Tomcat服务中通过JNDI（Java命名和目录接口）来获取数据源，优势是数据源完全独立于应用程序而管理，应用服务器中管的数据源通常以池的方式进行管理，也具备更好的性能。

## 配置Tomcat数据源

先在Web项目中的META-INF文件夹下创建一个context.xml，内容如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>

<context>
	<Resource 
		auth="Container"
		driverClassName="com.mysql.jdbc.driver"
		maxIdle="30"
		maxTotal="50" 
		maxWaitMillis="-1" 
		name="jdbc/spring"
		username="root"
		password="123456"
		type="javax.sql.DataSource" 
		url="jdbc:mysql://localhost:3306/learning?useSSL=true"/>
</context>
```

这里面就配置了数据源，其中有数据源的命名以及类型（javax.sql.DataSource）,还有数据库连接的配置信息。

配置完数据源之后我们需要让项目引用到这个数据源，在WEB-INF中的web.xml文件中加入数据源的引用信息。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" version="3.1">
  <display-name>SpringJNDI</display-name>
  <welcome-file-list>
    <welcome-file>index.html</welcome-file>
    <welcome-file>index.htm</welcome-file>
    <welcome-file>index.jsp</welcome-file>
    <welcome-file>default.html</welcome-file>
    <welcome-file>default.htm</welcome-file>
    <welcome-file>default.jsp</welcome-file>
  </welcome-file-list>
  
  <resource-ref>
    <description>DB Connection</description>
    <res-ref-name>jdbc/spring</res-ref-name>
    <res-type>javax.sql.DataSource</res-type>
    <res-auth>Container</res-auth>
  </resource-ref>
  
</web-app>
```

## Spring配置

我们还需要加入Spring的配置文件，里面仅配置数据源和JDBC模板来用以测试数据源。

```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:jee="http://www.springframework.org/schema/jee"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	 http://www.springframework.org/schema/beans/spring-beans.xsd
	 http://www.springframework.org/schema/aop
	 http://www.springframework.org/schema/aop/spring-aop.xsd
	 http://www.springframework.org/schema/context
	 http://www.springframework.org/schema/context/spring-context.xsd
	 http://www.springframework.org/schema/jee
	 http://www.springframework.org/schema/jee/spring-jee.xsd">
	 
	 <!-- 查找JNDI数据源 -->
	 <jee:jndi-lookup id="dataSource" jndi-name="jdbc/spring" resource-ref="true"></jee:jndi-lookup>
	 
	 <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
	 	<constructor-arg value="#{dataSource}"></constructor-arg>
	 </bean>
</beans>
```

Spring中，可以像使用Bean那样配置JNDI中的数据源的引用，装配到需求的类中。

位于jee命名空间的 &lt;jee:jndi-lookup&gt; 元素可用于检索JNDI中的任何对象（包括数据源）并将其用于Spring Bean中，将 resource-ref 属性设为 true，这样给定的jndi-name会自动添加 java:comp/env/ 前缀。

```xml
<jee:jndi-lookup id="dataSource" jndi-name="jdbc/MyTwitteDS" resource-ref="true"/>
```

## 测试

我们创建一个Servlet来测试我们的数据源配置是否成功，到时候，在首页添加一个超链接访问到该Servlet即可。

Servlet中通过Spring上下文获取 JDBC 模板类，因为我们在 JDBC模板类中注入了数据源，我们可以通过它来获取数据源，如果获取到了，那说明配置成功了。

```java
package com.li.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@WebServlet("/DataSourceServlet")
public class DataSourceServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    public DataSourceServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		JdbcTemplate jdbcTemplate=ctx.getBean("jdbcTemplate", JdbcTemplate.class);
		System.out.println(jdbcTemplate);
		System.out.println(jdbcTemplate.getDataSource());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

```

访问该Servlet的控制台输出结果：

```
org.springframework.jdbc.core.JdbcTemplate@68e0ea88
org.apache.tomcat.dbcp.dbcp2.BasicDataSource@3a415b1e
```

说明我们获取成功！
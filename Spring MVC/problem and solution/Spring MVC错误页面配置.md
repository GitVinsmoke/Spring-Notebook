# Spring MVC错误页面配置

springmvc跳转到自定义404页面的几种方法

## 一、Tomcat的错误页面配置方法

如，在 web.xml 文件中的 &lt;web-app&gt; 节点下配置：

```xml
<error-page>
     <error-code>404</error-code>
     <location>/WEB-INF/errors/404.jsp</location>
</error-page>
```

详见 JavaEE Repository 。

## 二、重写noHandlerFound方法，实现自定义的DispatcherServlet

web.xml 中配置前端控制器：

```xml
<servlet>
<servlet-name>springmvc</servlet-name>
<servlet-class>com.exceptionpage.Test</servlet-class>
<init-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>classpath:spring-mvc.xml</param-value>
</init-param>
</servlet>
<servlet-mapping>
<servlet-name>springmvc</servlet-name>
<url-pattern>/</url-pattern>
</servlet-mapping>
```

这里面我们使用了自己的控制器类。controller类需要继承DispatcherServlet，并重写noHandlerFound方法

```java
@Override
protected void noHandlerFound(HttpServletRequest request,
							  HttpServletResponse response) throws Exception {
	System.out.println("successful execute...");
	response.sendRedirect(request.getContextPath() + "/notFound");
}

@RequestMapping("/notFound")
public String test2(){
	  System.out.println("successful...");
	  return "404";
}
```

若没有匹配上url，则调用noHandlerFound方法，并且重定向到一个新的处理器。

## 三、通过 Tomcat 与 Controller

web.xml 中

```xml
<error-page>
		<error-code>400</error-code>
		<location>/error/400</location>
	</error-page>
	<error-page>
		<error-code>404</error-code>
		<location>/error/404</location>
	</error-page>
	<error-page>
		<error-code>500</error-code>
		<location>/error/500</location>
</error-page>

```

ErrorController类：

```java
package com.mvc.pab.internet.controller;
 
 
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
 
@Controller
@RequestMapping("error")
public class ErrorController {
	private static final String BASE_DIR = "error/";
	@RequestMapping("400")
	public String handle1(HttpServletRequest request){
		return BASE_DIR + "400";
	}
	@RequestMapping("404")
	public String handle2(HttpServletRequest request){
		return BASE_DIR + "404";
	}
	@RequestMapping("500")
	public String handle3(HttpServletRequest request){
		return BASE_DIR + "500";
	}
}

```

方法返回的是视图的字符路径。

项目当中有设置全局拦截器的，一定要在这里添加拦截器配置，不然会被拦截，配置方法是往自己的配置中节点代码 &lt;mvc:exclude-mapping path="/error/*" /&gt;，其中 /error/* 为我的错误页的视图层，意为都不拦截 `/error/` 。

```xml
<!--配置拦截器, 多个拦截器,顺序执行 -->
<mvc:interceptors>
	<mvc:interceptor>
		<!-- 匹配的是url路径， 如果不配置或/**,将拦截所有的Controller -->
		<mvc:mapping path="/**" />
		<mvc:exclude-mapping path="/error/*" />
		<mvc:exclude-mapping path="/test/*" />
		<mvc:exclude-mapping path="/manager/*" />
		<mvc:exclude-mapping path="/wxmanager/*" />
		<bean class="com.mvc.filter.CommonInterceptor"></bean>
	</mvc:interceptor>
	<mvc:interceptor>
		<!-- 匹配的是url路径， 如果不配置或/**,将拦截所有的Controller -->
		<mvc:mapping path="/manager/*" />
		<mvc:mapping path="/wxmanage/*" />
		<mvc:exclude-mapping path="/manager/login" />
		<mvc:exclude-mapping path="/manager/doLogin" />
		<bean class="com.mvc.filter.ManagerInterceptor"></bean>
	</mvc:interceptor>
</mvc:interceptors>
```


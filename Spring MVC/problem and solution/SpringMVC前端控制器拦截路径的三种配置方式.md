# SpringMVC前端控制器拦截路径的三种配置方式

配置springmvc的前端控制器需要在web.xml里面配置拦截的路径名称。

```xml
<!-- 配置前端控制器 -->
<servlet>
	<servlet-name>springmvc</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<!-- 加载SpringMVC的核心配置文件，DispatcherServlet类中有contextConfigLocation属性，不能变，默认加载WEB-INF路径下的文件 -->
	<init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:spring/springmvc.xml</param-value>
	</init-param>
	<!-- 配置SpringMVC什么时候启动，参数必须为整数 -->
	<!-- 如果是0或者大于0，则SpringMVC随容器启动而启动 -->
	<!-- 如果小于0，则在第一次请求进来时启动 -->
	<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>springmvc</servlet-name>
	<!-- 所有的请求都进入SpringMVC，/ 会拦截所有请求（包括静态资源）排除JSP页面，/* 会拦截所有请求包括JSP，则JSP不能显示 -->
	<url-pattern>/</url-pattern>
</servlet-mapping>
```

在这上面，我使用的是 `/` 这种拦截方式，还有两种方式，分别是：`.action` 和 `/*`。

## .action

```xml
<url-pattern>*.action</url-pattern>
```

代表是拦截后缀名字为 .action 结尾的url

## /

```xml
<url-pattern>/</url-pattern>
```

代表拦截所有的 url 但是不包括 `.jsp` 后缀的的url，这种是开发中常用的。

## /*

```xml
<url-pattern>/*</url-pattern>
```

代表拦截所有的 url 同样也包括 `.jsp` 为后缀的url。

## 总结

在我们项目开发中 一般会要求遵守restful .action 肯定是不可以的，因为restful不让有.action 一般来说 `.jsp` 页面 我们也写在 WEB-INF 目录下，不让人直接访问，也无需配置成 `/*`，所以一般是使用 `/` 来拦截。

## 补充

springMVC 中的拦截器配置方法与web.xml 里面的不一样，我们需要使用 `/**` 来代表拦截所有的请求：

```xml
<mvc:interceptors>
	<mvc:interceptor>
		<mvc:mapping path="/**"/>
		<bean class="com.guxiang.interceptor.Interceptor1"></bean>
	</mvc:interceptor>
</mvc:interceptors>
```
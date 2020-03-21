# SpringMVC访问静态页面（资源文件）

## 1. 激活服务器的默认servlet处理静态资源

```xml
<servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>*.html</url-pattern>
</servlet-mapping>
```

上面的配置含义表示html页面由服务器的缺省Servlet进行处理，同时注意上面的配置项要写在DispatcherServlet前面。

如果你的DispatcherServlet拦截"/"，拦截了所有的请求，那么同时对.js,.jpg等静态文件的访问也就被拦截了,从controller返回的html页面也被拦截了，因此浏览器得不到页面。

## 2. mvc:resources

```language
<mvc:resources location="/static/**" mapping="/static/" />
```

在SpringMVC的配置文件中使用以上配置。

`/static/**` 为映射路径，location指定静态资源的位置.使用 `mvc:resources/` 该元素,把映射路径注册到SimpleUrlHandlerMapping的urlMap中,key为注册的映射路径,而value为ResourceHttpRequestHandler，这样就巧妙的把对静态资源的访问由HandlerMapping转到ResourceHttpRequestHandler处理并返回。

这样，把这些资源文件告诉给SpringMVC，就不会被拦截。

## 3. mvc:default-servlet-handler

```language
<mvc:default-servlet-handler/>
```

在SpringMVC的配置文件中添加上面的配置即可，这样会在Spring MVC上下文中定义一个org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler，它会像一个检查员，对进入DispatcherServlet的URL进行筛查，如果发现是静态资源的请求，就将该请求转由Web应用服务器默认的Servlet处理，如果不是静态资源的请求，才由DispatcherServlet继续处理。

&lt;mvc:default-servlet-handler&gt;和&lt;mvc:resource&gt;的区别：&lt;mvc:default-servlet-handler&gt;是将静态资源的处理转给服务器默认的servlet进行处理，&lt;mvc:resource&gt;是由spring自己处理静态资源，这样spring可以添加一些额外的功能，如缓存等。
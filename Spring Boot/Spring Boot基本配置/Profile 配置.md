# Profile 配置

Profile 是 Spring 用来针对不同的环境对不同的配置提供支持，全局 Profile 配置使用 `application-{profile}.properties` （如 `application-prod.properties`）。

通过在 `application.properties` 中设置 `spring.profiles.active=prod` 来指定活动的 Profile。

下面做一个简单的演示，如我们分为生产（prod）和开发（dev）环境，生产环境下端口为 9099，开发环境下端口为 9091。

在 resources 文件下，有三个 spring 配置文件，它们的文件结构如下：

```
src/main/resources
	- application.properties
	- application-dev.properties
	- application-prod.properties
```

它们的内容如下：

- `application.properties`：

    ```properties
    # activate the profile
    spring.profiles.active=prod
    ```

- `application-dev.properties`：

    ```properties
    # modify Tomcat started port and context path
    server.port=9091
    server.servlet.context-path=/spring
    
    ```

- `application-prod.properties`：

    ```properties
    # modify Tomcat started port and context path
    server.port=9099
    server.servlet.context-path=/spring
    ```

    

先让 `spring.profiles.active=dev`，启动：

```
o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 9091 (http) with context path '/spring'
```

再让 `spring.profiles.active=prod`，启动：

```
o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 9099 (http) with context path '/spring'
```


# SpringBoot 2.X 上传文件无法获取到之CommonsMultipartResolver与StandardServletMultipartResolver

## 问题导入

这是在开发项目时用SpringBoot实现上传文件时MultipartFile遇到的空指针问题。

首选给出 form 表单：

```html
<form action="/upload" enctype="multipart/form-data" method="post" >
        <input type="file" name="file"><br>
        <input type="submit" value="提交">
</form>
```

而后端是用SpringBoot来实现接收文件的：

```java
@PostMapping("/file/upload")
public AjaxResponse uploadFile(@RequestParam(value = "file") final MultipartFile file){
    // omitted
}
```

问题来了，在调试的时候，后端的参数 file 总是未null，后来查了一会儿才发现是因为有人在项目@configuration里定义了MultipartResolverbean的缘故，如下：

```java
@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1000000);
        return multipartResolver;
    }

}
```

加的这个配置为什么不可以呢？

## CommonsMultipartResolver VS StandardServletMultipartResolver

SpringBoot 2 默认使用 StandardServletMultipartResolver 来解析含有文件的请求。这一点，我们可以找到这个 org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration 类：

```java
public class MultipartAutoConfiguration {
    private final MultipartProperties multipartProperties;

    public MultipartAutoConfiguration(MultipartProperties multipartProperties) {
        this.multipartProperties = multipartProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public MultipartConfigElement multipartConfigElement() {
        return this.multipartProperties.createMultipartConfig();
    }

    @Bean(
        name = {"multipartResolver"}
    )
    @ConditionalOnMissingBean({MultipartResolver.class})
    public StandardServletMultipartResolver multipartResolver() {
        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
        multipartResolver.setResolveLazily(this.multipartProperties.isResolveLazily());
        return multipartResolver;
    }
}
```

这个配置类里面就指定了一个 StandardServletMultipartResolver Bean 来解析含文件的请求。

我们可以还 debug 一下，在处理方法中添加 HttpServletRequest 参数：

```java
@PostMapping("/file/upload")
public AjaxResponse uploadFile(HttpServletRequest request, @RequestParam(value = "file") final MultipartFile file){
    // omitted
}
```

然后我们就可以发现，这个含有文件的请求 request 实际上是 StandardMultipartHttpServletRequest 类的实例。

而 CommonsMultipartResolver 使用commons Fileupload来处理multipart请求，所以在使用时，必须要引入相应的jar包，StandardServletMultipartResolver 是基于 Servlet3.0 来处理 multipart 请求的，所以不需要引用其他 JAR 包，但是必须使用支持 Servlet3.0 的容器才可以。以tomcat为例，从Tomcat 7.0.x的版本开始就支持Servlet3.0了。

从 request 中获取解析后的文件，两个 Resolver 都能判断 request 是否包含文件请求，转换为 MultipartHttpServletRequest，都可以获取文件。

因此，springboot 已经使用 StandardServletMultipartResolver 解析 request，我们再 CommonsMultipartResolver 做一次解析，就会取不到文件，应该是直接获取解析文件就行。

## 解决方法

加入配置类，我们还是可以使用 CommonsMultipartResolver 来作为请求解析器：

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


@Configuration
public class UploadConfig {
    // 显示声明CommonsMultipartResolver为mutipartResolver
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        // resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setResolveLazily(true);
        resolver.setMaxInMemorySize(40960);
        // 上传文件大小 5M 5*1024*1024
        resolver.setMaxUploadSize(5 * 1024 * 1024);
        return resolver;
    }

}
```

然后在springboot启动类上加入注解

```java
// 排除原有的Multipart配置
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

或者说，我们可以不用 CommonsMultipartResolver，什么都不用做，就直接用 springboot 的默认配置 StandardServletMultipartResolver 也可以。

## 注意

1. 如果要解析 request，必须禁止这两个解析器，然后自己从代码中手动解析。

2. 在配置文件中做文件上传相关配置，只针对 StandardServletMultipartResolver 生效，如果手动配置了 CommonsMultipartResolver，那么只能自己在代码中设置它的相关属性。

## 配置方式

在启动器中

```java
/**
 * 文件上传配置
 *
 * @return configuration Bean
 */
@Bean
public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    // 文件大小上限
    factory.setMaxFileSize("100MB");
    // 设置总上传数据总大小
    factory.setMaxRequestSize("10240MB");
    return factory.createMultipartConfig();
}
```
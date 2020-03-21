# 静态工具类中注入Service的解决方案

关于Spring/SpringBoot在静态工具类中注入Service的解决方案。

## 问题

最近遇到了需要在项目的工具类中注入Service，由于工具类中方法一般都是静态的，所以要求该属性也要是静态的（Service）才能使用。但是由于Spring/SpringBoot正常情况下不能支持注入静态属性（会报空指针异常）。

**主要原因在于：Spring的依赖注入实际上是依赖于Set方法进行注入值的，Spring是基于对象层面的依赖注入，而静态属性/静态变量实际上是属于类的。**

## 解决方案一

1. 给当前的工具类加上@Component，使其成为一个被Spring管理的bean对象

2. 声明一个静态的属性（加上注解@Autowired）和一个非静态的属性。

3. 声明一个返回值为void并且不能抛出异常的方法，在其中将非静态属性赋值给静态属性。该方法上加上注解 `@PostConstruct`，这样就将service的值注入了进来。

示例代码如下：

```java
@Component
public class MessageUtil {
	@Autowired
	private TestService service;

	private static TestService testService;

	@PostConstruct
	public void initStaticService() {
		testService = service;
	}
}
```

其中 `@PostConstruct` 是 JavaEE 5 规范之后，Servlet新增的两个影响servlet声明周期的注解之一，另外一个是 `@PreConstruct`。这两个都可以用来修饰一个非静态的返回值为void的方法，并且该方法不能抛出异常。

被 `@PostConstruct` 注解修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次，类似于Servlet中的init()方法。被该注解修饰的方法会在构造器执行之后，init()方法执行之前执行。Spring中允许开发者在受理的Bean中去使用它，当IOC容器被实例化管理当前bean时，被该注解修饰的方法会执行，完成一些初始化的工作。

被 `@PreConstruct` 注解修饰的方法会在服务器卸载Servlet的时候运行，类似于Servlet中的destroy方法。被该注解修饰的方法会在destroy方法执行之后，Servlet彻底卸载之前执行。

## 解决方案二

可以使用Set方法实现设值注入，注意Set方法需要是非静态的，这时就不需要加@Autowired注解了。

## 更多

在SpringMVC中也是一样，在 `@Controller` 标注的控制器中如果有静态方法要使用非静态属性（一般也是注入进行的Service），也可以使用上述相同的方法
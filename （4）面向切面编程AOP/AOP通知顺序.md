# 注解AOP以及通知顺序

如果我们有多个通知想要在同一连接点执行，那执行顺序如何确定呢？Spring AOP使用AspectJ的优先级规则来确定通知执行顺序。总共有两种情况：同一切面中通知执行顺序、不同切面中的通知执行顺序。

## 同一切面中通知执行顺序

而如果在同一切面中定义两个相同类型通知（如同是前置通知或环绕通知（proceed之前））并在同一连接点执行时，其执行顺序是未知的，如果确实需要指定执行顺序需要将通知重构到两个切面，然后定义切面的执行顺序。

## 不同切面中的通知执行顺序

当定义在不同切面的相同类型的通知需要在同一个连接点执行，如果没指定切面的执行顺序，这两个通知的执行顺序将是未知的。

如果需要他们顺序执行，可以通过指定切面的优先级来控制通知的执行顺序。

Spring中可以通过在切面实现类上实现org.springframework.core.Ordered接口或使用Order注解来指定切面优先级。在多个切面中，Ordered.getValue()方法返回值（或者注解值）较小值的那个切面拥有较高优先级，如图6-7所示。

1. 使用注解：

	```java
    @Order(value=1)
	```
    在切面中使用@Order注解可以定义通知的顺序，value接受一个int类型的值，值越小，则优先级越高

2. 使用XML配置

    ```xml
    <aop:config>
        <aop:aspect order="1"></aop:aspect>
     </aop:config>
    ```

我们不推荐使用实现Ordered接口方法，所以没介绍。

## 示例：

银行接口：

```java
package com.spring.advice;

public interface Bank {
	
	/*取款*/
	String drawMoney();
	
	/*存款*/
	void depositMoney(int money);
	
}

```

其实现类：ATM机

```java
package com.spring.advice;

import org.springframework.stereotype.Component;

@Component
public class ATM implements Bank {

	@Override
	public String drawMoney() {
		System.out.println("用户操作：已经取出100元");
		return "取出100元";
	}

	@Override
	public void depositMoney(int money) {
		if(money<100) {
			throw new IllegalArgumentException();
		}
		System.out.println("用户操作：存入" + money + "元");
	}

}

```

定义的切面：在操作前后需要进行的处理：

```java
package com.spring.advice;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*切面检测*/
@Component
@Aspect
@Order(value=1)
public class Detection {
	
	@Pointcut(value="execution(* com.spring.advice.Bank.*(..))")
	public void pointcut() {}
	
	@Pointcut(value="execution(* com.spring.advice.Bank.drawMoney(..))")
	public void drawpointcut() {}
	
	@Pointcut(value="execution(* com.spring.advice.Bank.depositMoney(..))")
	public void depositpointcut() {}
	
	/* 事先的安全检测，对所有操作都需要检测 */
	@Before("pointcut()")
	public void detectSecurity() {
		System.out.println("周围环境完全！");
	}
	
	/*操作提示*/
	@Before("pointcut()")
	public void inputPoint() {
		System.out.println("操作提示...");
	}
	
	
	@AfterReturning("depositpointcut()")
	public void depositSucess() {
		System.out.println("您已存款成功，请取出银行卡");
	}
	
	@AfterReturning("drawpointcut()")
	public void drawSuccess() {
		System.out.println("您已经取款成功，请拿好现金并取出银行卡");
	}
	
	@AfterThrowing("pointcut()")
	public void transactionFailed() {
		System.out.println("交易失败！");
	}
	
    @Around(value="pointcut()")
	public void around(ProceedingJoinPoint joinPoint) {
		System.out.println("环绕通知前");
		try {
			/*连接点的执行（Spring中为方法）*/
			joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("环绕通知后");
	}
}

```

另一个切面：开门关门操作，切面的优先级更高

```java
package com.spring.advice;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(value=0)
public class Door {

	@Pointcut(value="execution(* com.spring.advice.Bank.*(..))")
	public void pointcut() {}
	
	@Before("pointcut()")
	public void openDoor() {
		System.out.println("开门");
	}
	
	@After("pointcut()")
	public void closeDoor() {
		System.out.println("关门");
	}
}

```

配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!-- 引用Spring的多个Schema空间的格式定义文件 -->

<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	 http://www.springframework.org/schema/beans/spring-beans.xsd
	 http://www.springframework.org/schema/aop
	 http://www.springframework.org/schema/aop/spring-aop.xsd
	 http://www.springframework.org/schema/context
	 http://www.springframework.org/schema/context/spring-context.xsd">
	 
	 <!-- 开启注解AOP，自动代理 -->
	 <aop:aspectj-autoproxy></aop:aspectj-autoproxy>
	 
	 <!-- 开启自动扫描 -->
	 <context:component-scan base-package="com.spring.advice"></context:component-scan>
	 
</beans>
```

测试：

```java
public static void main(String[] args) {

    ApplicationContext ctx=new ClassPathXmlApplicationContext("com"+File.separator+"spring"+File.separator+"advice"+File.separator+"applicationContext.xml");
    /*不能将入参改为ATM.class，否则通知不起效，因为我们通知的是Bank中的方法*/
    Bank bank=ctx.getBean("ATM", Bank.class);
    bank.depositMoney(200);

}
```

**注意：在最后进行测试的时候有一点需要注意，就是在获取Bean的时候，class类型不能改为ATM.class，因为ATM.class虽然不会报错，但是并不会执行通知，因为我们是面向接口编程，通知的是接口中的方法，所以需要Bank接口的class类型。**
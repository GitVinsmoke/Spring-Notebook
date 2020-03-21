# Spring Boot计划任务

从 Spring 3.1 开始，计划任务在 Spring 中的实现变得异常简单。首先通过在配置类中注解 `@EnableScheduling` 来开启对计划任务的支持，然后在执行计划任务的方法上注解 `@Scheduled`，声明这是一个计划任务。

Spring 通过 `@Scheduled` 支持多种类型的计划任务，包含 corn、fixDelay、fixRate 等。

## 示例

（1）计划任务执行类

```java
package com.cnsuning.snds.portal.xavier.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ScheduleTaskService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("每隔五秒执行一次 " + DATE_FORMAT.format(new Date()));
    }

    @Scheduled(cron = "0 28 17 ? * *")
    public void fixTimeExecution() {
        System.out.println("在指定时间 " + DATE_FORMAT.format(new Date()) + "执行");
    }
}

```

代码解释：

- 通过 `@Scheduled` 声明该方法是计划任务，使用 fixRate 属性每隔固定时间执行。
- 使用 cron 属性可按照指定时间执行，本例指的是每天 17 点 28 分执行，cron 是 UNIX 和类 UNIX（Linux）系统下的定时任务。

（2）配置类

```java
package com.cnsuning.snds.portal.xavier.schedule;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("com.cnsuning.snds.portal.xavier")
@EnableScheduling
public class TaskSchedulerConfig{

}
```

代码解释：

- 在配置类中配置包扫描
- 使用注解 `@EnableScheduling` 来开启对计划任务的支持

（3）运行

```java
package com.cnsuning.snds.portal.xavier.schedule;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Xavier
 * 2019/10/27
 */
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TaskSchedulerConfig.class);

        // 不能调用 close 方法，否则任务也会停止
        // context.close();
    }

}
```

结果如下：

```
每隔五秒执行一次 17:27:43
每隔五秒执行一次 17:27:48
每隔五秒执行一次 17:27:53
每隔五秒执行一次 17:27:58
在指定时间 17:28:00执行
每隔五秒执行一次 17:28:03
每隔五秒执行一次 17:28:08
每隔五秒执行一次 17:28:13
每隔五秒执行一次 17:28:18
```

## No qualifying bean of type [org.springframework.scheduling.TaskScheduler] is defined

在运行 Spring 定时任务的时候，下面是报出来的两个异常：

```
2019-10-27 17:27:43.238 DEBUG   --- [           main] s.a.ScheduledAnnotationBeanPostProcessor : Could not find default TaskScheduler bean

org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'org.springframework.scheduling.TaskScheduler' available
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveNamedBean(DefaultListableBeanFactory.java:992)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.resolveSchedulerBean(ScheduledAnnotationBeanPostProcessor.java:293)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.finishRegistration(ScheduledAnnotationBeanPostProcessor.java:234)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.onApplicationEvent(ScheduledAnnotationBeanPostProcessor.java:211)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.onApplicationEvent(ScheduledAnnotationBeanPostProcessor.java:102)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.doInvokeListener(SimpleApplicationEventMulticaster.java:172)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.invokeListener(SimpleApplicationEventMulticaster.java:165)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:139)
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:400)
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:354)
	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:888)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:553)
	at org.springframework.context.annotation.AnnotationConfigApplicationContext.<init>(AnnotationConfigApplicationContext.java:88)
	at com.cnsuning.snds.portal.xavier.schedule.Main.main(Main.java:14)

2019-10-27 17:27:43.238 DEBUG   --- [           main] s.a.ScheduledAnnotationBeanPostProcessor : Could not find default ScheduledExecutorService bean

org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'java.util.concurrent.ScheduledExecutorService' available
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveNamedBean(DefaultListableBeanFactory.java:992)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.resolveSchedulerBean(ScheduledAnnotationBeanPostProcessor.java:293)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.finishRegistration(ScheduledAnnotationBeanPostProcessor.java:255)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.onApplicationEvent(ScheduledAnnotationBeanPostProcessor.java:211)
	at org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.onApplicationEvent(ScheduledAnnotationBeanPostProcessor.java:102)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.doInvokeListener(SimpleApplicationEventMulticaster.java:172)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.invokeListener(SimpleApplicationEventMulticaster.java:165)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:139)
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:400)
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:354)
	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:888)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:553)
	at org.springframework.context.annotation.AnnotationConfigApplicationContext.<init>(AnnotationConfigApplicationContext.java:88)
	at com.cnsuning.snds.portal.xavier.schedule.Main.main(Main.java:14)
```

如果你足够仔细的话，你会发现这两个异常的级别，不是ERROR 也不是 WARNING，而是DEBUG。

原来，Spring的定时任务调度器会尝试获取一个注册过的 task scheduler来做任务调度，它会尝试通过BeanFactory.getBean的方法来获取一个注册过的scheduler bean，获取的步骤如下：

1.尝试从配置中找到一个TaskScheduler Bean

2.寻找ScheduledExecutorService Bean

3.使用默认的scheduler

前两步，如果找不到的话，就会以debug的方式抛出异常，分别是：

```java
logger.debug("Could not find default TaskScheduler bean", ex);
logger.debug("Could not find default ScheduledExecutorService bean", ex);
```

所以，日志中打印出来的两个异常，根本不是什么错误信息，也不会影响定时器的使用，只不过是spring的自己打印的一些信息罢了，不过没搞明白，为什么非要用异常的方式打出来，估计是为了看这清晰点吧。也或者，这里面有一些重要的信息需要提示开发者。具体是什么原因，只能有机会进一步再去了解了。


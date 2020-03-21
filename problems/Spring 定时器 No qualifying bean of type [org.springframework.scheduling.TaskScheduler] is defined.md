# Spring 定时器 No qualifying bean of type [org.springframework.scheduling.TaskScheduler] is defined

最近项目里面，用了spring的定时任务，启动项目测试的时候，发现，启动的Log中居然有一个异常，但是项目并没有因此而停止，反而后续的定时任务仍然是好好的在运行。

下面是报出来的两个异常：

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


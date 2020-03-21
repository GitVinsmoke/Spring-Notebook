# Spring Boot多线程

Spring 通过任务执行器（TaskExecutor）来实现多线程和并发线程，使用 ThreadPoolTaskExecutor 可实现一个基于线程池的 TaskExecutor。而实际开发中任务一般是非阻碍的，即异步的，所以我们要在配置类中通过 `@EnableAsync` 开启对异步任务的支持，并通过在实际执行的 Bean 的方法中使用 `@Async` 注解来声明其是一个异步任务。

## 示例

（1）配置类

```java
package com.cnsuning.snds.portal.xavier.thread;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@ComponentScan("com.cnsuning.snds.portal.xavier")
@EnableAsync
public class TaskExecutorConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(25);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}

```

代码解释：

- 利用 `@EnableAsync` 注解开启异步任务支持
- 配置类实现 AsyncConfigurer 接口并重写 getAsyncExecutor 方法，并返回一个 ThreadPoolTaskExecutor，这样我们就获得一个基于线程池的 TaskExecutor。

（2）任务执行类

```java
package com.cnsuning.snds.portal.xavier.thread;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    @Async
    public void executeAsyncTask(Integer i) {
        System.out.println("执行异步任务：" + i);
    }

    @Async
    public void executeAsyncTaskPlus(Integer i) {
        System.out.println("执行异步任务 + 1：" + (i + 1));
    }
}

```

代码解释：

- 通过 `@Async` 注解表明该方法是个异步方法，如果注解在类级别，则表明该类所有的方法都是异步方法，而这里的方法自动被注入使用 ThreadPoolTaskExecutor 作为 TaskExecutor。

（3）运行

```java
package com.cnsuning.snds.portal.xavier.thread;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TaskExecutorConfig.class);
        AsyncService asyncService = context.getBean(AsyncService.class);
        for (int i = 0; i < 10; i++) {
            asyncService.executeAsyncTask(i);
            asyncService.executeAsyncTaskPlus(i);
        }
        context.close();
    }
}
```

运行结果：

```
执行异步任务：1
执行异步任务 + 1：1
执行异步任务：2
执行异步任务 + 1：2
执行异步任务：0
执行异步任务：4
执行异步任务 + 1：4
执行异步任务：3
执行异步任务 + 1：3
执行异步任务：6
执行异步任务 + 1：6
执行异步任务：5
执行异步任务 + 1：5
执行异步任务：8
执行异步任务 + 1：8
执行异步任务：7
执行异步任务 + 1：7
执行异步任务 + 1：10
执行异步任务：9
执行异步任务 + 1：9
```

从结果可以看到，任务是并发执行而不是顺序进行。
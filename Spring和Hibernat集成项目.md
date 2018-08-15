# Spring和Hibernat集成项目

1. 引入JAR包

2. web.xml配置文件

3. Spring应用上下文配置文件

4. 控制层配置

5. 整合项目架构

## web.xml配置文件

web.xml配置文件是所有web应用最基本的配置文件，Spring框架Bean工厂的创建启动、控制层框架的加载都在此配置。

## Spring应用上下文配置文件

在SSH或SSM整合应用中，Spring应用上下文配置文件既是Spring框架的IoC容器、AOP代理等的配置依，又是与Hibernate等ORM工具集成管理的核心配置（包括DataSource、SessionFactory、TransactionManager等核心bean都在此注册），还是管理控制层组件的所在，是名副其实的“bean工厂”配置。

## 整合项目架构

- 表现层：JSP页面

- 业务逻辑层：

	- domai包：实体
	- action或者controller包：业务控制器
	- service包：服务类以及服务对象（service & service.impl）

- 数据访问层：

	- dao包（dao & dao.impl）

架构原则：

- 通过接口耦合

- 事务管理在业务逻辑层实施。
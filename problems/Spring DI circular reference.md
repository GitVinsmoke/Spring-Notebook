# Spring DI circular reference

循环依赖，就是说类A依赖类B，而B又依赖于A，Spring启动的时候如果出现循环依赖的情况是会报错的，解决方案：

```java
@Lazy
@Autowired
private SersPersonService sersPersonService;
```


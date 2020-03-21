# 浅析 @PathVariable 和 @RequestParam

## 1. 代码实例

首先，上两个地址：

地址1：http://localhost:8989/SSSP/emps?pageNo=2

地址2：http://localhost:8989/SSSP/emp/7

如果想获取URL 1中的 pageNo 的值“2” ，则使用  @RequestParam；如果想获取URL 2中的 emp/7 中的“7 ”，则使用 @PathVariable。

实例如下：

```java
@RequestMapping(value="/getTaobao")
public List<Taobao> doAddTaobao(@RequestParam(required = false) Integer status){
    return taobaoService.getTaobao(status);
}

@RequestMapping(value = "/auditTaobao/{id}")
public void doAuditTaobao(@PathVariable Integer id){
    taobaoService.auditTaobao(id);
}
```

很明显，当看到`?`这种使用URL传参的方式，如果要获取这里面的参数，就应该使用@RequestParam，如果要获取路径中的参数，就应该使用@PathVariable。

## 2. 讲解

### 2.1. RequestParam

RequestParam：从字面意思上来理解，就是**请求参数**，顾名思义就是获取参数的。 

RequestParam是从请求里面获取参数，从请求来看： 

```
　/springmvc/user/page?name=Lisa&age=2
```

name和age属于请求参数而不是路径变量，所以应该添加`@RequestParam`的注解。

使用场景：

- 常用来处理简单类型的绑定，通过`HttpServletRequest.getParameter()`获取的String可直接转换为简单类型的情况。
- 用来处理Content-Type为 `application/x-www-form-urlencoded`编码的内容，提交方式GET、POST。
- 该注解有三个属性：value、required和defaultValue。value用来指定要传入值的id名称，required用来指示参数是否必须绑定，defaultValue用来指明当为传入该参数时的默认值。

示例：

```java
@RequestMapping(value = "/roles", method = RequestMethod.GET)
public JsonResponse listRoles(
    @RequestParam(value = "platform") final String platform,
    @RequestParam(value = "roleCode", required = false) final String roleCode,
    @RequestParam(value = "roleName", required = false) final String roleName,
    @RequestParam(value = "index", defaultValue = "1") final int index,
    @RequestParam(value = "size", defaultValue = "10") final int size
)
```



### 2.2. PathVariable

PathVariable：从字面意思上来理解，就是**路径变量**，顾名思义，就是要获取一个URL地址中的一部分值，究竟是哪一部分呢？RequestMapping上就说明了，如`@RequestMapping(value="/emp/{id}"）`，这个请求地址就把`id`这部分作为了一个路径变量，我们可以用`@PathVariable Integer id`这个使用注解绑定的参数来获取URL地址变量`{id}`的值。


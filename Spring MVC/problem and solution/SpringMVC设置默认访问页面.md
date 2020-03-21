# SpringMVC设置默认访问页面

一般地，在Web开发中，可以在 web.xml 配置文件中配置一下默认访问的页面，也就是网站首页：

```xml
<welcome-file-list>
<welcome-file>/index.html</welcome-file>
</welcome-file-list>
```

这是Tomcat容器默认的首页，但是这也是有一个问题的，那就是 welcome-file-list 一般情况下只能使用静态网页，如果非要把他配置成SpringMVC的控制器URL就会报错。

可以使用以下方法，实质就是为首页设置一个处理器适配器：

```java
/**
 * created by Vintage
 * 主页控制器
 */

@Controller
public class IndexControllor {

	@RequestMapping(value = {"/", "index"})
    public ModelAndView defaultIndex() {
		ModelAndView mav = new ModelAndView();
		...
		mav.setViewName("index.jsp");
		return mav;
	}

}
```

上面的方法中配置了两个 URL 路径，也就是说，使用下面两种请求都可以调用到该适配器：

```language
http://localhost:8080/yikexin/

http://localhost:8080/yikexin/index
```


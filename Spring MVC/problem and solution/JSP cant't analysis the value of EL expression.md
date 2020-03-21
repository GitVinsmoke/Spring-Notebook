# JSP页面获取不到EL表达式的值

今天在使用SpringMVC的时候，JSP页面获取不到一个简单的EL表达式的值，这就很不应该了，以前使用简单Servlet和JSP的时候能够获取，现在出现了问题。

随后我查阅资料，发现在JSP page指令中有这么一个属性：

```language
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
```

isELIgnored="false" 这一属性设置了是否忽略EL表达式，设置为 true 就忽略，false 则启用EL表达式。
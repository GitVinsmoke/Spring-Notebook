# SpringMVC的路径匹配规则 Ant-style

Spring默认的策略实现了org.springframework.util.AntPathMatcher，即Apache Ant的样式路径，Apache Ant样式的路径有三种通配符匹配方法（在下面的表格中列出)：

| wildcard | Description             |
| -------- | ----------------------- |
| ?        | 匹配任何单字符          |
| *        | 匹配0或者任意数量的字符 |
| **       | 匹配0或者更多的目录     |

在 org.springframework.util.AntPathMatcher 类的注释里，还提供了一条规则，即 `{spring:[a-z]+}`，该条规则可以匹配名称为 `spring` 且符合正则表达式 `[a-z]+` 的路径变量（path variable）。

## Example

在 org.springframework.util.AntPathMatcher 类的注释里，还提供了一些示例：

- **com/t?st.jsp** - matches com/test.jsp but also com/tast.jsp or com/txst.jsp

- **com/\*.jsp** - matches all .jsp files in the com directory

- **com/\**/test.jsp** - matches all test.jsp files underneath the com path

- **org/springframework/\**/\*.jsp** - matches all .jsp files underneath the org/springframework path

- **org/\**/servlet/bla.jsp** - matches org/springframework/servlet/bla.jsp but also org/springframework/testing/servlet/bla.jsp and org/servlet/bla.jsp

- **com/{filename:\\w+}.jsp** will match com/test.jsp and assign the value test to the filename variable

现在我们可以来使用一下Spring提供的这个工具类：

```java
public static void main(String[] args) {
  AntPathMatcher matcher = new AntPathMatcher();
  String pattern = "com/**/*.jsp";

  System.out.println(matcher.isPattern(pattern));
  System.out.println(matcher.match(pattern, "com/test.jsp"));
  System.out.println(matcher.match(pattern, "com/springframework/test.jsp"));
}
```

Output

```
true
true
true
```


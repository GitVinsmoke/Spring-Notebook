package com.spring.instruction;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

/*注解为一个切面，切面的功能就是引入新的功能*/

@Aspect
@Component
public class OptionInstrucer {
	
	/*将此接口注入到被通知的对象中*/
	@DeclareParents(value="com.spring.instruction.Base+", defaultImpl=OptionDelegate.class)
	public static Option option;
	
}

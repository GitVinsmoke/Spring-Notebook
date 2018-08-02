package com.spring.instruction;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

/*ע��Ϊһ�����棬����Ĺ��ܾ��������µĹ���*/

@Aspect
@Component
public class OptionInstrucer {
	
	/*���˽ӿ�ע�뵽��֪ͨ�Ķ�����*/
	@DeclareParents(value="com.spring.instruction.Base+", defaultImpl=OptionDelegate.class)
	public static Option option;
	
}

package com.spring.instruction;

import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Base {
	
	public static void main(String[] args) {
		ApplicationContext ctx=new ClassPathXmlApplicationContext("com"+File.separator+"spring"+File.separator+"instruction"+File.separator+"applicationContext.xml");
		Base b=ctx.getBean("base", Base.class);
		Option option=(Option) b;
		option.execute();
	}
}

package com.spring.advice;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BankTest {
	
	@Value(value="memory")
	private String song;
	

	public static void main(String[] args) {
		
		ApplicationContext ctx=new ClassPathXmlApplicationContext("com"+File.separator+"spring"+File.separator+"advice"+File.separator+"applicationContext.xml");
		/*不能将入参改为ATM.class，否则通知不起效，因为我们通知的是Bank中的方法*/
		Bank bank=ctx.getBean("ATM", Bank.class);
		bank.depositMoney(200);
		
		BankTest t=ctx.getBean(BankTest.class);
		System.out.println(t.song);
	}

}

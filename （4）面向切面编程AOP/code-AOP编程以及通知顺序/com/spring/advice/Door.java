package com.spring.advice;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(value=0)
public class Door {

	@Pointcut(value="execution(* com.spring.advice.Bank.*(..))")
	public void pointcut() {}
	
	@Before("pointcut()")
	public void openDoor() {
		System.out.println("开门");
	}
	
	@After("pointcut()")
	public void closeDoor() {
		System.out.println("关门");
	}
}

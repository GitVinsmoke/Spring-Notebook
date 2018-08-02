package com.spring.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*������*/
@Component
@Aspect
@Order(value=1)
public class Detection {
	
	@Pointcut(value="execution(* com.spring.advice.Bank.*(..))")
	public void pointcut() {}
	
	@Pointcut(value="execution(* com.spring.advice.Bank.drawMoney(..))")
	public void drawpointcut() {}
	
	@Pointcut(value="execution(* com.spring.advice.Bank.depositMoney(..))")
	public void depositpointcut() {}
	
	/* ���ȵİ�ȫ��⣬�����в�������Ҫ��� */
	@Before("pointcut()")
	public void detectSecurity() {
		System.out.println("��Χ������ȫ��");
	}
	
	/*������ʾ*/
	@Before("pointcut()")
	public void inputPoint() {
		System.out.println("������ʾ...");
	}
	
	
	@AfterReturning("depositpointcut()")
	public void depositSucess() {
		System.out.println("���Ѵ��ɹ�����ȡ�����п�");
	}
	
	@AfterReturning("drawpointcut()")
	public void drawSuccess() {
		System.out.println("���Ѿ�ȡ��ɹ������ú��ֽ�ȡ�����п�");
	}
	
	@AfterThrowing("pointcut()")
	public void transactionFailed() {
		System.out.println("����ʧ�ܣ�");
	}
	
	@Around(value="pointcut()")
	public void around(ProceedingJoinPoint joinPoint) {
		System.out.println("����֪ͨǰ");
		try {
			/*���ӵ��ִ�У�Spring��Ϊ������*/
			joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("����֪ͨ��");
	}
	
}

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

/*切面检测*/
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
	
	/* 事先的安全检测，对所有操作都需要检测 */
	@Before("pointcut()")
	public void detectSecurity() {
		System.out.println("周围环境完全！");
	}
	
	/*操作提示*/
	@Before("pointcut()")
	public void inputPoint() {
		System.out.println("操作提示...");
	}
	
	
	@AfterReturning("depositpointcut()")
	public void depositSucess() {
		System.out.println("您已存款成功，请取出银行卡");
	}
	
	@AfterReturning("drawpointcut()")
	public void drawSuccess() {
		System.out.println("您已经取款成功，请拿好现金并取出银行卡");
	}
	
	@AfterThrowing("pointcut()")
	public void transactionFailed() {
		System.out.println("交易失败！");
	}
	
	@Around(value="pointcut()")
	public void around(ProceedingJoinPoint joinPoint) {
		System.out.println("环绕通知前");
		try {
			/*连接点的执行（Spring中为方法）*/
			joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("环绕通知后");
	}
	
}

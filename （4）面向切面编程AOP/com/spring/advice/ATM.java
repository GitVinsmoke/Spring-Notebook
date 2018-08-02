package com.spring.advice;

import org.springframework.stereotype.Component;

@Component
public class ATM implements Bank {

	@Override
	public String drawMoney() {
		System.out.println("用户操作：已经取出100元");
		return "取出100元";
	}

	@Override
	public void depositMoney(int money) {
		if(money<100) {
			throw new IllegalArgumentException();
		}
		System.out.println("用户操作：存入" + money + "元");
	}

}

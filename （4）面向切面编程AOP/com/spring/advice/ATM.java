package com.spring.advice;

import org.springframework.stereotype.Component;

@Component
public class ATM implements Bank {

	@Override
	public String drawMoney() {
		System.out.println("�û��������Ѿ�ȡ��100Ԫ");
		return "ȡ��100Ԫ";
	}

	@Override
	public void depositMoney(int money) {
		if(money<100) {
			throw new IllegalArgumentException();
		}
		System.out.println("�û�����������" + money + "Ԫ");
	}

}

package com.li.student.service;

import java.io.File;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.li.student.domain.Student;
import com.li.student.persistence.StudentDao;

public class StudentService {
	
	/*��Ҫ���õ���DAO*/
	private StudentDao dao;
	
	/*��Ҫע�����*/
	private TransactionTemplate transactionTemplate;

	public void setDao(StudentDao dao) {
		this.dao = dao;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public void addStud(Student s) {
		transactionTemplate.execute(new TransactionCallback<Object>() {

			@Override
			public Object doInTransaction(TransactionStatus status) {
				try {
					dao.addStu(s);
				} catch (RuntimeException e) {
					status.setRollbackOnly();
					throw e;
				}
				return null;
			}
		});
	}
	
	public List<Student> getAllStus() {
		/*��Ϊ����һ����ȡ�ķ��������Կ��Բ���ӵ�������*/
		return dao.getAllStus();
	}
	
	public void saveStu(Student s) {
		transactionTemplate.execute(new TransactionCallback<Object>() {

			@Override
			public Object doInTransaction(TransactionStatus status) {
				try {
					dao.saveStu(s);
				} catch (RuntimeException e) {
					status.setRollbackOnly();
					throw e;
				}
				return null;
			}
		});
	}
	
	public static void main(String[] args) {
		ApplicationContext ctx=new ClassPathXmlApplicationContext("com"+File.separator+"li"+File.separator+"student"+File.separator+"application.xml");
		StudentService ss=ctx.getBean("studentService", StudentService.class);
		
		List<Student> list=ss.getAllStus();
		for(Student s : list)
			System.out.println(s);
		
	}
}

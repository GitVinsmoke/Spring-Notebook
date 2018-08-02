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
	
	/*需要调用到的DAO*/
	private StudentDao dao;

	public void setDao(StudentDao dao) {
		this.dao = dao;
	}

	public void addStud(Student s) {
		dao.addStu(s);
	}
	
	public List<Student> getAllStus() {
		return dao.getAllStus();
	}
	
	public void saveStu(Student s) {
		dao.saveStu(s);
	}
	
	public static void main(String[] args) {
		ApplicationContext ctx=new ClassPathXmlApplicationContext("com"+File.separator+"li"+File.separator+"student"+File.separator+"application.xml");
		StudentService ss=ctx.getBean("studentService", StudentService.class);
		
		List<Student> list=ss.getAllStus();
		for(Student s : list)
			System.out.println(s);
		
	}
}

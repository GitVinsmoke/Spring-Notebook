package com.li.spring.database.service;

import java.io.File;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.li.spring.database.domain.Student;
import com.li.spring.database.persistence.BaseDao;

public class StudentService {
	
	/*面向接口编程，使其不依赖某一个具体的DAO，当需要改变实现时，注入不同的实现即可*/
	private BaseDao dao;

	public void setDao(BaseDao dao) {
		this.dao = dao;
	}
	
	public void addStu(Student student) {
		dao.addStu(student);
	}
	
	public List<Student> getAllStus() {
		return dao.getAllStus();
	}
	
	public void saveStu(Student student) {
		dao.saveStu(student);
	}
	
	public static void main(String[] args) {
		ApplicationContext ctx=new ClassPathXmlApplicationContext("com"+File.separator+"li"+File.separator+"spring"+File.separator+"database"+File.separator+"database.xml");
		StudentService ss=ctx.getBean("studentService", StudentService.class);
//		Student s=new Student("2025", "lin", 20, "male");
//		ss.addStu(s);
		List<Student> list=ss.getAllStus();
		for(Student student : list) {
			System.out.println(student);
		}
	}
}

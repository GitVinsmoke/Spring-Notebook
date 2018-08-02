package com.li.student.service;

import java.io.File;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.li.student.domain.Student;
import com.li.student.persistence.StudentDao;

@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
public class StudentService {
	
	/*��Ҫ���õ���DAO*/
	private StudentDao dao;


	public void setDao(StudentDao dao) {
		this.dao = dao;
	}

	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void addStud(Student s) {
		dao.addStu(s);
	}
	
	public List<Student> getAllStus() {
		/*��Ϊ����һ����ȡ�ķ��������Կ��Բ���ӵ�������*/
		return dao.getAllStus();
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
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

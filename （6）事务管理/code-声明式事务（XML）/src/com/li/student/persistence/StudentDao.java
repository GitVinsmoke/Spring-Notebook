package com.li.student.persistence;

import java.util.List;

import com.li.student.domain.Student;

/*��¶���ⲿ�����ݿ�����ӿڣ�����Ϊ����*/

public interface StudentDao {
	
	void addStu(Student s);
	
	List<Student> getAllStus();
	
	void saveStu(Student s);
}

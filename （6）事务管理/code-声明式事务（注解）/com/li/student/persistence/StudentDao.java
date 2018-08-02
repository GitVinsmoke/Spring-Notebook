package com.li.student.persistence;

import java.util.List;

import com.li.student.domain.Student;

/*暴露给外部的数据库操作接口，定义为泛型*/

public interface StudentDao {
	
	void addStu(Student s);
	
	List<Student> getAllStus();
	
	void saveStu(Student s);
}

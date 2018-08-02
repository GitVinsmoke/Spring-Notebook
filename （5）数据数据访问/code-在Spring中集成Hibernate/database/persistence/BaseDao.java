package com.li.spring.database.persistence;

import java.util.List;

import com.li.spring.database.domain.Student;

public interface BaseDao {
	
	void addStu(Student student);
	
	List<Student> getAllStus();
	
	void saveStu(Student student);
}

package com.li.spring.database.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="Student")
@Table(name="stu_info")
public class Student {
	
	private String stuNo;
	
	private String stuName;
	
	
	private Integer stuAge;
	
	private String stuGender;

	@Id
	@Column(name="stu_no")
	public String getStuNo() {
		return stuNo;
	}
	public void setStuNo(String stuNo) {
		this.stuNo = stuNo;
	}
	
	@Column(name="stu_name")
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	
	@Column(name="stu_age")
	public Integer getStuAge() {
		return stuAge;
	}
	public void setStuAge(Integer stuAge) {
		this.stuAge = stuAge;
	}
	
	@Column(name="stu_gender")
	public String getStuGender() {
		return stuGender;
	}
	public void setStuGender(String stuGender) {
		this.stuGender = stuGender;
	}
	
	public Student() {
		super();
	}
	
	public Student(String stuNo, String stuName, Integer stuAge, String stuGender) {
		super();
		this.stuNo = stuNo;
		this.stuName = stuName;
		this.stuAge = stuAge;
		this.stuGender = stuGender;
	}
	@Override
	public String toString() {
		return "Student [stuNo=" + stuNo + ", stuName=" + stuName + ", stuAge=" + stuAge + ", stuGender=" + stuGender
				+ "]";
	}
	
}

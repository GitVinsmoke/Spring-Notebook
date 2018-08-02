package com.li.student.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.li.student.domain.Student;

public class StudentDaoImpl implements StudentDao {

	/*Jdbc模板类对象*/
	private JdbcTemplate jdbcTemplate;
	
	private static final String SQL_INSERT="insert into stu_info (stu_no, stu_name, stu_age, stu_gender) values(?,?,?,?)";
	private static final String SQL_SELECT_ALL="select * from stu_info";
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate=jdbcTemplate;
	}
	
	@Override
	public void addStu(Student s) {
		jdbcTemplate.update(SQL_INSERT, s.getStuNo(), s.getStuName(), s.getStuAge(), s.getStuGender());
	}

	@Override
	public List<Student> getAllStus() {
		return jdbcTemplate.query(SQL_SELECT_ALL, new RowMapper<Student>() {

			@Override
			public Student mapRow(ResultSet rs, int arg1) throws SQLException {
				Student s=new Student();
				s.setStuNo(rs.getString("stu_no"));
				s.setStuName(rs.getString("stu_name"));
				s.setStuGender(rs.getString("stu_gender"));
				s.setStuAge(rs.getInt("stu_age"));
				return s;
			}
			
		});
	}

	@Override
	public void saveStu(Student s) {
		String sql="update stu_info set stu_name=?, stu_gender=?, stu_age=? where stu_no=?";
		jdbcTemplate.update(sql, s.getStuName(), s.getStuGender(), s.getStuAge(), s.getStuNo());
	}

}

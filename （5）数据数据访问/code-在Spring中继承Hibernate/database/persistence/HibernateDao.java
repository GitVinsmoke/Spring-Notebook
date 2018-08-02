package com.li.spring.database.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.li.spring.database.domain.Student;

/*对此DAO进行注解*/
@Repository
public class HibernateDao implements BaseDao {

	/*Hibernate DAO中应该要有一个SessionFactory字段*/
	private SessionFactory sessionFactory;
	
	/*往构造器中自动注入参数*/
	@Autowired
	public HibernateDao(SessionFactory sessionFactory) {
		this.sessionFactory=sessionFactory;
	}
	
	/*往构造器中自动注入参数*/
	public HibernateDao() {
		super();
	}

	/*获取Session对象，类似于JDBC的连接，获取Session才能进行操作*/
	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void addStu(Student student) {
		currentSession().save(student);
	}

	@Override
	public List<Student> getAllStus() {
		Query<Student> query=currentSession().createQuery("from Student", Student.class);
		List<Student> list=query.getResultList();
		return list;
	}

	@Override
	public void saveStu(Student student) {
		currentSession().update(student);
	}

}

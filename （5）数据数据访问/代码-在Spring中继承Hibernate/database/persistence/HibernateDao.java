package com.li.spring.database.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.li.spring.database.domain.Student;

/*�Դ�DAO����ע��*/
@Repository
public class HibernateDao implements BaseDao {

	/*Hibernate DAO��Ӧ��Ҫ��һ��SessionFactory�ֶ�*/
	private SessionFactory sessionFactory;
	
	/*�����������Զ�ע�����*/
	@Autowired
	public HibernateDao(SessionFactory sessionFactory) {
		this.sessionFactory=sessionFactory;
	}
	
	/*�����������Զ�ע�����*/
	public HibernateDao() {
		super();
	}

	/*��ȡSession����������JDBC�����ӣ���ȡSession���ܽ��в���*/
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

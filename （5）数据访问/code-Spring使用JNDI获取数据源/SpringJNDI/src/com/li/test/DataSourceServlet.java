package com.li.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@WebServlet("/DataSourceServlet")
public class DataSourceServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    public DataSourceServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		JdbcTemplate jdbcTemplate=ctx.getBean("jdbcTemplate", JdbcTemplate.class);
		System.out.println(jdbcTemplate);
		System.out.println(jdbcTemplate.getDataSource());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

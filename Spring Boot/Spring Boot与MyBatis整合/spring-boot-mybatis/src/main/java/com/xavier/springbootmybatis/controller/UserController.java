package com.xavier.springbootmybatis.controller;

import com.xavier.springbootmybatis.domain.User;
import com.xavier.springbootmybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Xavier Li
 * 2019/7/15
 */
@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @RequestMapping("/query")
    public User query() {
        return userService.findUserById(1L);
    }

    @RequestMapping("/insert")
    public int insert() {
        User user = new User();
        user.setId(2L);
        user.setUserName("Nike");
        user.setPassword("000000");
        user.setCreateTime(new Date(System.currentTimeMillis()));
        return userService.insertUser(user);
    }
}

package com.xavier.springbootmybatis.service;

import com.xavier.springbootmybatis.dao.UserDao;
import com.xavier.springbootmybatis.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Xavier Li
 * 2019/7/15
 */
@Service
public class UserService {

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findUserById(Long id) {
        return userDao.findUserById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }
}

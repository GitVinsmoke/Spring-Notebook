package com.xavier.springbootmybatis.dao;

import com.xavier.springbootmybatis.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Xavier Li
 * 2019/7/15
 */
@Mapper
@Component
public interface UserDao {

    /**
     * Find a user by id
     *
     * @param id user id
     * @return   user domain
     */
    User findUserById(Long id);

    /**
     * Insert a user
     *
     * @param user user domain
     * @return     result
     */
    int insertUser(User user);
}

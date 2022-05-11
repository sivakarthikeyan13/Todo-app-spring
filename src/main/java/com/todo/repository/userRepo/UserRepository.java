package com.todo.repository.userRepo;

import com.todo.domain.User;
import org.hibernate.HibernateException;

public interface UserRepository {

    void saveUser(User user) throws HibernateException;
    boolean existsUserByEmail(String email) throws HibernateException;
    User findUserByEmailAndPassword(String email, String password) throws HibernateException;
    User findUserById(Long id) throws HibernateException;
    void updateUser(User user) throws HibernateException;
    boolean existsUserById(Long id) throws HibernateException;
    void deleteUserById(Long id) throws HibernateException;
}

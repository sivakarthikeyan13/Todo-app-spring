package com.todo.service;

import com.todo.domain.User;

public interface UserService {
    User createNewUser(User user) throws Exception;
    User fetchUserLogin(User user) throws Exception;
    User updateUser(Long id, User user) throws Exception;
    void deleteUser(Long id) throws Exception;
}

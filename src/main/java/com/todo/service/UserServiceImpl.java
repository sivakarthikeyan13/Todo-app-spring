package com.todo.service;

import com.todo.domain.User;
import com.todo.repository.userRepo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InputValidation inputValidation;

    @Override
    public User createNewUser(User user) throws Exception {
        String name = user.getName();
        String email = user.getEmail();
        String password = user.getPassword();
        String confirmPassword = user.getConfirmPassword();

        // ----------INPUT VALIDATIONS----------
        List<String> inputList = Arrays.asList(name, email, password, confirmPassword);
        inputValidation.emptyOrNullFieldValidation(inputList);
        inputValidation.inputFieldTypeValidation(name, email, password);
        inputValidation.confirmPasswordMatchValidation(password, confirmPassword);

        //check if email already exists
        if (userRepository.existsUserByEmail(email))
            throw new Exception(email + " is already registered!");
        logger.info("email id available");
        //save user to repo
        userRepository.saveUser(user);
        logger.info("User saved in database");
        return user;
    }

    @Override
    public User fetchUserLogin(User user) throws Exception {
        String email = user.getEmail();
        String password = user.getPassword();

        // ----------INPUT VALIDATIONS----------
        List<String> inputList = Arrays.asList(email, password);
        inputValidation.emptyOrNullFieldValidation(inputList);
        inputValidation.inputFieldTypeValidation(email, "email");

        //get user from repo
        User existingUser = userRepository.findUserByEmailAndPassword(email, password);
        //check if user exists
        if (existingUser == null)
            throw new Exception("Invalid Email or Password !!!");
        logger.info("User details fetched from database");
        return existingUser;
    }

    @Override
    public User updateUser(Long id, User user) throws Exception {
        String name = user.getName();
        String email = user.getEmail();
        String password = user.getPassword();
        String newPassword = user.getNewPassword();
        String confirmPassword = user.getConfirmPassword();

        //check if password is provided
        if (password == null || password.isEmpty())
            throw new Exception("Enter password to Change Profile details");
        //check if user exists
        User existingUser = userRepository.findUserById(id);
        if (existingUser == null)
            throw new Exception("user with id " + id + " does not exists");
        logger.info("Existing User with id: " + id + " found in database");
        //check if password is correct
        if (!password.equals(existingUser.getPassword()))
            throw new Exception("Incorrect Password!");
        logger.info("password matches");

        //----------FILL EMPTY FIELDS----------
        //set id
        user.setId(existingUser.getId());
        //set name
        if (name.isEmpty())
            user.setName(existingUser.getName());
        else
            inputValidation.inputFieldTypeValidation(name, "name");
        //set email
        if (email.isEmpty()){
            user.setEmail(existingUser.getEmail());
        } else{
            inputValidation.inputFieldTypeValidation(email, "email");
            if (userRepository.existsUserByEmail(email))
                throw new Exception(email + " is already registered!");
            logger.info(email + " is valid and available");
        }

        //set newPassword
        if (newPassword != null && !newPassword.isEmpty()) {
            inputValidation.confirmPasswordMatchValidation(newPassword, confirmPassword);
            if (newPassword.equals(password))
                throw new Exception("New password can not be same a old password!");
            inputValidation.inputFieldTypeValidation(newPassword, "password");
            user.setPassword(newPassword);
        }

        //update user details in repo
        userRepository.updateUser(user);
        logger.info("User with id: " + id + " updated in database");
        return user;
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        //check if user exists in repo
        if(!userRepository.existsUserById(id))
            throw new Exception("user with id: " + id + " does not exists");
        logger.info("Existing User with id: " + id + " found in database");
        //delete user in repo
        userRepository.deleteUserById(id);
        logger.info("User with id: " + id + " deleted from database");
    }
}

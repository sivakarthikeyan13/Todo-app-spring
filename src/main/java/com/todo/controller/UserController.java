package com.todo.controller;

import com.todo.domain.User;
import com.todo.dto.UserDto;
import com.todo.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

//    @GetMapping("/")
//    public ResponseEntity<?> hello(){
//        logger.info("APP STARTED...");
//        return ResponseEntity.ok("spring created...");
//    }

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        logger.info("registerUser api called...");
        ResponseEntity<?> response = null;
        UserDto userDto = null;
        try {
            User registerResponse = userService.createNewUser(user);
            logger.info("User registration successful");
            userDto = modelMapper.map(registerResponse, UserDto.class);
            response = new ResponseEntity<>(userDto, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("User registration unsuccessful: " + e.getMessage(),e);
            response =  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        logger.info("registerUser api response sent...");
        return response;
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        logger.info("loginUser api called...");
        ResponseEntity<?> response = null;
        UserDto userDto = null;
        try {
            User loginResponse = userService.fetchUserLogin(user);
            logger.info("User login successful");
            userDto = modelMapper.map(loginResponse, UserDto.class);
            response = ResponseEntity.ok(userDto);
        } catch (Exception e) {
            logger.error("User login unsuccessful: " + e.getMessage(),e);
            response =  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        logger.info("loginUser api response sent...");
        return response;
    }

    @PutMapping("/api/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        logger.info("updateUser api called...");
        ResponseEntity<?> response = null;
        UserDto userDto = null;
        try {
            User updateResponse = userService.updateUser(id, user);
            logger.info("User updated successfully");
            userDto = modelMapper.map(updateResponse, UserDto.class);
            response = new ResponseEntity<>(userDto, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("User update unsuccessful: " + e.getMessage(),e);
            response =  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        logger.info("updateUser api response sent...");
        return response;
    }

    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("deleteUser api called...");
        ResponseEntity<?> response = null;
        try {
            userService.deleteUser(id);
            logger.info("User deletion successful");
            response = ResponseEntity.ok("User deleted");
        } catch (Exception e) {
            logger.error("User deletion unsuccessful: " + e.getMessage(), e);
            response = new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        logger.info("deleteUser api response sent...");
        return response;
    }
}

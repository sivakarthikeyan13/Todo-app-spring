package com.todo.controller;

import com.todo.domain.TodoItem;
import com.todo.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TodoController {

    Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoService todoService;

    @GetMapping("/api/todoItems/{userId}")
    public ResponseEntity<?> fetchAllTodoItems(@PathVariable Long userId) {
        logger.info("fetchAllTodoItems api called...");
        ResponseEntity<?> response = null;
        try {
            List<TodoItem> todoItems = todoService.fetchAllTodoItems(userId);
            logger.info("Todo list fetch successful");
            response = ResponseEntity.ok(todoItems); // .ok() is short for status(HttpStatus.OK).body()
        } catch (Exception e) {
            logger.error("Todo list fetch unsuccessful: " + e.getMessage(),e);
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        logger.info("fetchAllTodoItems api response sent...");
        return response;
    }

    @PostMapping("/api/todoItems")
    public ResponseEntity<?> createNewTodoItem(@RequestBody TodoItem todoItem) {
        logger.info("createNewTodoItem api called...");
        ResponseEntity<?> response = null;
        try {
            TodoItem newTodoItem = todoService.createNewTodoItem(todoItem);
            logger.info("Todo item created successfully");
            response = ResponseEntity.ok(newTodoItem);
        } catch (Exception e) {
            logger.error("Todo item creation unsuccessful: " + e.getMessage(),e);
            response =  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        logger.info("createNewTodoItem api response sent...");
        return  response;
    }

    @PutMapping("/api/todoItems/{id}")
    public ResponseEntity<?> updateTodoItems(@PathVariable Long id, @RequestBody TodoItem todoItem) {
        logger.info("updateTodoItems api called...");
        ResponseEntity<?> response = null;
        try {
            TodoItem updatedTodoItem = todoService.updateTodoItem(id, todoItem);
            logger.info("Todo item updated successfully");
            response = ResponseEntity.ok(updatedTodoItem);
        } catch (Exception e) {
            logger.error("Todo item update unsuccessful: " + e.getMessage(),e);
            response =  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        logger.info("updateTodoItems api response sent...");
        return response;
    }

    @DeleteMapping("/api/todoItems/{id}")
    public ResponseEntity<?> deleteTodoItems(@PathVariable Long id) {
        logger.info("deleteTodoItems api called...");
        ResponseEntity<?> response = null;
        try {
            todoService.deleteTodoItem(id);
            logger.info("Todo item deletion successful");
            response = ResponseEntity.ok("Todo Item deleted");
        } catch (Exception e) {
            logger.error("Todo item deletion unsuccessful: " + e.getMessage(), e);
            response = new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        logger.info("deleteTodoItems api response sent...");
        return response;
    }
}

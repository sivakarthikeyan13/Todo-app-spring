package com.todo.service;

import com.todo.domain.TodoItem;
import com.todo.repository.todoRepo.TodoRepository;
import com.todo.repository.userRepo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InputValidation inputValidation;

    @Override
    public List<TodoItem> fetchAllTodoItems(Long userId) throws Exception {
        if (!userRepository.existsUserById(userId))
            throw new Exception("user with id: " + userId + " does not exists");
        logger.info("User with id: " + userId + " found");
        List<TodoItem> todoItemList = todoRepository.findAllByUserIdOrderByDate(userId);
        logger.info("Todo list of user collected from database");
        return todoItemList;
    }

    @Override
    public TodoItem createNewTodoItem(TodoItem todoItem) throws Exception {
        String taskName = todoItem.getTask();
        Long userId = todoItem.getUserId();
        String date = todoItem.getDate();

        // ----------INPUT VALIDATIONS----------
        if (userId == null)
            throw new Exception("enter user id");
        if (!userRepository.existsUserById(userId))
            throw new Exception("user with id: " + userId + " does not exists");
        logger.info("user id is valid");
        List<String> inputList = Arrays.asList(taskName, date);
        inputValidation.emptyOrNullFieldValidation(inputList);
        inputValidation.inputFieldTypeValidation(taskName, "task");
        //TODO: VALIDATE DATE >= TODAY

        //save task item to repo
        todoRepository.saveTodoItem(todoItem);
        logger.info("Task saved in database");
        return todoItem;
    }

    @Override
    public TodoItem updateTodoItem(Long id, TodoItem todoItem) throws Exception {

        String taskName = todoItem.getTask();
        Long userId = todoItem.getUserId();

        if(userId == null)
            throw new Exception("User id not found!");
        TodoItem existingItem = todoRepository.findItemById(id);
        //check if task-item exists
        if (existingItem == null)
            throw new Exception("Task with id: " + id + " does not exists");
        logger.info("Existing Task with id: " + id + " found");
        if (!existingItem.getUserId().equals(userId))
            throw new Exception("Task with id: " + id + " did not match with user id: " + userId );

        //FILL EMPTY FIELDS
        //set id
        todoItem.setId(existingItem.getId());
        //set userId
        todoItem.setUserId(existingItem.getUserId());

        //set task name
        if (taskName.isEmpty())
            todoItem.setTask(existingItem.getTask());
        else
            inputValidation.inputFieldTypeValidation(taskName, "task");
        //set isDone
        if (todoItem.getIsDone() == null)
            todoItem.setIsDone(existingItem.getIsDone());
        //set date
        if (todoItem.getDate() == null || todoItem.getDate().isEmpty())
            todoItem.setDate(LocalDate.parse(existingItem.getDate()));
//        else
//            TODO: VALIDATE DATE >= TODAY


        //update task-item details in repo
        todoRepository.updateItem(todoItem);
        logger.info("Task with id: " + id + " updated in database");
        return todoItem;
    }

    @Override
    public void deleteTodoItem(Long id) throws Exception {
        //check if task exists in repo
        if (!todoRepository.existsItemById(id))
            throw new Exception("Task with id: " + id + " does not exists");
        logger.info("Existing Task with id: " + id + " found in database");
        //delete task in repo
        todoRepository.deleteItemById(id);
        logger.info("Task with id: " + " deleted from database");
    }

}

package com.todo.service;

import com.todo.domain.TodoItem;

import java.util.List;

public interface TodoService {
    List<TodoItem> fetchAllTodoItems(Long userId) throws Exception;
    TodoItem createNewTodoItem(TodoItem todoItem) throws Exception;
    TodoItem updateTodoItem(Long id, TodoItem todoItem) throws Exception;
    void deleteTodoItem(Long id) throws Exception;
}

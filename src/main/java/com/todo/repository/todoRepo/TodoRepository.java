package com.todo.repository.todoRepo;

import com.todo.domain.TodoItem;
import org.hibernate.HibernateException;

import java.util.List;

public interface TodoRepository {
    List<TodoItem> findAllByUserId(Long userId) throws HibernateException;
    List<TodoItem> findAllByDate(Long userId) throws HibernateException;
    List<TodoItem> findAllByUserIdOrderByDate(Long userId) throws HibernateException;
    void saveTodoItem(TodoItem todoItem) throws HibernateException;
    TodoItem findItemById(Long id) throws HibernateException;
    void updateItem(TodoItem item) throws HibernateException;
    boolean existsItemById(Long id)  throws HibernateException;
    void deleteItemById(Long id) throws HibernateException;
}

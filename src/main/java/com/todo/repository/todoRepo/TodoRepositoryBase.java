package com.todo.repository.todoRepo;

import com.todo.domain.TodoItem;
import org.hibernate.HibernateException;

import java.util.List;

public abstract class TodoRepositoryBase implements TodoRepository {

    //NOT USED
    @Override
    public List<TodoItem> findAllByUserId(Long userId) throws HibernateException {
        return null;
    }

    //NOT USED
    @Override
    public List<TodoItem> findAllByDate(Long userId) throws HibernateException {
        return null;
    }

    @Override
    public List<TodoItem> findAllByUserIdOrderByDate(Long userId) throws HibernateException{
        return null;
    }

    @Override
    public void saveTodoItem(TodoItem todoItem) throws HibernateException {

    }

    @Override
    public TodoItem findItemById(Long id) throws HibernateException {
        return null;
    }

    @Override
    public void updateItem(TodoItem item) throws HibernateException {

    }

    @Override
    public boolean existsItemById(Long id) throws HibernateException {
        return false;
    }

    @Override
    public void deleteItemById(Long id) throws HibernateException {

    }
}

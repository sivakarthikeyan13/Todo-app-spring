package com.todo.repository.todoRepo;

import com.todo.domain.TodoItem;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public class TodoRepositoryImpl extends TodoRepositoryBase {

    Logger logger = LoggerFactory.getLogger(TodoRepositoryImpl.class);


    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<TodoItem> findAllByUserIdOrderByDate(Long userId) throws HibernateException {
        Session session = null;
        List<TodoItem> todoItemList;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from TodoItem i where i.userId=:userId order by i.date");
            query.setParameter("userId",userId);
            todoItemList = query.getResultList();
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
        return todoItemList;
    }

    @Override
    public void saveTodoItem(TodoItem todoItem) throws HibernateException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(todoItem);
            session.getTransaction().commit();
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
    }

    @Override
    public TodoItem findItemById(Long id) throws HibernateException {
        Session session = null;
        TodoItem item;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            item = session.get(TodoItem.class, id);
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
        return item;
    }

    @Override
    public void updateItem(TodoItem item) throws HibernateException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(item);
            session.getTransaction().commit();
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
    }

    @Override
    public boolean existsItemById(Long id) throws HibernateException {
        Session session = null;
        TodoItem item;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            item = session.get(TodoItem.class, id);
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
        return item != null;
    }

    @Override
    public void deleteItemById(Long id) throws HibernateException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            TodoItem item = session.get(TodoItem.class, id);
            logger.info(String.valueOf(item));
            if (item != null) {
                session.delete(item);
                session.getTransaction().commit();
            }
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
    }
}

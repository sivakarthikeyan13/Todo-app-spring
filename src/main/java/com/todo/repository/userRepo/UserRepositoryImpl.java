package com.todo.repository.userRepo;

import com.todo.domain.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveUser(User user) throws HibernateException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
    }

    @Override
    public boolean existsUserByEmail(String email) throws HibernateException {
        Session session = null;
        User existingUser;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from User s where s.email=:email");
            query.setParameter("email",email);
            existingUser = (User) query.uniqueResult();
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
        return existingUser != null;
    }

    @Override
    public User findUserByEmailAndPassword(String email, String password) throws HibernateException {
        Session session = null;
        User loggedInUser;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from User s where s.email=:email and s.password=:password");
            query.setParameter("email", email);
            query.setParameter("password", password);
            loggedInUser = (User) query.uniqueResult();
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
        return loggedInUser;
    }

    @Override
    public User findUserById(Long id) throws HibernateException {
        Session session = null;
        User user;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            user = session.get(User.class, id);
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
        return user;
    }

    @Override
    public void updateUser(User user) throws HibernateException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
    }

    @Override
    public boolean existsUserById(Long id) throws HibernateException {
        Session session = null;
        User user;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            user = session.get(User.class, id);
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
        return user != null;
    }

    @Override
    public void deleteUserById(Long id) throws HibernateException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            User user = session.get(User.class, id);
            logger.info(String.valueOf(user));
            if (user != null) {
                session.delete(user);
                Query query = session.createQuery("delete from TodoItem i where i.userId=:userId");
                query.setParameter("userId", id);
                int delCount = query.executeUpdate();
                logger.info(String.valueOf(delCount) + " tasks deleted");
                session.getTransaction().commit();
            }
        } finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
    }
}

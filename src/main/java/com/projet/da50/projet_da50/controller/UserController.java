package com.projet.da50.projet_da50.controller;

import com.projet.da50.projet_da50.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import com.projet.da50.projet_da50.model.User;

public class UserController {

    private SessionFactory factory;

    public UserController() {
        // Create the SessionFactory when the application is started
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class).buildSessionFactory();
    }

    // Find a user by username
    public User findUserByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Find a user by mail
    public User findUserByMail(String mail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User where mail = :mail", User.class);
            query.setParameter("mail", mail);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Create a new user
    public Long createUser(String username, String password, String mail) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            if (findUserByUsername(username) != null) {
                System.err.println("Username already exists: " + username);
                return null;
            }
            if (findUserByMail(mail) != null) {
                System.err.println("Mail already exists: " + mail);
                return null;
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            User user = new User(username, hashedPassword, mail);

            Long id = (Long) session.save(user);
            transaction.commit();
            System.out.println("User created with ID: " + id);
            return id;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    // Verify if a user exists with the right password
    public boolean verifyUserCredentials(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = findUserByUsername(username);
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a user
    public boolean deleteUserById(Long userId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) {
                session.delete(user);
                transaction.commit();
                System.out.println("User deleted successfully: " + userId);
                return true;
            } else {
                System.err.println("User not found: " + userId);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.err.println("Failed to delete user: " + userId);
            return false;
        }
    }

    // Get a user by ID
    public User getUserById(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, userId);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to get user: " + userId);
            return null;
        }
    }

    /**
     * Updates a user's information in the database.
     *
     * @param user The user object with updated information.
     * @return True if the user was updated successfully, false otherwise.
     */
    public boolean updateUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            System.out.println("User updated successfully: " + user.getId());
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.err.println("Failed to update user: " + user.getId());
            return false;
        }
    }

    // Close the SessionFactory when the application is closed
    public void close() {
        if (factory != null && !factory.isClosed()) {
            factory.close();
        }
    }
}
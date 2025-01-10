package com.projet.da50.projet_da50.controller.user;

import com.projet.da50.projet_da50.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import com.projet.da50.projet_da50.model.user.User;

public class UserController {

    private final SessionFactory factory;

    /**
     * Default constructor for UserController.
     */
    public UserController() {
        // Create the SessionFactory when the application is started
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class).buildSessionFactory();
    }

    /**
     * Find a user by username
     * @param username The username of the user.
     * @return The user if found, null otherwise.
     */
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

    /**
     * Find a user by mail
     * @param mail The mail of the user.
     * @return The user if found, null otherwise.
     */
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

    /**
     * Create a user in the database
     * @param username The username of the user.
     * @param password The password of the user.
     * @param mail The mail of the user.
     * @return The ID of the user if created, null otherwise.
     */
    public Long createUser(String username, String password, String mail) {
        Transaction transaction;
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
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verify the user credentials (username and password) in the database
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the credentials are correct, false otherwise.
     */
    public boolean verifyUserCredentials(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = findUserByUsername(username);
            return user != null && BCrypt.checkpw(password, user.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a user by ID in the database
     * @param userId The ID of the user.
     * @return True if the user is deleted, false otherwise.
     */
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

    /**
     * Get a user by ID in the database
     * @param userId The ID of the user.
     * @return The user if found, null otherwise.
     */
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
     * Update a user in the database
     * @param user The user to update.
     * @return True if the user is updated, false otherwise.
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

    /**
     * Close the SessionFactory when the application is stopped
     */
    public void close() {
        if (factory != null && !factory.isClosed()) {
            factory.close();
        }
    }
}
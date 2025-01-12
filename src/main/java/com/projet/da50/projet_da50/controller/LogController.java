package com.projet.da50.projet_da50.controller;

import com.projet.da50.projet_da50.HibernateUtil;
import com.projet.da50.projet_da50.model.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * This class handles the creation and management of logs in the application.
 */
public class LogController {

    private final SessionFactory factory;

    /**
     * Constructor for LogController.
     * Initializes the SessionFactory using the Hibernate configuration.
     */
    public LogController() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Log.class).buildSessionFactory();
    }

    /**
     * Creates a new log entry in the database.
     *
     * @param userId The ID of the user associated with the log.
     * @param action The action performed by the user.
     * @param detail Additional details about the action.
     */
    public void createLog(Long userId, String action, String detail) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Log log = new Log(userId, action, detail);
            session.save(log);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Closes the SessionFactory when the application is closed.
     */
    public void close() {
        if (factory != null && !factory.isClosed()) {
            factory.close();
        }
    }
}
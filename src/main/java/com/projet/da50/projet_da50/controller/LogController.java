package com.projet.da50.projet_da50.controller;

import com.projet.da50.projet_da50.HibernateUtil;
import com.projet.da50.projet_da50.model.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class LogController {

    private final SessionFactory factory;

    public LogController() {
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Log.class).buildSessionFactory();
    }

    public void createLog(Long userId, String action) {
        Transaction transaction = null;
        //String sql = "INSERT INTO logs (user_id, action) VALUES (?, ?)";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Log log = new Log(userId, action);
            System.out.println("Saving log");
            session.save(log);
            System.out.println("Saving log");
            transaction.commit();
            System.out.println("Log saved");
        } catch (Exception e) {
            System.out.println("Error saving log");
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Close the SessionFactory when the application is closed
    public void close() {
        if (factory != null && !factory.isClosed()) {
            factory.close();
        }
    }
}
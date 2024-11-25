package com.projet.da50.projet_da50.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.projet.da50.projet_da50.model.User;

public class UserController {

    private SessionFactory factory;
/*
    public UserController() {
        // Créer la SessionFactory à partir de la configuration
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class).buildSessionFactory();
    }

    // Vérifier si un utilisateur existe
    public boolean userExists(String username) {
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            String hql = "FROM User WHERE username = :username";
            Query<User> query = session.createQuery(hql);
            query.setParameter("username", username);
            User result = query.uniqueResult();
            session.getTransaction().commit();
            return result != null; // Si l'utilisateur existe
        } finally {
            session.close();
        }
    }

    // Créer un nouvel utilisateur
    public String createUser(String username, String password, String email) {
        if (userExists(username)) {
            return "L'utilisateur " + username + " existe déjà.";
        }

        // Créer une session Hibernate pour ajouter un utilisateur
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            User newUser = new User(username, password, email);
            session.save(newUser);  // Sauvegarder l'utilisateur dans la base de données
            session.getTransaction().commit(); // Valider la transaction
            return "Utilisateur créé avec succès !";
        } finally {
            session.close();
        }
    }

    // Vérifier si un utilisateur existe avec le bon mot de passe
    public boolean verifyUser(String username, String password) {
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            String hql = "FROM User WHERE username = :username AND password = :password";
            Query<User> query = session.createQuery(hql);
            query.setParameter("username", username);
            query.setParameter("password", password);
            User result = query.uniqueResult();
            session.getTransaction().commit();
            return result != null;  // Si l'utilisateur existe et que le mot de passe est correct
        } finally {
            session.close();
        }
    }

    // Fermer la sessionFactory quand elle n'est plus nécessaire
    public void close() {
        factory.close();
    }
 */
}

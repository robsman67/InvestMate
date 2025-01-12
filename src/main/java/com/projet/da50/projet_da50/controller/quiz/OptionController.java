package com.projet.da50.projet_da50.controller.quiz;

import com.projet.da50.projet_da50.HibernateUtil;
import com.projet.da50.projet_da50.model.quiz.Option;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Controller for the Option entity
 * This class is used to interact with the database
 */
public class OptionController {

    private final SessionFactory factory;

    /**
     * Constructor for the OptionController
     * This constructor initializes the factory attribute
     */
    public OptionController(){
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Option.class).buildSessionFactory();
    }

    /**
     * Method to get all the options of a question
     * @param questionId the id of the question
     * @return a list of options
     */
    public List<Option> getAllOptionsByQuestionId(Long questionId){
        try (Session session = factory.openSession()) {
            Query<Option> query = session.createQuery("from Option where question_id = :questionId", Option.class);
            query.setParameter("questionId", questionId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to get an option by its id
     * @param optionId the id of the option
     * @return the option
     */
    public Option getOptionById(Long optionId){
        try (Session session = factory.openSession()) {
            Query<Option> query = session.createQuery("from Option where id = :optionId", Option.class);
            query.setParameter("optionId", optionId);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to create an option
     * @param option the option to create
     */
    public void createOption(Option option) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(option);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Method to delete an option
     * @param option the option to delete
     */
    public void updateOption(Option option){
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.update(option);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the Hibernate session factory.
     */
    public void close() {
        if (factory != null && !factory.isClosed()) {
            factory.close();
        }
    }
}

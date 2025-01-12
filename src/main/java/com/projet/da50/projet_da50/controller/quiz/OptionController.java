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
 */
public class OptionController {
    private SessionFactory factory;

    /**
     * Constructor
     */
    public OptionController(){
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Option.class).buildSessionFactory();
    }

    /**
     * Get all options by question id
     * @param questionId the question id
     * @return the list of options
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
     * Get an option by its id
     * @param optionId the option id
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
     * Create an option
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
     * Delete an option
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
     * Delete an option
     */
    public void close() {
        if (factory != null && !factory.isClosed()) {
            factory.close();
        }
    }
}

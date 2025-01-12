package com.projet.da50.projet_da50.controller.quiz;

import com.projet.da50.projet_da50.HibernateUtil;
import com.projet.da50.projet_da50.model.quiz.Question;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Controller for Question entity
 * Contains methods to interact with the database
 */
public class QuestionController {

    private final SessionFactory factory;

    /**
     * Constructor for QuestionController
     * Initializes the factory
     */
    public QuestionController(){
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Question.class).buildSessionFactory();
    }

    /**
     * Get all questions of a quiz from the database
     * @param quizId Id of the quiz
     * @return List of all questions
     */
    public List<Question> getAllQuestionsByQuizId(Long quizId){
        try (Session session = factory.openSession()) {
            Query<Question> query = session.createQuery("from Question where quiz_id = :quizId", Question.class);
            query.setParameter("quizId", quizId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a question by its id
     * @param questionId Id of the question
     * @return Question object
     */
    public Question getQuestionById(Long questionId){
        try (Session session = factory.openSession()) {
            Query<Question> query = session.createQuery("from Question where id = :questionId", Question.class);
            query.setParameter("questionId", questionId);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create a question in the database
     * @param question Question object
     */
    public void createQuestion(Question question) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(question);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
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

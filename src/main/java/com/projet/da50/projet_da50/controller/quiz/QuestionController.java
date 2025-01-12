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
 * Controller for the Question entity
 */
public class QuestionController {

    private SessionFactory factory;

    /**
     * Constructor
     */
    public QuestionController(){
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Question.class).buildSessionFactory();
    }

    /**
     * Get all questions by quiz id
     * @param quizId the quiz id
     * @return the list of questions
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
     * @param questionId the question id
     * @return the question
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
     * Create a question
     * @param question the question to create
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


}

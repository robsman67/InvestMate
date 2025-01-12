package com.projet.da50.projet_da50.controller.quiz;

import com.projet.da50.projet_da50.HibernateUtil;
import com.projet.da50.projet_da50.model.quiz.Question;
import com.projet.da50.projet_da50.model.quiz.Quiz;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Controller for the Quiz entity
 */
public class QuizController {
    private SessionFactory factory;

    /**
     * Constructor
     */
    public QuizController(){
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Quiz.class).buildSessionFactory();
    }

    /**
     * Create a quiz
     * @param title the title of the quiz
     * @param hasMultipleAnswers if the quiz has multiple answers
     * @param questions the list of questions
     */
    public void createQuiz(String title, boolean hasMultipleAnswers, List<Question> questions){
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setHasMultipleAnswers(hasMultipleAnswers);
        quiz.setQuestions(questions);
        for (Question question : questions) {
            question.setQuiz(quiz);
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(quiz);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Get all quizzes
     * @return the list of quizzes
     */
    public List<Quiz> getAllQuizzes(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Quiz> query = session.createQuery("from Quiz", Quiz.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a quiz by its id
     * @param quizId the quiz id
     * @return the quiz
     */
    public Quiz getQuizById(Long quizId){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Quiz> query = session.createQuery("from Quiz where id = :quizId", Quiz.class);
            query.setParameter("quizId", quizId);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Update a quiz
     * @param quiz the quiz to update
     * @param newQuestions the list of new questions
     */
    public void updateQuiz(Quiz quiz, List<Question> newQuestions) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Load the existing quiz from the database
            Quiz existingQuiz = session.get(Quiz.class, quiz.getId());
            if (existingQuiz == null) {
                throw new IllegalArgumentException("Quiz not found");
            }

            // Update the quiz fields
            existingQuiz.setTitle(quiz.getTitle());
            existingQuiz.setHasMultipleAnswers(quiz.getHasMultipleAnswers());

            // Update the existing questions collection in-place
            List<Question> existingQuestions = existingQuiz.getQuestions();

            // Remove questions that are no longer needed
            existingQuestions.removeIf(question -> !newQuestions.contains(question));

            // Add new questions
            for (Question question : newQuestions) {
                if (!existingQuestions.contains(question)) {
                    question.setQuiz(existingQuiz); // Set the relationship
                    existingQuestions.add(question);
                }
            }

            // Save the updated quiz
            session.update(existingQuiz);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a quiz by its id
     * @param quizId the quiz id
     */
    public void deleteQuiz(Long quizId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Retrieve the quiz by ID
            Quiz quiz = session.get(Quiz.class, quizId);

            if (quiz != null) {
                session.delete(quiz);  // Delete the quiz, elements are deleted due to cascade and orphan removal.
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Find quizzes by title
     * @param title the title of the quiz
     * @return the list of quizzes
     */
    public List<Quiz> findQuizzesByTitle(String title) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Quiz> query = session.createQuery("from Quiz where lower(title) like :title", Quiz.class);
            query.setParameter("title", "%" + title.toLowerCase() + "%");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * Close the session factory
     */
    public void close() {
        if (factory != null && !factory.isClosed()) {
            factory.close();
        }
    }
}
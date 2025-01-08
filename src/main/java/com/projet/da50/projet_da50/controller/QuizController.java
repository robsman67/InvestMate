package com.projet.da50.projet_da50.controller;

import com.projet.da50.projet_da50.HibernateUtil;
import com.projet.da50.projet_da50.model.Question;
import com.projet.da50.projet_da50.model.Quiz;
import com.projet.da50.projet_da50.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class QuizController {
    private SessionFactory factory;
    public QuizController(){
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Quiz.class).buildSessionFactory();
    }
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

    public List<Quiz> getAllQuizzes(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Quiz> query = session.createQuery("from Quiz", Quiz.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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



    // Close the SessionFactory when the application is closed
    public void close() {
        if (factory != null && !factory.isClosed()) {
            factory.close();
        }
    }
}
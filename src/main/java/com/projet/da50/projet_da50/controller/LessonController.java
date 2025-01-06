package com.projet.da50.projet_da50.controller;

import com.projet.da50.projet_da50.HibernateUtil;
import com.projet.da50.projet_da50.model.*;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LessonController {

    private Lesson lesson;

    private SessionFactory factory;

    public LessonController() {
        this.lesson = new Lesson();
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Lesson.class).buildSessionFactory();
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    /**
     * Valide les entrées pour une leçon.
     * @param lessonTitle Titre de la leçon (doit être non vide)
     * @return `true` si les entrées sont valides, `false` sinon
     */
    public boolean validateInputs(String lessonTitle) {
        if (lessonTitle == null || lessonTitle.isEmpty()) {
            showAlert("Validation Error", "Le titre de la leçon ne peut pas être vide.");
            return false;
        }
        if (lessonTitle.length() > 100){
            showAlert("Validation Error", "Le titre de la leçon ne peut dépasser 100 caractères. Il est trop long de " + (lessonTitle.length() - 100) + " caractères.");
            return false;
        }

        // Ajout d'autres règles de validation si nécessaire
        return true;
    }

    /**
     * Crée une leçon avec les entrées validées.
     * @param lessonTitle Titre de la leçon
     * @return Une instance de `Lesson`
     */
    public Lesson createMainTitle(String lessonTitle) {

        lesson.setTitle(lessonTitle);

        return lesson;
    }

    public Lesson setTag(Tags tag) {

        lesson.setTag(tag);

        return lesson;
    }

    public Lesson createSubTitle(String content, TitleType type){
        Title title = new Title();
        title.setContent(content);
        title.setType(type);

        lesson.addElement(title);

        return lesson;

    }

    public Lesson createParagraph(String content, ParagraphType type){
        Paragraph paragraph = new Paragraph();
        paragraph.setContent(content);
        paragraph.setType(type);

        lesson.addElement(paragraph);

        return lesson;
    }

    public Lesson createImage(String url) throws IOException {

        byte[] imageBytes = readImageAsBytes(url);

        PictureIntegration image = new PictureIntegration();
        image.setContentPath(url);
        image.setImageData(imageBytes);

        lesson.addElement(image);

        return lesson;
    }

    public byte[] readImageAsBytes(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }

    public Lesson createVideo(String url){
        VideoIntegration video = new VideoIntegration();
        video.setContentPath(url);

        lesson.addElement(video);

        return lesson;
    }

    public void removeElement(Elements element) {
        lesson.removeElement(element);
    }

    /**
     * Affiche une alerte d'erreur ou d'information.
     * @param title Titre de l'alerte
     * @param message Message de l'alerte
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean swapElements(List<Elements> lesson, int index1, int index2) {
        // Vérifier que les indices sont valides
        if (index1 >= 0 && index1 < lesson.size() && index2 >= 0 && index2 < lesson.size()) {
            // Sauvegarder l'élément à l'indice index1 dans une variable temporaire
            Elements temp = lesson.get(index1);

            // Échanger les éléments
            lesson.set(index1, lesson.get(index2));
            lesson.set(index2, temp);

            return true;  // Indiquer que l'échange a réussi
        } else {
            System.out.println("Indices invalides !");
            return false;  // Indiquer que l'échange a échoué en raison d'indices invalides
        }
    }

    public List<Lesson> findLessonContainsTitle(String title) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Lesson> query = session.createQuery("from Lesson where title like :title", Lesson.class);
            query.setParameter("title", "%" + title + "%");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Lesson getLessonByTitle(String title) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Lesson> query = session.createQuery("from Lesson where title = :title", Lesson.class);
            query.setParameter("title", title);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Lesson> findLessonByTags(Tags tag) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Lesson> query = session.createQuery("from Lesson where tag = :tag", Lesson.class);
            query.setParameter("tag", tag);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Lesson> findLessonByTitleAndTags(String title, Tags tag) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Lesson> query = session.createQuery("from Lesson where title like :title and tag = :tag", Lesson.class);
            query.setParameter("title", "%" + title + "%");
            query.setParameter("tag", tag);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Lesson> findLesson(String title, Tags tag) {
       if (title == null && tag == Tags.ALL) {
           return getAllLessons();
       } else if (title != null && tag == Tags.ALL) {
           return findLessonContainsTitle(title);
       } else if (title == null && tag != Tags.ALL) {
           return findLessonByTags(tag);
       } else {
           return findLessonByTitleAndTags(title, tag);
       }
    }

    public List<Lesson> getAllLessons() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Lesson> query = session.createQuery("from Lesson", Lesson.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer createLesson() {
        // Declare the transaction and session outside of the try-with-resources block
        Session session = null;
        Transaction transaction = null;
        try {
            // Open the session and begin the transaction manually
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Check if the lesson already exists
            System.out.println("List of lessons with title: " + getLessonByTitle(lesson.getTitle()));
            if (getLessonByTitle(lesson.getTitle()) != null) {
                System.err.println("This lesson already exists: " + lesson.getTitle());
                return null;
            }

            System.out.println("Creating lesson: " + lesson.getTitle());

            // Save the lesson
            Integer id = (Integer) session.save(lesson);

            // Commit the transaction if everything is fine
            transaction.commit();
            System.out.println("Lesson created with ID: " + id);
            return id;
        } catch (Exception e) {
            // Rollback the transaction if there was an error
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    // Close the SessionFactory when the application is closed
    public void close() {
        if (factory != null && !factory.isClosed()) {
            factory.close();
        }
    }

}

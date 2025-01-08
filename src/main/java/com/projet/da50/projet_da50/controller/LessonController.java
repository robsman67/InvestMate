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

import static com.projet.da50.projet_da50.controller.TokenManager.getIdToken;

/**
 * The LessonController class manages the creation, validation, and retrieval of Lesson objects.
 * It interacts with the Hibernate ORM to store and retrieve lesson data from the database.
 */
public class LessonController {

    private Lesson lesson;
    private final SessionFactory factory;
    private final LogController logController = new LogController();

    /**
     * Default constructor, initializing a new Lesson and configuring Hibernate SessionFactory.
     */
    public LessonController() {
        this.lesson = new Lesson();
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Lesson.class).buildSessionFactory();
    }

    /**
     * Constructor with an existing Lesson to be managed by this controller.
     * @param lesson The existing Lesson instance.
     */
    public LessonController(Lesson lesson) {
        this.lesson = lesson;
        factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Lesson.class).buildSessionFactory();
    }

    /** Getter and Setter for the lesson attribute. */

    public Lesson getLesson() {
        return lesson;
    }


    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    /**
     * Validates the inputs for creating a lesson.
     * @param lessonTitle The title of the lesson to validate.
     * @return true if the inputs are valid, false otherwise.
     */
    public boolean validateInputs(String lessonTitle) {

        if (lessonTitle == null || lessonTitle.isEmpty()) {
            showAlert("Validation Error", "The lesson title cannot be empty.");
            return false;
        }
        if (lessonTitle.length() > 100){
            showAlert("Validation Error", "The lesson title cannot exceed 100 characters. It is " + (lessonTitle.length() - 100) + " characters too long.");
            return false;
        }

        logController.createLog(getIdToken(), "Lesson Creation", "Lesson title: " + lessonTitle);
        // Additional validation rules can be added here as needed.
        return true;
    }

    /**
     * Creates and sets the main title for the lesson.
     * @param lessonTitle The title of the lesson.
     * @return The updated Lesson object.
     */
    public Lesson createMainTitle(String lessonTitle) {
        lesson.setTitle(lessonTitle);
        return lesson;
    }

    /**
     * Sets a tag for the lesson.
     * @param tag The tag to assign to the lesson.
     * @return The updated Lesson object.
     */
    public Lesson setTag(Tags tag) {
        lesson.setTag(tag);
        return lesson;
    }

    /**
     * Creates and adds a subtitle to the lesson.
     * @param content The content of the subtitle.
     * @param type The type of the subtitle.
     * @return The updated Lesson object.
     */
    public Lesson createSubTitle(String content, TitleType type) {
        Title title = new Title();
        title.setContent(content);
        title.setType(type);
        title.setPosition(lesson.getElements().size());

        lesson.addElement(title);
        return lesson;
    }

    /**
     * Creates and adds a paragraph to the lesson.
     * @param content The content of the paragraph.
     * @param type The type of the paragraph.
     * @return The updated Lesson object.
     */
    public Lesson createParagraph(String content, ParagraphType type) {
        Paragraph paragraph = new Paragraph();
        paragraph.setContent(content);
        paragraph.setType(type);
        paragraph.setPosition(lesson.getElements().size());

        lesson.addElement(paragraph);
        return lesson;
    }

    /**
     * Creates and adds an image to the lesson.
     * @param url The URL where the image is located.
     * @return The updated Lesson object.
     * @throws IOException If there is an error reading the image file.
     */
    public Lesson createImage(String url) throws IOException {
        byte[] imageBytes = readAsBytes(url);

        PictureIntegration image = new PictureIntegration();
        image.setContentPath(url);
        image.setImageData(imageBytes);
        image.setPosition(lesson.getElements().size());

        lesson.addElement(image);
        return lesson;
    }

    /**
     * Creates and adds a video to the lesson.
     * @param url The URL where the video is located.
     * @return The updated Lesson object.
     * @throws IOException If there is an error reading the video file.
     */
    public Lesson createVideo(String url) throws IOException {
        byte[] videoBytes = readAsBytes(url);

        VideoIntegration video = new VideoIntegration();
        video.setContentPath(url);
        video.setVideoData(videoBytes);
        video.setPosition(lesson.getElements().size());

        lesson.addElement(video);
        return lesson;
    }

    /**
     * Reads a file as bytes from a given URL.
     * @param url The URL of the file to read.
     * @return A byte array representing the file's contents.
     * @throws IOException If there is an error reading the file.
     */
    public static byte[] readAsBytes(String url) throws IOException {
        Path path = Paths.get(url);
        return Files.readAllBytes(path);
    }

    /**
     * Removes an element from the lesson.
     * @param element The element to remove.
     */
    public void removeElement(Elements element) {
        lesson.removeElement(element);
    }

    /**
     * Displays an alert with the given title and message.
     * @param title The title of the alert.
     * @param message The message to display in the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Swaps two elements in the lesson's list of elements.
     * @param lesson The list of elements in the lesson.
     * @param index1 The index of the first element.
     * @param index2 The index of the second element.
     * @return true if the swap was successful, false otherwise.
     */
    public boolean swapElements(List<Elements> lesson, int index1, int index2, Boolean isUpdate) {
        // Validate the indices
        if (index1 >= 0 && index1 < lesson.size() && index2 >= 0 && index2 < lesson.size()) {
            // Swap elements in the list
            Elements element1 = lesson.get(index1);
            Elements element2 = lesson.get(index2);

            // Perform the swap in the list
            lesson.set(index1, element2);
            lesson.set(index2, element1);

            // Swap positions (the new position is based on the index in the list)
            element1.setPosition(index2);
            element2.setPosition(index1);

            // Update the IDs in the database using Hibernate
            if (isUpdate) {
                Session session = null;
                try {
                    session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();

                    // Make sure the elements are in a persistent state before updating
                    session.merge(element1);  // Re-attaches element1 if it's detached
                    session.merge(element2);  // Re-attaches element2 if it's detached

                    // Update the elements' positions in the database
                    session.update(element1);
                    session.update(element2);


                    session.getTransaction().commit();
                    return true;  // Swap and database update successful
                } catch (Exception e) {
                    session.getTransaction().rollback();
                    System.out.println("Error during database update: " + e.getMessage());
                    return false;  // Swap failed due to database error
                }
            }
            return true;  // Swap successful
        } else {
            System.out.println("Invalid indices!");
            return false;  // Swap failed due to invalid indices
        }
    }

    /**
     * Finds lessons that contain the given title.
     * @param title The title to search for.
     * @return A list of lessons that contain the given title.
     */
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

    /**
     * Retrieves a lesson by its exact title.
     * @param title The title of the lesson.
     * @return The lesson with the given title, or null if not found.
     */
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

    /**
     * Finds lessons by the given tag.
     * @param tag The tag to search for.
     * @return A list of lessons that have the given tag.
     */
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

    /**
     * Finds lessons that match both a title and a tag.
     * @param title The title to search for.
     * @param tag The tag to search for.
     * @return A list of lessons that match both the title and the tag.
     */
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

    /**
     * Finds lessons based on a title and a tag, with fallback conditions.
     * @param title The title to search for.
     * @param tag The tag to search for.
     * @return A list of lessons matching the search criteria.
     */
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

    /**
     * Retrieves all lessons from the database.
     * @return A list of all lessons.
     */
    public List<Lesson> getAllLessons() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Lesson> query = session.createQuery("from Lesson", Lesson.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates and saves a new lesson in the database.
     * @return The ID of the created lesson, or null if creation failed.
     */
    public Integer createLesson() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            if (getLessonByTitle(lesson.getTitle()) != null) {
                System.err.println("This lesson already exists: " + lesson.getTitle());
                return null;
            }

            Integer id = (Integer) session.save(lesson);
            transaction.commit();
            return id;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the current lesson in the database.
     */
    public void updateLesson() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(lesson);
            session.getTransaction().commit();
            logController.createLog(getIdToken(), "Update Lesson", "Lesson id: " + lesson.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Deletes a lesson from the database.
     * @param lessonId The ID of the lesson to delete.
     */
    public void deleteLesson(int lessonId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Retrieve the lesson by ID
            Lesson lesson = session.get(Lesson.class, lessonId);

            if (lesson != null) {
                // Lesson exists, so proceed with deletion.
                logController.createLog(getIdToken(), "Delete Lesson", "Lesson id: " + lesson.getId());
                session.delete(lesson);  // Delete the lesson, elements are deleted due to cascade and orphan removal.
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the Hibernate SessionFactory.
     */
    public void close() {
        if (factory != null && !factory.isClosed()) {
            factory.close();
        }
    }

}
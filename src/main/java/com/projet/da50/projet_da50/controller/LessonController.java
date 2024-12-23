package com.projet.da50.projet_da50.controller;

import com.projet.da50.projet_da50.model.*;
import javafx.scene.control.Alert;

public class LessonController {

    private Lesson lesson;

    private Integer index = 0;

    public LessonController() {
        this.lesson = new Lesson();
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

        // Ajout d'autres règles de validation si nécessaire
        return true;
    }

    /**
     * Crée une leçon avec les entrées validées.
     * @param lessonTitle Titre de la leçon
     * @return Une instance de `Lesson`
     */
    public Lesson createMainTitle(String lessonTitle) {

        // Ajouter un élément "Title"
        Title title = new Title();
        title.setContent(lessonTitle);
        title.setType(TitleType.MainTitle);
        lesson.addElement(String.valueOf(title.getType()), title);

        return lesson;
    }

    public Lesson createSubTitle(String content, TitleType type){
        Title title = new Title();
        title.setContent(content);
        title.setType(type);

        String id = "Content" + index++;

        lesson.addElement(id, title);

        return lesson;

    }

    public Lesson createParagraph(String content, ParagraphType type){
        Paragraph paragraph = new Paragraph();
        paragraph.setContent(content);
        paragraph.setType(type);

        String id = "Content" + index++;

        lesson.addElement(id, paragraph);

        return lesson;
    }

    public Lesson createImage(String url){
        PictureIntegration image = new PictureIntegration();
        image.setContentPath(url);

        String id = "Content" + index++;

        lesson.addElement(id, image);

        return lesson;
    }

    public Lesson createVideo(String url){
        VideoIntegration video = new VideoIntegration();
        video.setContentPath(url);

        String id = "Content" + index++;

        lesson.addElement(id, video);

        return lesson;
    }

    public void removeElement(Elements element) {
        lesson.removeElement(element);
    }

    public void saveLesson() {
        System.out.println("saveLesson");
        System.out.println(lesson.toString());
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
}

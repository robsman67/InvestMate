package com.projet.da50.projet_da50.model.quiz;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Quiz entity
 */
@Entity
@Table(name = "Quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="title")
    private String title;

    @Column(name="hasMultipleAnswers")
    private boolean hasMultipleAnswers;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    // Constructors
    public Quiz() {
        //
    }

    /**
     * Constructor
     * @return the title of the quiz
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the title
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get if the quiz has multiple answers
     * @return if the quiz has multiple answers
     */
    public boolean getHasMultipleAnswers(){
        return hasMultipleAnswers;
    }

    /**
     * Set if the quiz has multiple answers
     * @param hasMultipleAnswers if the quiz has multiple answers
     */
    public void setHasMultipleAnswers(boolean hasMultipleAnswers){
        this.hasMultipleAnswers = hasMultipleAnswers;
    }

    /**
     * Set the questions
     * @param questions the questions
     */
    public void setQuestions(List<Question> questions){
        this.questions = questions;
    }

    /**
     * Get the questions
     * @return the questions
     */
    public List<Question> getQuestions() {
        return questions;
    }

}

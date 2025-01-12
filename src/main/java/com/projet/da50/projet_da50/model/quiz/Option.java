package com.projet.da50.projet_da50.model.quiz;

import javax.persistence.*;

/**
 * Option entity
 */
@Entity
@Table(name = "Options")
public class Option {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "isCorrect")
    private boolean isCorrect;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    /**
     * Constructor
     * @param content the content of the option
     * @param isCorrect if the option is correct
     */
    public Option(String content, boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    /**
     * Constructor
     */
    public Option(){

    }

    /**
     * Get the id
     * @return the id
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
     * Get if the option is correct
     * @return if the option is correct
     */
    public boolean isCorrect() {
        return isCorrect;
    }

    /**
     * Set if the option is correct
     * @param correct if the option is correct
     */
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    /**
     * Get the content
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the question
     * @return the question
     */
    public Question getQuestion(){
        return this.question;
    }

    /**
     * Set the question
     * @param question the question
     */
    public void setQuestion(Question question){
        this.question = question;
    }
}

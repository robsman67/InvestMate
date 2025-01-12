package com.projet.da50.projet_da50.model.quiz;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Question entity
 */
@Entity
@Table(name = "Questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "question")
    private String question;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="quiz_id")
    private Quiz quiz;

    /**
     * Constructor
     * @param question the question
     * @param options the list of options
     */
    public Question(String question, List<Option> options) {
        this.question = question;
        this.options = options;
        for (Option option : options) {
            option.setQuestion(this);
        }
    }

    /**
     * Constructor
     */
    public Question() {

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
     * Get the question
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Set the question
     * @param question the question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Get the options
     * @return the options
     */
    public List<Option> getOptions() {
        return options;
    }

    /**
     * Set the options
     * @param option the options
     */
    public void addOptions(Option option) {
        this.options.add(option);
    }

    /**
     * Remove an option
     * @param option the option to remove
     */
    public void removeOption(Option option) {
        this.options.remove(option);
    }

    /**
     * Get the quiz
     * @return the quiz
     */
    public Quiz getQuiz() {
        return quiz;
    }

    /**
     * Set the quiz
     * @param quiz the quiz
     */
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}

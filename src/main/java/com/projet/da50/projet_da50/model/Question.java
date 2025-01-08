package com.projet.da50.projet_da50.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public Question(String question, List<Option> options) {
        this.question = question;
        this.options = options;
        for (Option option : options) {
            option.setQuestion(this);
        }
    }

    public Question() {

    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void addOptions(Option option) {
        this.options.add(option);
    }

    public void removeOption(Option option) {
        this.options.remove(option);
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}

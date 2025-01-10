package com.projet.da50.projet_da50.model.quiz;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getHasMultipleAnswers(){
        return hasMultipleAnswers;
    }

    public void setHasMultipleAnswers(boolean hasMultipleAnswers){
        this.hasMultipleAnswers = hasMultipleAnswers;
    }

    public void setQuestions(List<Question> questions){
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

}

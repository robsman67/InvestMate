package com.projet.da50.projet_da50.model.quiz;

import javax.persistence.*;

@Entity
@Table(name = "Options")
public class Option {
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

    // Constructors
    public Option(String content, boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public Option(){

    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Question getQuestion(){
        return this.question;
    }
    public void setQuestion(Question question){
        this.question = question;
    }
}

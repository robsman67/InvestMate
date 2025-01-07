package com.projet.da50.projet_da50.model;

import com.sun.jna.platform.win32.Sspi;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Logs") // Name of the table in the database
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "action", nullable = false)
    private String action;

//    @Column(name = "timestamp", nullable = false)
//    private Timestamp timestamp;


    public Log() {
    }

    public Log(Long userId, String action) {
        this.userId = userId;
        this.action = action;
       // this.timestamp = new Timestamp(System.currentTimeMillis());
    }


    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

//    public Timestamp getTimestamp() {
//        return timestamp;
//    }



}

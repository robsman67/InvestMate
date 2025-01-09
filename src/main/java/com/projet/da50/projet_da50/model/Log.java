package com.projet.da50.projet_da50.model;

import com.sun.jna.platform.win32.Sspi;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Logs") // Name of the table in the database
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "detail")
    private String detail;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;


    /**
     * Default constructor for Log. For hibernate.
     */
    public Log() {
    }

    /**
     * Constructor with parameters for Log.
     *
     * @param userId The id of the user.
     * @param action The action performed.
     * @param detail The detail of the action.
     */
    public Log(Long userId, String action, String detail) {
        this.userId = userId;
        this.action = action;
        this.detail = detail;
        timestamp = new Timestamp(System.currentTimeMillis());
    }


    /**
     * Getters id
     */
    public Long getId() {
        return id;
    }


}

package com.projet.da50.projet_da50.model;

import javax.persistence.*; // Changed import from javax.persistence to jakarta.persistence

@Entity
@Table(name = "Users") // Nom de la table dans la base de données
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation
    @Column(name = "id")
    private Long id;

    //Must be unique
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    //Must be unique
    @Column(name = "mail", unique= true, nullable = false)
    private String mail;

    //@Enumerated(EnumType.STRING)  // Added annotation for enum mapping to the database
    @Column(name = "role", nullable = false)
    private Role role;

    // Constructeur par défaut
    public User() {}

    // Constructeur avec paramètres
    public User(String username, String password, String mail) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.role = Role.Reader; // Default role
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

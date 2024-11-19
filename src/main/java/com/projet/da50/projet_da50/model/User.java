package com.projet.da50.projet_da50.model;

import javax.persistence.*; // Changed import from javax.persistence to jakarta.persistence

@Entity
@Table(name = "Users") // Nom de la table dans la base de données
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)  // Added annotation for enum mapping to the database
    @Column(name = "role", nullable = false)
    private Role role;

    // Constructeur par défaut
    public User() {}

    // Constructeur avec paramètres
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = Role.Reader; // Par défaut, un nouvel utilisateur est un lecteur
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

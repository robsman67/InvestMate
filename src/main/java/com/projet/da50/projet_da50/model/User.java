package com.projet.da50.projet_da50.model;

import javax.persistence.*;

/**
 * This class represents a User entity mapped to the "Users" table in the database.
 */
@Entity
@Table(name = "Users") // Name of the table in the database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    @Column(name = "id")
    private Long id;

    // Must be unique
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    // Must be unique
    @Column(name = "mail", unique= true, nullable = false)
    private String mail;

    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * Default constructor for User.
     */
    public User() {}

    /**
     * Constructor with parameters for User.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param mail The email of the user.
     */
    public User(String username, String password, String mail) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.role = Role.Reader; // Default role
    }

    // Getters and setters

    /**
     * Gets the ID of the user.
     *
     * @return The ID of the user.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the user.
     *
     * @param id The ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email of the user.
     *
     * @return The email of the user.
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets the email of the user.
     *
     * @param mail The email to set.
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Gets the role of the user.
     *
     * @return The role of the user.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role The role to set.
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Returns a string representation of the user.
     *
     * @return A string representation of the user.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                ", role=" + role +
                '}';
    }
}
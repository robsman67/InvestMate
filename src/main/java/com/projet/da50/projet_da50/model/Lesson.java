package com.projet.da50.projet_da50.model;

import javax.persistence.*;
import java.util.*;

/**
 * Represents a lesson composed of a title, a list of elements (e.g., text, image, video),
 * and a tag that categorizes the lesson.
 *
 * The lesson is associated with a specific tag from the Tags enum.
 */
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID for the lesson
    @Column(name = "id")
    private Integer id;

    @Column(name = "title", unique = true, nullable = false) // Title must be unique and not null
    private String title;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id")
    private List<Elements> elements = new ArrayList<>(); // List of elements in the lesson (text, images, videos)

    @Enumerated(EnumType.STRING)
    @Column(name = "tag", nullable = false) // Tag to categorize the lesson
    private Tags tag;

    // Default constructor
    public Lesson() {
    }

    // Getters and setters

    /**
     * Gets the title of the lesson.
     *
     * @return The title of the lesson.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the lesson.
     *
     * @param title The title to be set for the lesson.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the ID of the lesson.
     *
     * @return The ID of the lesson.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the lesson.
     *
     * @param id The ID to be set for the lesson.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the list of elements in the lesson.
     *
     * @return The list of elements.
     */
    public List<Elements> getElements() {
        return elements;
    }

    /**
     * Sets the list of elements in the lesson.
     *
     * @param elements The list of elements to be set.
     */
    public void setElements(List<Elements> elements) {
        this.elements = elements;
    }

    /**
     * Gets the tag of the lesson.
     *
     * @return The tag of the lesson.
     */
    public Tags getTag() {
        return tag;
    }

    /**
     * Sets the tag of the lesson.
     *
     * @param tag The tag to be set for the lesson.
     */
    public void setTag(Tags tag) {
        this.tag = tag;
    }

    /**
     * Adds an element to the lesson.
     *
     * @param element The element to be added.
     */
    public void addElement(Elements element) {
        this.elements.add(element);
    }

    /**
     * Removes an element from the lesson.
     * Searches through the list of elements and removes the element if found.
     *
     * @param value The element to be removed.
     * @return `true` if the element was removed, `false` otherwise.
     */
    public boolean removeElement(Elements value) {
        // Iterate through the list and find the element to remove
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).equals(value)) {
                elements.remove(i);  // Remove the element at index i
                return true; // Element removed successfully
            }
        }
        return false;  // Element not found
    }

    /**
     * Returns a string representation of the lesson object.
     *
     * @return A string that represents the lesson, including its ID, elements, and tag.
     */
    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", elements=" + elements +
                ", tag=" + tag +
                '}';
    }
}

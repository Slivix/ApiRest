package com.entreprise.tutorialdemoformation.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tutorials")
public class Tutorial {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    public Tutorial() {
    }

    public Tutorial(Long id, String title, String description, boolean publish) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publish = publish;
    }

    public Tutorial(String title, String description, boolean publish) {
        this.title = title;
        this.description = description;
        this.publish = publish;
    }

    public Tutorial(String title, String description) {
        this.title = title;
        this.description = description;
        this.publish = false;
    }

    @Column(name = "description")
    private String description;

    @Column(name = "publish")
    private boolean publish;

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPublish() {
        return publish;
    }
}
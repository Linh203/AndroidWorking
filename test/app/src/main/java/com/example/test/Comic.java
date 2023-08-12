package com.example.test;

import java.io.Serializable;

public class Comic implements Serializable {
    private String id;
    private String title;
    private String content;
    private String end_date;
    private int status;
    private String image;
    private String createdAt;

    public Comic() {
    }

    public Comic(String id, String title, String content, String end_date, int status, String image, String createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.end_date = end_date;
        this.status = status;
        this.image = image;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", end_date='" + end_date + '\'' +
                ", status='" + status + '\'' +
                ", image='" + image + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

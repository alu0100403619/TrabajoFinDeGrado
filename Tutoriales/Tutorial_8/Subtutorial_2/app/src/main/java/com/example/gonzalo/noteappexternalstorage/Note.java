package com.example.gonzalo.noteappexternalstorage;

/**
 * Created by Gonzalo on 27/01/2015.
 */
public class Note {

    private String id;
    private String title;
    private String content;

    public Note(String id, String title, String content) {
        setId(id);
        setTitle(title);
        setContent(content);
    }

    public Note(String title, String content) {
        setTitle(title);
        setContent(content);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

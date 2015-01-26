package com.example.gonzalo.noteapp;

/**
 * Created by Gonzalo on 23/01/2015.
 */
public class Note {

    private String content;
    private String id;
    private String title;

    public Note (String noteId, String noteTitle, String noteContent) {
        id = noteId;
        title = noteTitle;
        content = noteContent;
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

    @Override
    public String toString() {
        return this.getTitle();
    }

}

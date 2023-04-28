package com.dovepay.doveEditor.entity;

import java.sql.Timestamp;

public class DeeeAtclList {
    private String id;
    private String title;
    private String author;
    private String editor;
    private Timestamp timeCreate;
    private Timestamp timeModify;

    public DeeeAtclList() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Timestamp getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Timestamp timeCreate) {
        this.timeCreate = timeCreate;
    }

    public Timestamp getTimeModify() {
        return timeModify;
    }

    public void setTimeModify(Timestamp timeModify) {
        this.timeModify = timeModify;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

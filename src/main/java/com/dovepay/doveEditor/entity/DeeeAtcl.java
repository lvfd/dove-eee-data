package com.dovepay.doveEditor.entity;

import java.sql.Timestamp;

public class DeeeAtcl {
    private String title;
    private String author;
    private String editor;
    private String version;
    private String id;
    private String content;
    private Timestamp timeCreate;
    private Timestamp timeModify;

    public DeeeAtcl(String title, String author, String editor, String version, String id, String content, Timestamp timeCreate, Timestamp timeModify) {
        this.title = title;
        this.author = author;
        this.editor = editor;
        this.version = version;
        this.id = id;
        this.content = content;
        this.timeCreate = timeCreate;
        this.timeModify = timeModify;
    }

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}

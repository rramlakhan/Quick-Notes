package com.example.quicknotes;

public class Note {
    private String id;
    private String heading;
    private String content;
    private String userId;

    public Note (String id, String heading, String content, String userId) {
        this.id = id;
        this.heading = heading;
        this.content = content;
        this.userId = userId;
    }
    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

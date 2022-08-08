package com.ambitious.fghdoctor.Model;

public class Noti {

    String id, title,message, image,time_Ago;

    public Noti(String id, String title, String message, String image, String time_Ago) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.image = image;
        this.time_Ago = time_Ago;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime_Ago() {
        return time_Ago;
    }

    public void setTime_Ago(String time_Ago) {
        this.time_Ago = time_Ago;
    }
}

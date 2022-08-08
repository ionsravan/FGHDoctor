package com.ambitious.fghdoctor.Model;

public class Winners {

    String uid, name, mobile, city, user_image, video, video_title;

    public Winners(String uid, String name, String mobile, String city, String user_image, String video, String video_title) {
        this.uid = uid;
        this.name = name;
        this.mobile = mobile;
        this.city = city;
        this.user_image = user_image;
        this.video = video;
        this.video_title = video_title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }
}

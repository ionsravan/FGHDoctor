package com.ambitious.fghdoctor.Model;

public class OxygenBank {

    String id, user_type, name, address, user_image, available, rating, obj;

    public OxygenBank() {
    }

    public OxygenBank(String id, String user_type, String name, String address, String user_image, String available, String rating, String obj) {
        this.id = id;
        this.user_type = user_type;
        this.name = name;
        this.address = address;
        this.user_image = user_image;
        this.available = available;
        this.rating = rating;
        this.obj = obj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }
}

package com.ambitious.fghdoctor.Model;

public class Category {

    String name,id,img;
    int image;
    boolean is_Sel;

    public Category(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isIs_Sel() {
        return is_Sel;
    }

    public void setIs_Sel(boolean is_Sel) {
        this.is_Sel = is_Sel;
    }
}

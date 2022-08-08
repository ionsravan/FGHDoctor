package com.ambitious.fghdoctor.Model;

import android.graphics.Bitmap;

public class MarketProduct {
    Bitmap img;
    String pName,price,weight,imgs,pos;
    Boolean isChecked = false;


    public MarketProduct() {
    }

    public MarketProduct(Bitmap img, String pName, String price, String weight, String imgs) {
        this.img = img;
        this.pName = pName;
        this.price = price;
        this.weight = weight;
        this.imgs = imgs;
      //  this.pos = pos;
    }

    public MarketProduct(Bitmap img, String pName, String price, String weight, Boolean isChecked) {
        this.img = img;
        this.pName = pName;
        this.price = price;
        this.weight = weight;
        this.isChecked = isChecked;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}

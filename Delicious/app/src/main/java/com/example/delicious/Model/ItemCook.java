package com.example.delicious.Model;

public class ItemCook {
    int img, icon;
    String text;

    public ItemCook(int img, int icon, String text) {
        this.img = img;
        this.icon = icon;
        this.text = text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String  getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

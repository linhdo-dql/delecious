package com.example.delicious.Model;

public class ItemDropDown {
    int iconsrc;
    String text;

    public int getIconsrc() {
        return iconsrc;
    }

    public void setIconsrc(int iconsrc) {
        this.iconsrc = iconsrc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ItemDropDown(int iconsrc, String text) {
        this.iconsrc = iconsrc;
        this.text = text;
    }
}

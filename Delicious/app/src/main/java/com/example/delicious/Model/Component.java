package com.example.delicious.Model;

public class Component {
    String number;
    String name, determine;
    boolean check;

    public Component(String number, String name, String determine, boolean check) {
        this.number = number;
        this.name = name;
        this.determine = determine;
        this.check = check;
    }

    public String  getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetermine() {
        return determine;
    }

    public void setDetermine(String determine) {
        this.determine = determine;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}

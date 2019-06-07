package com.aimuhire.onlinecontactlist;

public class Contact {

    String name;
    String image;
    String phone;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Contact(String name, String phone,int id) {
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPhone() {
        return phone;
    }
}



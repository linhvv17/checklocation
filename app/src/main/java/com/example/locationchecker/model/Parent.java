package com.example.locationchecker.model;

public class Parent {
    private String id;
    private String email;
    private String name;
    private String phone;
    private String image;
    private String code;
    public  Parent(){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Parent(String id,String email, String name, String phone, String image, String code) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.code = code;
    }
}

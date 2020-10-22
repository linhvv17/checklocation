package com.example.locationchecker.model;

public class Kid {
    private String id;
    private String code;
    private String name;
    private String phone;
    private String lat;
    private String lng;
    public Kid(){

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String  getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Kid(String id, String code, String name, String phone, String lat, String lng) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
    }

    public Kid(String id, String code, String name, String phone) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.phone = phone;
    }
}

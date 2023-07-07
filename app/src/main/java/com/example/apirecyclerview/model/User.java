package com.example.apirecyclerview.model;

public class User {
    private int id;//dat dung nhu du lieu json
    private String name;
    private String job;
    private String address;

    public User(String name, String job, String address) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
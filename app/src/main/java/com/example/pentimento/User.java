package com.example.pentimento;

public class User {

    private String id;
    private String email;
    private String name;
    private String phone;
    private int age;

    public User () {}

    public User(String id, String email, String name, String phone, int age) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName()
    {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

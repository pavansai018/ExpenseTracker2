package com.krishnachaitanya.expensetracker.Model;

/**
 * Created by chaitanya on 30/3/18.
 */

public class User {
    private String email,password,repassword,phone,name,avatarUrl;


    public User(){
         this.email=email;
         this.password=password;
         this.repassword=repassword;
         this.phone=phone;
         this.name=name;
         this.avatarUrl=avatarUrl;
      }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
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
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

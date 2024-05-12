package com.example.yallain;

import java.util.Date;
//TODO : 1
public class UserModel {
    String fullName ,email ,sex,formattedDateOfBirth;
    int userType;
    static int ADMIN_USER=0;
    static int USER=1;
    public UserModel(String fullName, String userName, String sex, String formattedDateOfBirth,int userType) {
        this.fullName = fullName;
        this.email = userName;
        this.sex = sex;
        this.formattedDateOfBirth = formattedDateOfBirth;
        this.userType=userType;
    }

}

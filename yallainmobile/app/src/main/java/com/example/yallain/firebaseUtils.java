package com.example.yallain;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//TODO : 1
public class firebaseUtils {
    public static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String USERS_COLLECTION="users";


    //Check Email/Password Validation Before SignUp / signIn
    static boolean isValidEmail(String emailAddress){
        Pattern pattern=Pattern.compile(EMAIL_REGEX);
        Matcher matcher=pattern.matcher(emailAddress);
        return matcher.matches();
    }
    static boolean isValidPassword(String password){
        return (password.length()>8&&password.length()<16);
    }
    public static Map<String, Object> convertUserModelToMap(UserModel user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", user.fullName);
        userMap.put("email", user.email);
        userMap.put("sex", user.sex);
        userMap.put("formattedDateOfBirth", user.formattedDateOfBirth);
        userMap.put("userType", user.userType);
        return userMap;
    }
}

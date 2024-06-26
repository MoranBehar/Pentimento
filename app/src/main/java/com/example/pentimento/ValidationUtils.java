package com.example.pentimento;

import android.text.TextUtils;
import android.util.Patterns;

public class ValidationUtils {

    public ValidationUtils(){}

    public boolean isEmailOK(String email) {
        if (android.text.TextUtils.isEmpty(email))
            return false;
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return false;

        return true;
    }
    public boolean isPasswordOK(String password) {
        if (android.text.TextUtils.isEmpty(password))
            return false;
        else if (password.length() < 6)
            return false;

        return true;
    }
    public boolean isNameOK(String name) {

        String allowedNameRegex = "^[A-Za-z\\s]+$";

        if (android.text.TextUtils.isEmpty(name))
            return false;
        else if (name.length() < 3)
            return false;
        else if(!name.matches(allowedNameRegex))
            return false;

        return true;
    }
    public boolean isAgeOK(String age) {

        int ageNum;

        try {
            ageNum=Integer.parseInt(age);
        } catch (NumberFormatException e) {
            return false;
        }

        if (android.text.TextUtils.isEmpty(age))
            return false;
        else if (ageNum < 15)
            return false;

        return true;
    }
    public boolean isPhoneOK(String phone) {
        if (TextUtils.isEmpty(phone))
            return false;
        else if (!phone.substring(0, 2).equals("05") || phone.charAt(3)!='-')
            return false;

        return true;
    }
}

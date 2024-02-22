package com.example.pentimento;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etSignUpName, etSignUpEmail, etSignUpPhoneNumber, etSignUpAge, etSignUpPassword;
    Button btnSignUp, btnNeedLogin;

    FirebaseAuth fbAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();
        setListeners();
        fbAuth = FirebaseAuth.getInstance();
    }

    private void findViews() {
        etSignUpName = findViewById(R.id.etSignUpName);
        etSignUpEmail = findViewById(R.id.etSignUpEmail);
        etSignUpPhoneNumber = findViewById(R.id.etSignUpPhoneNumber);
        etSignUpAge = findViewById(R.id.etSignUpAge);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnNeedLogin = findViewById(R.id.btnNeedLogin);
    }

    private void setListeners() {
        btnSignUp.setOnClickListener(this);
        btnNeedLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnSignUp)
        {
//            signUpUserToApp();
        }
        else if(v == btnNeedLogin)
        {
            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
        }
    }

//    private void signUpUserToApp() {
//        if(isEmailOK(email) && isPasswordOK(password) && isNameOK(name) &&
//                isAgeOK(age) && isPhoneOK(phone)){
//
//        }
//
//    }
}
package com.example.pentimento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    FormTextEditComponent cmpSignUpName, cmpSignUpEmail, cmpSignUpPhoneNumber, cmpSignUpAge, cmpSignUpPassword;

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
        cmpSignUpName = findViewById(R.id.etSignUpName);
        cmpSignUpEmail = findViewById(R.id.etSignUpEmail);
        cmpSignUpPhoneNumber = findViewById(R.id.etSignUpPhoneNumber);
        cmpSignUpAge = findViewById(R.id.etSignUpAge);
        cmpSignUpPassword = findViewById(R.id.etSignUpPassword);

        etSignUpName = cmpSignUpName.getEditText();
        etSignUpEmail = cmpSignUpEmail.getEditText();
        etSignUpPhoneNumber = cmpSignUpPhoneNumber.getEditText();
        etSignUpAge = cmpSignUpAge.getEditText();
        etSignUpPassword = cmpSignUpPassword.getEditText();

        btnSignUp = findViewById(R.id.btnSignUp);
        btnNeedLogin = findViewById(R.id.btnNeedLogin);
    }

    private void setListeners() {
        btnSignUp.setOnClickListener(this);
        btnNeedLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSignUp) {
            startSignUpProses();
        } else if (v == btnNeedLogin) {
            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
        }
    }

    private void startSignUpProses() {
        String name = etSignUpName.getText().toString();
        String email = etSignUpEmail.getText().toString();
        String phone = etSignUpPhoneNumber.getText().toString();
        String age = etSignUpAge.getText().toString();
        String password = etSignUpPassword.getText().toString();

        ValidationUtils validate = new ValidationUtils();

        if (validate.isEmailOK(email) && validate.isPasswordOK(password)
                && validate.isNameOK(name) && validate.isAgeOK(age)
                && validate.isPhoneOK(phone)) {

            signUpUserToApp(name, email, phone, password, age);
        } else if (!validate.isEmailOK(email)) {
            cmpSignUpEmail.setError("Email is invalid");
        } else if (!validate.isAgeOK(age)) {
            cmpSignUpAge.setError("You must be at least 15 to register");
        } else if (!validate.isNameOK(name)) {
            cmpSignUpName.setError("Name is invalid, only allow a-z A-Z");
        } else if (!validate.isPasswordOK(password)) {
            etSignUpPassword.setError("Password is invalid, need at lest 6 chars");
        } else if (!validate.isPhoneOK(phone)) {
            etSignUpPhoneNumber.setError("Invalid phone number");
        }
    }

    private void signUpUserToApp(String name, String email, String phone, String password, String age) {

        fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseFirestore store = FirebaseFirestore.getInstance();
                    User user = new User(fbAuth.getUid(), email, name, phone, Integer.parseInt(age));

                    store.collection("Users").document(fbAuth.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            UIAlerts.ErrorAlert("SignUp", "Failed registering a new user", SignUpActivity.this);
                        }
                    });

                } else {
                    UIAlerts.ErrorAlert("SignUp", "Failed registering a new user", SignUpActivity.this);
                    Log.d("error", task.getException().toString());
                }
            }
        });
    }

}
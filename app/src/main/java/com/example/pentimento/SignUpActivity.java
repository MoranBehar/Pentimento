package com.example.pentimento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
            startSignUpProses();
        }
        else if(v == btnNeedLogin)
        {
            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
        }
    }

    private void startSignUpProses(){
        String name = etSignUpName.getText().toString();
        String email = etSignUpEmail.getText().toString();
        String phone = etSignUpPhoneNumber.getText().toString();
        String age = etSignUpAge.getText().toString();
        String password = etSignUpPassword.getText().toString();

        ValidateInfo validate = new ValidateInfo();

        if(validate.isEmailOK(email) && validate.isPasswordOK(password) &&
                validate.isNameOK(name) && validate.isAgeOK(age) && validate.isPhoneOK(phone)) {

            signUpUserToApp(name, email, phone, password, age);
        }
        else if(!validate.isEmailOK(email))
        {
            Toast.makeText(SignUpActivity.this, "Email not valid",
                    Toast.LENGTH_LONG).show();
        }
        else if(!validate.isAgeOK(age))
        {
            Toast.makeText(SignUpActivity.this, "Age not valid",
                    Toast.LENGTH_LONG).show();
        }
        else if(!validate.isNameOK(name))
        {
            Toast.makeText(SignUpActivity.this, "Name not valid",
                    Toast.LENGTH_LONG).show();
        }
        else if(!validate.isPasswordOK(password))
        {
            Toast.makeText(SignUpActivity.this, "Password not valid",
                    Toast.LENGTH_LONG).show();
        }
        else if(!validate.isPhoneOK(phone))
        {
            Toast.makeText(SignUpActivity.this, "Phone not valid",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void signUpUserToApp(String name, String email, String phone,
                                 String password, String age) {

        fbAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    FirebaseFirestore store = FirebaseFirestore.getInstance();
                                    User user = new User(email, name, phone, Integer.parseInt(age));

                                    store.collection("users").document(fbAuth.getUid())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Intent intent = new Intent(SignUpActivity.this,
                                                            FragmentsActivity.class);
                                                    startActivity(intent);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignUpActivity.this,
                                                            "Failed to register",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            });

                                    Toast.makeText(SignUpActivity.this,
                                            "Wait...",
                                            Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(SignUpActivity.this,
                                            "Failed to register",
                                            Toast.LENGTH_LONG).show();
                                    Log.d("error", task.getException().toString());
                                }
                            }
                        });
    }

//    private boolean isEmailOK(String email) {
//        if (android.text.TextUtils.isEmpty(email))
//            return false;
//        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
//            return false;
//
//        return true;
//    }
//    private boolean isPasswordOK(String password) {
//        if (android.text.TextUtils.isEmpty(password))
//            return false;
//        else if (password.length() < 6)
//            return false;
//
//        return true;
//    }
//    private boolean isNameOK(String name) {
//        if (android.text.TextUtils.isEmpty(name))
//            return false;
//        else if (name.length() < 2)
//            return false;
//        else if(!name.chars().allMatch(Character::isLetter))
//            return false;
//
//        return true;
//    }
//    private boolean isAgeOK(String age) {
//        int ageNum=Integer.parseInt(age);
//        if (android.text.TextUtils.isEmpty(age))
//            return false;
//        else if (ageNum < 15)
//            return false;
//
//        return true;
//    }
//    private boolean isPhoneOK(String phone) {
//        if (TextUtils.isEmpty(phone))
//            return false;
//        else if (!phone.substring(0, 2).equals("05") || phone.charAt(3)!='-')
//            return false;
//
//        return true;
//    }
}
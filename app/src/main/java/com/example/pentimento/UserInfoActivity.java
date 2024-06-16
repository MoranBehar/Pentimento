package com.example.pentimento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    FormTextEditComponent cmpEmail, cmpName, cmpAge, cmpPhone;
    TextView etUserDetailsEmail, etUserDetailsName, etUserDetailsAge, etUserDetailsPhone;

    Button btnClose, btnUpdateInfo;

    FirebaseAuth fbAuth;
    FirebaseFirestore store;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        findViews();
        setListeners();

        fbAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();

        loadUserData();
    }

    private void setListeners() {
        btnClose.setOnClickListener(this);
        btnUpdateInfo.setOnClickListener(this);
    }

    private void findViews() {

        // Edit Components
        cmpEmail = findViewById(R.id.etUserDetailsEmail);
        cmpName = findViewById(R.id.etUserDetailsName);
        cmpAge = findViewById(R.id.etUserDetailsAge);
        cmpPhone = findViewById(R.id.etUserDetailsPhone);

        // Text view
        etUserDetailsEmail = cmpEmail.getEditText();
        etUserDetailsName = cmpName.getEditText();
        etUserDetailsAge = cmpAge.getEditText();
        etUserDetailsPhone = cmpPhone.getEditText();

        btnClose = findViewById(R.id.btnClose);
        btnUpdateInfo = findViewById(R.id.btnUpdateInfo);
    }

    @Override
    public void onClick(View v) {
        if (v == btnClose) {
            finish();
        } else if (v == btnUpdateInfo) {
            String newEmail = etUserDetailsEmail.getText().toString();
            String newName = etUserDetailsName.getText().toString();
            String newPhone = etUserDetailsPhone.getText().toString();
            String newAge = etUserDetailsAge.getText().toString();

            Boolean formValid = true;

            //check if new data is valid
            ValidationUtils validate = new ValidationUtils();

            if (!validate.isEmailOK(newEmail)) {
                etUserDetailsEmail.setError("Invalid email address");
                formValid = false;
            }
            if (!validate.isNameOK(newName)) {
                etUserDetailsName.setError("Invalid or missing name");
                formValid = false;
            }
            if (!validate.isPhoneOK(newPhone)) {
                etUserDetailsPhone.setError("Invalid phone number");
                formValid = false;
            }
            if (!validate.isAgeOK(newAge)) {
                etUserDetailsAge.setError("You must be at least 15");
                formValid = false;
            }

            if (formValid) {
                //creating new user with the new date
                User newUserInfo = new User(fbAuth.getUid(), newEmail, newName, newPhone, Integer.parseInt(newAge));

                //update the current user to the new user with the same id
                updateUserInfo(newUserInfo);
            }
        }
    }

    private void updateUserInfo(User user) {

        //get current user id
        String uid = fbAuth.getUid();

        //change the current user data with the new user data
        store.collection("Users").document(uid).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        UIAlerts.InfoAlert("User Profile", "Your profile was updated Successfully", UserInfoActivity.this);

                        // Wait for 3 sec before killing the activity and returning to the previous one
                        new Handler().postDelayed(() -> finish(), 3000);
                    }
                });

    }


    private void loadUserData() {
        String uid = fbAuth.getUid();

        store.collection("Users").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        getUserData(documentSnapshot);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        UIAlerts.ErrorAlert("Error","Could not load your data", UserInfoActivity.this);
                    }
                });
    }

    private void getUserData(DocumentSnapshot documentSnapshot) {
        //if the user exist - get his data
        if (documentSnapshot.exists()) {
            User user = documentSnapshot.toObject(User.class);
            etUserDetailsEmail.setText(user.getEmail());
            etUserDetailsName.setText(user.getName());
            etUserDetailsAge.setText(String.valueOf(user.getAge()));
            etUserDetailsPhone.setText(user.getPhone());

            // Check if data has changed
            configureFormChangedListeners();
        }
    }

    private void configureFormChangedListeners() {

        //check for each one of the user data for changes
        //if there is a change - set the flag to true
        etUserDetailsPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                formChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etUserDetailsAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                formChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etUserDetailsName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                formChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etUserDetailsEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                formChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void formChanged() {

        //there is new data
        btnUpdateInfo.setEnabled(true);
    }
}
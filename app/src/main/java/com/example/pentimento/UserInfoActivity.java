package com.example.pentimento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    TextView etUserDetailsEmail,etUserDetailsName,etUserDetailsAge, etUserDetailsPhone;

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

        fbAuth=FirebaseAuth.getInstance();
        store=FirebaseFirestore.getInstance();

        loadUserData();
    }

    private void setListeners() {
        btnClose.setOnClickListener(this);
        btnUpdateInfo.setOnClickListener(this);
    }

    private void findViews() {
        etUserDetailsEmail=findViewById(R.id.etUserDetailsEmail);
        etUserDetailsName=findViewById(R.id.etUserDetailsName);
        etUserDetailsAge=findViewById(R.id.etUserDetailsAge);
        etUserDetailsPhone=findViewById(R.id.etUserDetailsPhone);
        btnClose=findViewById(R.id.btnClose);
        btnUpdateInfo=findViewById(R.id.btnUpdateInfo);
    }

    @Override
    public void onClick(View v) {
        if(v == btnClose)
        {
            finish();
        }
        else if(v == btnUpdateInfo)
        {
            updateUserInfo();
        }
    }

    private void updateUserInfo() {

    }


    private void loadUserData() {
        String uid= fbAuth.getUid();

        store.collection("users").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        getUserData(documentSnapshot);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserInfoActivity.this,
                                "Could not get your data", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getUserData(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
            User user = documentSnapshot.toObject(User.class);
            etUserDetailsEmail.setText(user.getEmail());
            etUserDetailsName.setText(user.getName());
            etUserDetailsAge.setText(String.valueOf(user.getAge()));
            etUserDetailsPhone.setText(user.getPhone());
        }
    }
}
package com.example.pentimento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    FormTextEditComponent cmpLogin, cmpPassword;
    EditText etLogInEmail, etLogInPassword;

    Button btnLogIn, btnNeedSignup;

    FirebaseAuth fbAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        findViews();
        setListeners();
        fbAuth = FirebaseAuth.getInstance();
    }

    private void findViews() {
        cmpLogin = findViewById(R.id.etLogInEmail);
        cmpPassword = findViewById(R.id.etLogInPassword);
        etLogInEmail = cmpLogin.getEditText();
        etLogInPassword = cmpPassword.getEditText();

        // TODO - Remove before release
        etLogInEmail.setText("user@gmail.com");
        etLogInPassword.setText("123456");

        btnLogIn = findViewById(R.id.btnLogIn);
        btnNeedSignup = findViewById(R.id.btnNeedSignup);
    }

    private void setListeners() {
        btnLogIn.setOnClickListener(this);
        btnNeedSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnLogIn)
        {
            logInUserToTheApp();
        }
        else if(v == btnNeedSignup)
        {
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    private void logInUserToTheApp() {
        String email = etLogInEmail.getText().toString();
        String password = etLogInPassword.getText().toString();

        fbAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(LogInActivity.this,
                                    FragmentsActivity.class);
                            startActivity(intent);
                        }
                        else if(email != fbAuth.getCurrentUser().getEmail())
                        {
                            Toast.makeText(LogInActivity.this,
                                    "Email incorrect",
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(LogInActivity.this,
                                    "Password incorrect",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
package com.example.smilingface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btnSignIn, btnResetPw, btnRegisterLink;
    private EditText inputEmail, inputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() != null) {
//            startActivity(new Intent(SignInActivity.this, MainActivity.class));
//            finish();
//        }
        setContentView(R.layout.signin_screen);

        btnSignIn = findViewById(R.id.btnSignin2);
        inputEmail = findViewById(R.id.inputViewEmail);
        inputPassword = findViewById(R.id.inputPw);
        btnRegisterLink = findViewById(R.id.btnRegisterLink);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(SignInActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(SignInActivity.this,SelectFace.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("user", (Serializable) email);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
        btnRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnResetPw = findViewById(R.id.btn_reset_password);
        btnResetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ResetPassword.class);
                startActivity(intent);
            }
        });
    }
}
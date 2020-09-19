package com.agro.agro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kusu.loadingbutton.LoadingButton;

/*LOGIN */

public class LoginActivity extends AppCompatActivity {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        final EditText txtEmail = findViewById(R.id.textLoginEmail);
        final EditText txtPassword = findViewById(R.id.textLoginPassword);

        LoadingButton buttonLogin = (LoadingButton) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLogin.showLoading();

                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                //Input Validation
                if (email.isEmpty() || password.isEmpty()) {
                    buttonLogin.hideLoading();
                    if (email.isEmpty()) {
                        txtEmail.setError("Please Enter Your Email.");
                        txtEmail.requestFocus();
                    } else {
                        txtEmail.setError(null);
                    }
                    if (password.isEmpty()) {
                        txtPassword.setError("Please Enter Your Password.");
                        txtPassword.requestFocus();
                    } else {
                        txtPassword.setError(null);
                    }
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        buttonLogin.hideLoading();
                                        Toast.makeText(LoginActivity.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        LoadingButton buttonRegister = (LoadingButton) findViewById(R.id.buttonLoginRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
package com.agro.agro;

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

/*REGISTER */

public class RegisterActivity extends AppCompatActivity {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText txtName = findViewById(R.id.txtName);
        EditText txtEmailAddress = findViewById(R.id.textRegisterEmailAddress);
        EditText txtPassword = findViewById(R.id.textRegisterPassword);

        Button buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = txtName.getText().toString();
                final String email = txtEmailAddress.getText().toString();
                final String password = txtPassword.getText().toString();

                if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    if (email.isEmpty()) {
                        txtEmailAddress.setError("Enter Your Email Address.");
                        txtEmailAddress.requestFocus();
                    } else {
                        txtEmailAddress.setError(null);
                    }
                    if (name.isEmpty()) {
                        txtName.setError("Enter Your Name.");
                        txtName.requestFocus();
                    } else {
                        txtName.setError(null);
                    }
                    if (password.isEmpty()) {
                        txtPassword.setError("Enter a Valid Password.");
                        txtPassword.requestFocus();
                    } else {
                        txtPassword.setError(null);
                    }
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(getApplicationContext(), "Registration Successfully.", Toast.LENGTH_SHORT).show();

                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Registration Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });
    }

}
package com.agro.agro;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kusu.loadingbutton.LoadingButton;

/*REGISTER */

public class RegisterActivity extends AppCompatActivity {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        EditText txtName = findViewById(R.id.txtName);
        EditText txtEmailAddress = findViewById(R.id.textRegisterEmailAddress);
        EditText txtPassword = findViewById(R.id.textRegisterPassword);

        LoadingButton buttonRegister = (LoadingButton) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonRegister.showLoading();
                final String name = txtName.getText().toString();
                final String email = txtEmailAddress.getText().toString();
                final String password = txtPassword.getText().toString();

                if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    buttonRegister.hideLoading();
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
                                        buttonRegister.hideLoading();
                                        Toast.makeText(getApplicationContext(), "Registration Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

}
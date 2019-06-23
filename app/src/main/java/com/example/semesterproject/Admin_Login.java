package com.example.semesterproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class Admin_Login extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button btnLogin;

    private ProgressDialog mDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__login);

        mDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.emailEditText);
        pass = (EditText) findViewById(R.id.passwordEditText);
        btnLogin = (Button) findViewById(R.id.loginBTN_ID);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminEmail = email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();

                if(TextUtils.isEmpty(adminEmail)) {
                    email.setError("Required Field...");
                    return;
                }
                if(TextUtils.isEmpty(mPass)) {
                    pass.setError("Required Field...");
                    return;
                }

                mDialog.setTitle("LOADING");
                mDialog.setMessage("Please wait...");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(adminEmail, mPass).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if(task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Dashboard_Page.class));
                                    mDialog.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Problem occured while Signing In:", Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();
                                }
                            }
                        }
                );
            }
        });

    }
}

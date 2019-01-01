package com.granule.granule;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button loginbutton;
    private EditText emailFeild;
    private EditText passwordFeild;
    private String pass_word;
    private String email_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailFeild = findViewById(R.id.text_input_login_email);
        passwordFeild = findViewById(R.id.password_feild_login_activity);
        loginbutton = findViewById(R.id.button_login_activity_button);


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pass_word = passwordFeild.getEditableText().toString();
                email_string = emailFeild.getEditableText().toString();
                if (TextUtils.isEmpty(pass_word) || TextUtils.isEmpty(email_string) ) {

                    Toast.makeText(LoginActivity.this, "Some fields are empty.", Toast.LENGTH_LONG).show();


                }else{
                    signInExistingUser(email_string,pass_word);
                }
            }
        });
    }
    public void signInExistingUser(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent iMainActivity = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(iMainActivity);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Jack", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

}

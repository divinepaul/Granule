package com.granule.granule;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    protected Button registerButton;
    protected EditText nameFeild;
    protected EditText emailFeild;
    protected EditText passwordFeild;
    private FirebaseAuth mAuth;

    DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = findViewById(R.id.register_activity_butn);
        nameFeild = findViewById(R.id.register_activity_namefield);
        emailFeild = findViewById(R.id.register_activity_emailfield);
        passwordFeild = findViewById(R.id.register_activity_passfield);


        mAuth = FirebaseAuth.getInstance();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailfeildstring = emailFeild.getEditableText().toString();
                String passwordfeildstring = passwordFeild.getEditableText().toString();
                String nameFeildString = nameFeild.getEditableText().toString();

                registerUser(nameFeildString,emailfeildstring,passwordfeildstring);
            }
        });
    }
    public void registerUser(final String name,String email,String password){



        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("ACCOUNT","CREATED");

                    FirebaseUser mUser = mAuth.getCurrentUser();
                    String uid;
                    String userEmail;

                    if (mUser!=null) {

                        uid = mUser.getUid();
                        userEmail = mUser.getEmail();

                        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                        HashMap<String,String> userMap = new HashMap<>();
                        userMap.put("email",userEmail);
                        userMap.put("name",name);

                        mDatabaseRef.setValue(userMap);

                    }

                    Intent iMainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(iMainActivity);
                    finish();
                }else{
                    Log.e("Account","fail");
                    Toast.makeText(RegisterActivity.this,"Auth fail",Toast.LENGTH_LONG).show();

                }

            }
        });
    }


}

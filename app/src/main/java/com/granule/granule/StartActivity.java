package com.granule.granule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {
    private Button loginButton;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loginButton = findViewById(R.id.login_nav_button);
        registerButton= findViewById(R.id.register_nav_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iLoginActivity = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(iLoginActivity);

            }

        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iRegisterActivity = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(iRegisterActivity);
            }
        });

    }


}

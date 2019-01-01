package com.granule.granule;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {


    private FirebaseUser mUser;
    private DatabaseReference mRefName;
    private EditText nameField;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initAssign();
    }

    private void initAssign() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser!= null){
            mRefName = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("name");
         }
         nameField = findViewById(R.id.editText2);

    }



}

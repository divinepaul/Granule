package com.granule.granule;

import android.content.Intent;
import android.graphics.Point;
import android.provider.ContactsContract;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "Granule" ;
    private DatabaseReference mRefName;
    private String mUserName;
    private EditText message_feild;
    private ImageButton send_button;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser currentUser;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ProgressBar mProgress;
    private ScrollView mScroll;
    private LinearLayout chatLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent iStartScreen = new Intent(MainActivity.this,StartActivity.class);
            startActivity(iStartScreen);
            finish();
        }else{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("chats");
            mProgress = findViewById(R.id.progressBar);
            mRefName = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        }

        mScroll = findViewById(R.id.message_scroll);
        mScroll.fullScroll(View.FOCUS_DOWN);
        send_button = findViewById(R.id.send_button);
        message_feild = findViewById(R.id.message_feild);
        chatLayout = findViewById(R.id.chat_linear_layout);
        send();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.log_out){
            mAuth.signOut();
            Intent iStartScreen = new Intent(MainActivity.this,StartActivity.class);
            startActivity(iStartScreen);
            finish();

        }
        if (item.getItemId() == R.id.profile_item){

            Intent iProfileScreen = new Intent(getApplicationContext(),ProfileActivity.class);
            startActivity(iProfileScreen);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    public void send(){

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(message_feild.getEditableText().toString()) && !message_feild.getEditableText().toString().equals(" ")) {
                    if(mUserName == null){
                        send_button.setEnabled(false);
                    }else {

                        String message = message_feild.getEditableText().toString();
                        String email = mUserName;

                        Map<String, String> message_package = new HashMap<>();
                        message_package.put(email, message);

                        mDatabaseRef.push().setValue(message_package);

                        message_feild.setText(" ");
                        mScroll.fullScroll(View.FOCUS_DOWN);
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mScroll.post(new Runnable() {
            public void run() {
                mScroll.fullScroll(View.FOCUS_DOWN);
            }
        });

        if (currentUser!=null) {
            mRefName.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (dataSnapshot.getValue() != null) {
                        mUserName = dataSnapshot.getValue().toString();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Intent iStartScreen = new Intent(MainActivity.this,StartActivity.class);
            startActivity(iStartScreen);
            finish();
        }

        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mScroll.fullScroll(View.FOCUS_DOWN);
                mProgress.setVisibility(View.GONE);
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                    TextView messageTextView = new TextView(MainActivity.this);
                    TextView userTextView = new TextView(MainActivity.this);
                    LinearLayout messageLayout = new LinearLayout(MainActivity.this);

                    String message_user = childSnapshot.getKey();
                    String message = childSnapshot.getValue(String.class);

                    messageTextView.setText(message);


                    messageLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    LinearLayout.LayoutParams parameter =   (LinearLayout.LayoutParams) messageLayout.getLayoutParams();

                    if (message_user.equals(mUserName)){

                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x;
                        int height = size.y;

                        userTextView.setGravity(Gravity.START);
                        messageTextView.setGravity(Gravity.START);

                        parameter.setMargins((width - 200),0,10,20);

                        messageLayout.setBackgroundResource(R.drawable.bg_round_black);
                        messageTextView.setTextColor(getResources().getColor(android.R.color.white));
                        userTextView.setTextColor(getResources().getColor(android.R.color.white));

                    }else{
                        parameter.setMargins(10,0,0,20);
                        userTextView.setTextColor(getResources().getColor(android.R.color.black));
                        messageTextView.setTextColor(getResources().getColor(android.R.color.black));
                        messageLayout.setBackgroundResource(R.drawable.bg_round);
                    }
                    messageLayout.setLayoutParams(parameter);

                    messageLayout.setOrientation(LinearLayout.VERTICAL);


                    messageTextView.setTextSize(15);

                    userTextView.setText(message_user);
                    userTextView.setTextSize(10);


                    messageLayout.addView(userTextView);
                    messageLayout.addView(messageTextView);

                    chatLayout.setPadding(0,0,0,80);
                    chatLayout.addView(messageLayout);
                    mScroll.post(new Runnable() {
                        public void run() {
                            mScroll.fullScroll(View.FOCUS_DOWN);
                        }
                    });

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mScroll.post(new Runnable() {
            public void run() {
                mScroll.fullScroll(View.FOCUS_DOWN);
            }
        });

        Intent iService = new Intent(getApplicationContext(),NotificationService.class);

        startService(iService);

    }

}

package com.example.smilingface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectFace extends AppCompatActivity {
    private static final String TAG = SelectFace.class.getSimpleName();
    private DatabaseReference mFbDb;
    private FirebaseDatabase mFbInstance;
    private Button btnFinish;
    private String userId;
    private ImageView imgHappy, imgNormal, imgSad;
    int happy=0,sad=0,normal=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_face);
        btnFinish = findViewById(R.id.btnFinish);
        imgHappy = findViewById(R.id.imgViewHappy);
        imgHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happy++;
            }
        });
        imgNormal = findViewById(R.id.imgViewNormal);
        imgNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normal++;
            }
        });
        imgSad = findViewById(R.id.imgViewUnhappy);
        imgSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sad++;
            }
        });
        mFbInstance = FirebaseDatabase.getInstance();
        mFbDb = mFbInstance.getReference("users");
        mFbInstance.getReference("app_title").setValue("Smiling Face");
        mFbInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e(TAG,"App title updated");
                String appTitle = snapshot.getValue(String.class);
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                String email = (String) bundle.getSerializable("user");
                if (TextUtils.isEmpty(userId)) {
                    createUser(email,happy,normal,sad);
                } else {
                    updateUser(email,happy,normal,sad);
                }
                Intent intent = new Intent(SelectFace.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void createUser(String email,int happy, int normal, int sad) {
        if (TextUtils.isEmpty(userId)) {
            userId = mFbDb.push().getKey();
        }
        User user = new User(email,happy,normal,sad);
        mFbDb.child(userId).setValue(user);
        addUserChangeListener();
    }

    private void addUserChangeListener() {
        mFbDb.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }
                Log.e(TAG, "User data is changed!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }
    private void updateUser(String email,int happy, int normal, int sad) {
        if (!TextUtils.isEmpty(email))
            mFbDb.child(userId).child("happy").setValue(happy);
            mFbDb.child(userId).child("sad").setValue(sad);
            mFbDb.child(userId).child("normal").setValue(normal);
    }
}
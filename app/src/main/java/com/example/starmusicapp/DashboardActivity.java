package com.example.starmusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    ImageView home, users, notification, uploadmusic, logout, manageuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        home = findViewById(R.id.Home);
/*        users = findViewById(R.id.users);
        notification = findViewById(R.id.Notification);*/
        uploadmusic = findViewById(R.id.UploadMusic);
        logout = findViewById(R.id.Logout);
        manageuser = findViewById(R.id.Manageuser);

//        users.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), UserActivity.class));
//                finish();
//            }
//        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                finish();
            }
        });
        uploadmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UploadActivity.class));
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                Toast.makeText(DashboardActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        manageuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserManage.class));
                finish();
            }
        });
    }
}
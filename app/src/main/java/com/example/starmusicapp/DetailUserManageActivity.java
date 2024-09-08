package com.example.starmusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailUserManageActivity extends AppCompatActivity {
    TextView detailName, detailEmail, detailUsertype;
    FloatingActionButton deleteBtn, editBtn;
    String uid = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_manage);

        detailName = findViewById(R.id.detailName);
        detailEmail = findViewById(R.id.detailEmail);
        detailUsertype = findViewById(R.id.detailUsertype);
        deleteBtn = findViewById(R.id.deletebtnuser);
        editBtn = findViewById(R.id.editbtnuser);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailName.setText(bundle.getString("Name"));
            detailEmail.setText(bundle.getString("Email"));
            uid = bundle.getString("Uid");
            int usertypeValue = getIntent().getIntExtra("Usertype", 0);
            detailUsertype.setText(String.valueOf(usertypeValue));
        }
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

                reference.child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailUserManageActivity.this, "Đã xóa dữ liệu", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), UserManage.class));
                        finish();
                    }
                });

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int usertype = Integer.parseInt(detailUsertype.getText().toString());
                Intent intent = new Intent(DetailUserManageActivity.this, UpdateUserActivity.class)
                        .putExtra("Name", detailName.getText().toString())
                        .putExtra("Email", detailEmail.getText().toString())
                        .putExtra("Usertype", usertype)
                        .putExtra("Uid", uid);
                startActivity(intent);
            }
        });

    }
}
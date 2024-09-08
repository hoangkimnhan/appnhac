package com.example.starmusicapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.MoreObjects;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserActivity extends AppCompatActivity {
    Button updateUserBtn;
    EditText updatenameuser, updateemailuser, updatetypeuser;
    String name, email, uid;
    Integer usertype;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        updateUserBtn = findViewById(R.id.updateUserButton);
        updatenameuser = findViewById(R.id.updatenameUser);
        updateemailuser = findViewById(R.id.updateemailUser);
        updatetypeuser = findViewById(R.id.updatetypeUser);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                uri = data.getData();
                            }
                        }
                    }
                });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            updatenameuser.setText(bundle.getString("Name"));
            updateemailuser.setText(bundle.getString("Email"));
            int usertypeValue = bundle.getInt("Usertype", 0);
            updatetypeuser.setText(String.valueOf(usertypeValue));
            uid = bundle.getString("Uid");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        updateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pending
                saveData();
                Intent intent = new Intent(UpdateUserActivity.this, UserManage.class);
                startActivity(intent);
            }
        });
    }

    public void saveData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        performUpdate();
        dialog.dismiss();
    }

    private void performUpdate() {
        // Lấy giá trị từ các trường nhập liệu
        String name = updatenameuser.getText().toString().trim();
        String email = updateemailuser.getText().toString().trim();
        String usertypeString = updatetypeuser.getText().toString().trim();
        int usertype = Integer.parseInt(usertypeString);

        // Tạo một HashMap để chứa các trường dữ liệu cần cập nhật
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("name", name);
        updateData.put("email", email);
        updateData.put("usertype", usertype);

        // Cập nhật dữ liệu trong cơ sở dữ liệu
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        databaseReference.updateChildren(updateData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateUserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(UpdateUserActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
}
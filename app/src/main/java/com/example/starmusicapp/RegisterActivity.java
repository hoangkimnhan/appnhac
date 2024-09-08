package com.example.starmusicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

public class RegisterActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextInputEditText editTextFullName, editTextEmail, editTextPassword, editTextConfirmPassword, editTextPhone;
    Button registerBtn;
    TextView tvToLogin;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        progressBar= findViewById(R.id.progressBar);
        editTextFullName = findViewById(R.id.registerName);
        editTextEmail = findViewById(R.id.registerEmail);
        editTextPassword = findViewById(R.id.registerPassword);
        editTextConfirmPassword = findViewById(R.id.registerConfirmPassword);
        editTextPhone = findViewById(R.id.registerPhoneNumber);
        registerBtn = findViewById(R.id.btn_register);
        tvToLogin = findViewById(R.id.tv_toLogin);

        tvToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checkField(editTextFullName);
//                checkField(editTextEmail);
//                checkField(editTextPassword);
//                checkField(editTextConfirmPassword);
//                checkField(editTextPhone);

                String email, password, phoneNumber, confirmPassword;
                email= String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                phoneNumber= String.valueOf(editTextPhone.getText());
                confirmPassword= String.valueOf(editTextConfirmPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Nhập Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Nhập Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(RegisterActivity.this, "Nhập Số Điện Thoại", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(RegisterActivity.this, "Nhập Lại Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!confirmPassword.equals(password)){
                    Toast.makeText(RegisterActivity.this, "Password phải giống nhau", Toast.LENGTH_SHORT).show();
                    return;
                }


                fAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    String uid = task.getResult().getUser().getUid();
                                    Users user = new Users(uid,editTextFullName.getText().toString(), editTextPhone.getText().toString(), editTextEmail.getText().toString(), editTextPassword.getText().toString(),0);
//                                    String currentData = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(user);

                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(RegisterActivity.this, "Tài khoản đã được đăng ký",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "Có Ai Đó Đã Sử Dụng Email Này Để Đăng Ký Rồi",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

    }

//    public boolean checkField(TextInputEditText textField){
//        if(textField.getText().toString().isEmpty()){
//            textField.setError("Lỗi");
//            valid = false;
//        }else {
//            valid = true;
//        }
//        return valid;
//    }
}
package com.example.starmusicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView tvToRegis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar= findViewById(R.id.progressBar);
        mAuth= FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        tvToRegis = findViewById(R.id.tv_toRegis);

        tvToRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email= String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                fStore = FirebaseFirestore.getInstance();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Nhập Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Nhập Password", Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    String uid = task.getResult().getUser().getUid();
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    firebaseDatabase.getReference().child("Users").child(uid).child("usertype").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int usertype = snapshot.getValue(Integer.class);
                                            if(usertype == 0){
                                                editTextEmail.setError(null);
                                                //Pass the data using intent
                                                String nameFromDB = snapshot.child(uid).child("name").getValue(String.class);
                                                String emailFromDB = snapshot.child(uid).child("email").getValue(String.class);
                                                String passDB = snapshot.child(uid).child("password").getValue(String.class);
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.putExtra("name",nameFromDB);
                                                intent.putExtra("email",emailFromDB);
                                                intent.putExtra("password",passDB);
                                                startActivity(intent);

                                            }

                                            if(usertype == 1 ){
                                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Tài khoản chưa được đăng ký",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });


    }

//    private void checkUserAccessLevel(String uid){
//        DocumentReference df = fStore.collection("Users").document(uid);
//        //extract the dât from the document
//        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
//                //identify the user access level
//                if(documentSnapshot.getString("isAdmin") != null){
//                    //user is Admin
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    Toast.makeText(LoginActivity.this, "Admin đã đăng nhập", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                if(documentSnapshot.getString("isUser") != null){
//                    startActivity(new Intent(getApplicationContext(), IntroActivity.class));
//                    Toast.makeText(LoginActivity.this, "User đã đăng nhập", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//        });
//    }
}


//firebaseDatabase.getReference().child("Users").child(uid).child("usertype").addListenerForSingleValueEvent(new ValueEventListener() {
//@Override
//public void onDataChange(@NonNull DataSnapshot snapshot) {
//        int usertype = snapshot.getValue(Integer.class);
//
//        if(usertype == 0){
//        Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
//        startActivity(intent);
//
//        }
//
//        if(usertype == 1 ){
//        String nameFromDB = snapshot.child(uid).child("")
//        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
//        startActivity(intent);
//
//        }
//        }
//
//@Override
//public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//        });



//    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//    Query checkUserDatabase = reference.orderByChild("uid").equalTo(uid);
//                                    checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//@Override
//public void onDataChange(@NonNull DataSnapshot snapshot) {
//        if(snapshot.exists()){
//        editTextEmail.setError(null);
//        String passwordFromDB = snapshot.child(uid).child("password").getValue(String.class);
//        int usertype = snapshot.child(uid).child("usertype").getValue(Integer.class);
//        if(passwordFromDB.equals(password)){
//        if(usertype == 0){
//        editTextEmail.setError(null);
//        //Pass the data using intent
////                                                        String nameFromDB = snapshot.child(uid).child("name").getValue(String.class);
////                                                        String emailFromDB = snapshot.child(uid).child("email").getValue(String.class);
////                                                        String passDB = snapshot.child(uid).child("password").getValue(String.class);
//        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
//        }
//        if(usertype == 1){
//        editTextEmail.setError(null);
//        //Pass the data using intent
//        String nameFromDB = snapshot.child(uid).child("name").getValue(String.class);
//        String emailFromDB = snapshot.child(uid).child("email").getValue(String.class);
//        String passDB = snapshot.child(uid).child("password").getValue(String.class);
//        Intent intent = new Intent(LoginActivity.this, AlanActivity.class);
//        }
//
//        }
//        // If sign in fails, display a message to the user.
//        Toast.makeText(LoginActivity.this, "Tài khoản chưa được đăng ký",
//        Toast.LENGTH_SHORT).show();
//        }
//        }
//
//@Override
//public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//        });
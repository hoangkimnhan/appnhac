package com.example.starmusicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.Value;

import java.util.ArrayList;
import java.util.List;

public class UserManage extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Users> usersList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    UserAdapter userAdapter;
    SearchView searchView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);

        recyclerView = findViewById(R.id.recyclerViewUser);
        searchView = findViewById(R.id.searchuser);
        searchView.clearFocus();
        fab = findViewById(R.id.floatingbtn);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserManage.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(UserManage.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        usersList = new ArrayList<>();
        userAdapter = new UserAdapter(UserManage.this,usersList);
        recyclerView.setAdapter(userAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Users users = itemSnapshot.getValue(Users.class);
                    users.setUid(itemSnapshot.getKey());
                    usersList.add(users);

                }
                userAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //pending
                searchList(newText);
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        });
    }

    public void searchList(String text){
        ArrayList<Users> searchList = new ArrayList<>();
        for(Users users: usersList){
            if(users.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(users);
            }
        }
        userAdapter.searchUserList(searchList);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        databaseReference.removeEventListener(eventListener);
    }
}
package com.example.starmusicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starmusicapp.Adapter.NotificationAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    BottomNavigationView nav_bar;
    private List<NotificationModel> notificationList;
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        nav_bar = findViewById(R.id.nav_bar);
        nav_bar.setSelectedItemId(R.id.notification);
        nav_bar.setOnItemSelectedListener(getListener());
        recyclerView = findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(notificationAdapter);



        // Lấy thông báo từ SharedPreferences và thêm vào danh sách thông báo
        List<NotificationModel> savedNotifications = getSavedNotifications();
        notificationList.addAll(savedNotifications);
        notificationAdapter.notifyDataSetChanged();
        clearSavedNotifications();
    }

    private List<NotificationModel> getSavedNotifications() {
        List<NotificationModel> savedNotifications = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("NotificationPrefs", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String title = entry.getKey();
            String message = entry.getValue().toString();
            NotificationModel notification = new NotificationModel(title, message);
            savedNotifications.add(notification);
        }

        return savedNotifications;
    }

    private void clearSavedNotifications() {
        SharedPreferences sharedPreferences = getSharedPreferences("NotificationPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    private NavigationBarView.OnItemSelectedListener getListener() {
        return new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.personal:
                        startActivity(new Intent(getApplicationContext(), PlayListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.notification:
                        return true;
                }
                return true;
            }
        };
    }
}

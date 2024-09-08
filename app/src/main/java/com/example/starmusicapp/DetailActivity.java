package com.example.starmusicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.starmusicapp.Adapter.SliderAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    BottomNavigationView nav_bar;

    ViewPager2 viewPager2;
    DatabaseReference mref;
    TextView SongName, SongAuth;

    MediaPlayer mediaPlayer;
    boolean play = true;
    ImageView Play,Pause,Prev,Next,imageview,downloadIcon;
    Integer currentSongIndex = 0;
    SeekBar seekBar;
    TextView Pass,Due;
    Handler handler;
    String out,out2;
    Integer totalTime;

    ArrayList<String> imageurls= new ArrayList<>();
    ArrayList<String> songnames= new ArrayList<>();
    ArrayList<String> songauths= new ArrayList<>();
    ArrayList<String> songurls= new ArrayList<>();

    List<SliderItem> sliderItems= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alan);
        nav_bar = findViewById(R.id.nav_bar);
        nav_bar.setSelectedItemId(R.id.home);
        nav_bar.setOnItemSelectedListener(getListener());
        imageview = findViewById(R.id.imageViewAlan);
        downloadIcon = findViewById(R.id.downloadIcon);
        mref = FirebaseDatabase.getInstance().getReference();
        SongName = (TextView)findViewById(R.id.songname);
        SongAuth = (TextView)findViewById(R.id.songauth);
        seekBar = (SeekBar)findViewById(R.id.seek_bar);
        Pass = (TextView)findViewById(R.id.tv_pass);
        Due = (TextView)findViewById(R.id.tv_due);
        handler = new Handler();
        Play = (ImageView)findViewById(R.id.play);
        Pause = (ImageView)findViewById(R.id.pause);
        Prev = (ImageView)findViewById(R.id.prev);
        Next = (ImageView)findViewById(R.id.next);
        mref = FirebaseDatabase.getInstance().getReference("Songs");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String title = bundle.getString("Title");
            // Lấy tham chiếu đến TextView trong layout của bạn
            TextView songNameTextView = findViewById(R.id.songname);
            TextView songAuthTextView = findViewById(R.id.songauth);

            // Set tên bài hát và tác giả vào TextView
            songNameTextView.setText(title);


            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String songname = ds.child("songname").getValue(String.class);
                        if (songname != null && songname.equals(title)) {
                            songurls.add(ds.child("songurl").getValue(String.class));

                            String imageUrl = ds.child("imageurl").getValue(String.class);
                            if (imageUrl != null) {
                                imageurls.add(imageUrl);
                            }
                            String songAuth = ds.child("songauth").getValue(String.class);
                            if (songAuth != null) {
                                songauths.add(songAuth);
                                songAuthTextView.setText(songAuth);
                            }
                        }
                    }

                    // Nếu có ít nhất một bài hát được tìm thấy, phát bài hát đầu tiên
                    if (!songurls.isEmpty()) {
                        init(currentSongIndex);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý khi có lỗi đọc dữ liệu từ Firebase
                }


            });

        }

//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                init(viewPager2.getCurrentItem());
//                currentSongIndex = viewPager2.getCurrentItem();
//            }
//        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                currentSongIndex = currentSongIndex + 1;
                if (currentSongIndex >= songurls.size()) {
                    currentSongIndex = 0; // Nếu đã đến cuối danh sách, quay lại bài đầu tiên
                }
                init(currentSongIndex);
            }
        });

        Prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                currentSongIndex = currentSongIndex - 1;
                if (currentSongIndex >= 0) {
                    init(currentSongIndex);
                } else {
                    // Nếu đã ở bài hát đầu tiên, chuyển đến bài hát cuối cùng
                    currentSongIndex = songurls.size() - 1;
                    init(currentSongIndex);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b)
                {
                    mediaPlayer.seekTo(i*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        downloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra xem đã cấp quyền truy cập vào bộ nhớ chưa
                if (ContextCompat.checkSelfPermission(DetailActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Nếu đã cấp quyền, tải nhạc
                    downloadSong();
                } else {
                    // Nếu chưa cấp quyền, yêu cầu cấp quyền
                    ActivityCompat.requestPermissions(DetailActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        });
    }
    private void downloadSong() {
        // Lấy URL của bài hát hiện tại
        String songUrl = songurls.get(currentSongIndex);

        // Tạo đường dẫn lưu trữ cho file nhạc
        String fileName = "song_" + System.currentTimeMillis() + ".mp3";
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                + File.separator + fileName;

        // Tạo yêu cầu tải file
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(songUrl));
        request.setTitle("Đang tải nhạc"); // Tiêu đề thông báo tải xuống
        request.setDescription("Vui lòng chờ..."); // Mô tả thông báo tải xuống
        request.setDestinationUri(Uri.fromFile(new File(filePath))); // Đường dẫn lưu trữ file nhạc

        // Lấy quản lý tải xuống
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        // Gửi yêu cầu tải xuống và nhận ID của tác vụ tải xuống
        long downloadId = downloadManager.enqueue(request);

        // Hiển thị thông báo tải xuống
        Toast.makeText(this, "Đang tải nhac...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nếu đã cấp quyền, tải nhạc
                downloadSong();
            } else {
                // Nếu không cấp quyền, hiển thị thông báo
                Toast.makeText(this, "Sự cho phép đã bị từ chối. KHông thể tải nhạc", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void init(int currentItem) {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Pause.setVisibility(View.VISIBLE);
        Play.setVisibility(View.INVISIBLE);

        // Lấy thông tin của bài hát từ danh sách dựa trên currentItem
        String audioUrl = songurls.get(currentItem);

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    initializeSeekBar();
                    // Lấy URL của hình ảnh từ danh sách imageurls
                    String imageUrl = imageurls.get(currentItem);
                    // Tải và hiển thị hình ảnh bằng Glide
                    Glide.with(DetailActivity.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_launcher_background) // Drawable placeholder_image là hình ảnh mặc định khi đang tải
                            .error(R.drawable.ic_launcher_background) // Drawable error_image là hình ảnh hiển thị khi xảy ra lỗi
                            .into(imageview); // yourImageView là ImageView để hiển thị hình ảnh
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        SongName.setText(songnames.get(currentItem));
//        SongAuth.setText(songauths.get(currentItem));
    }

    private void initializeSeekBar() {
        if (mediaPlayer != null) {
            int duration = mediaPlayer.getDuration();
            int currentPosition = mediaPlayer.getCurrentPosition();

            // Kiểm tra nếu đang phát nhạc và thời gian đã phát không vượt quá thời gian của bài hát
            if (mediaPlayer.isPlaying() && currentPosition < duration) {
                seekBar.setMax(duration / 1000);
                seekBar.setProgress(currentPosition / 1000);

                // Format thời gian đã phát và thời gian còn lại
                String elapsed = String.format("%02d:%02d", currentPosition / 60000, (currentPosition % 60000) / 1000);
                Pass.setText(elapsed);

                int remaining = duration - currentPosition;
                String remainingTime = String.format("%02d:%02d", remaining / 60000, (remaining % 60000) / 1000);
                Due.setText(remainingTime);
            }
        }

        // Lặp lại hàm này mỗi giây để cập nhật thanh SeekBar
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeSeekBar();
            }
        }, 1000);
    }

    public void playpausebutton(View view){
        if(play)
        {
            play = false;
            Pause.setVisibility(View.INVISIBLE);
            Play.setVisibility(View.VISIBLE);
            mediaPlayer.pause();
        }
        else
        {
            play = true;
            Pause.setVisibility(View.VISIBLE);
            Play.setVisibility(View.INVISIBLE);
            mediaPlayer.start();
        }
    }
    private NavigationBarView.OnItemSelectedListener getListener() {
        return new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.personal:
                        startActivity(new Intent(getApplicationContext(),PlayListActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        };
    }
}


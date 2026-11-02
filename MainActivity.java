package com.example.customtvlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView tvTime, tvDateWeather;
    private ImageView btnWifi, btnInput, btnLauncherSettings, btnSystemSettings, wallpaperBackground;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable timeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClockTicker();
        setupQuickActionListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedWallpaper();
    }

    private void initViews() {
        tvTime = findViewById(R.id.tvTime);
        tvDateWeather = findViewById(R.id.tvDateWeather);
        btnWifi = findViewById(R.id.btnWifi);
        btnInput = findViewById(R.id.btnInput);
        btnLauncherSettings = findViewById(R.id.btnLauncherSettings);
        btnSystemSettings = findViewById(R.id.btnSystemSettings);
        wallpaperBackground = findViewById(R.id.wallpaperBackground);
    }

    private void setupClockTicker() {
        timeRunnable = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
                tvTime.setText(timeFormat.format(new Date()));
                tvDateWeather.setText(dateFormat.format(new Date()) + " • 24°C Clear");
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(timeRunnable);
    }

    private void loadSavedWallpaper() {
        SharedPreferences prefs = getSharedPreferences("LauncherPrefs", MODE_PRIVATE);
        String type = prefs.getString("wallpaper_type", "bing");

        if (type.equals("bing")) {
            // Bing Daily Image Feed Placeholder URL
            String bingUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb";
            Glide.with(this).load(bingUrl).centerCrop().into(wallpaperBackground);
        } else if (type.equals("custom")) {
            String uriString = prefs.getString("custom_image_uri", null);
            if (uriString != null) {
                Glide.with(this).load(Uri.parse(uriString)).centerCrop().into(wallpaperBackground);
            }
        } else if (type.equals("aerial")) {
            // Fallback render for aerial video placeholder
            Glide.with(this).load("https://images.unsplash.com/photo-1464822759023-fed622ff2c3b").centerCrop().into(wallpaperBackground);
        }
    }

    private void setupQuickActionListeners() {
        btnWifi.setOnClickListener(v -> startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)));
        
        btnInput.setOnClickListener(v -> {
            try {
                startActivity(new Intent("android.intent.action.TV_INPUT_SETTINGS"));
            } catch (Exception e) {
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });

        // Custom Launcher Preferences Menu
        btnLauncherSettings.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LauncherSettingsActivity.class)));

        // Native System Android TV Settings
        btnSystemSettings.setOnClickListener(v -> startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timeRunnable);
    }
}
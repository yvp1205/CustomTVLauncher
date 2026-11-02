package com.example.customtvlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class LauncherSettingsActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 101;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_settings);

        prefs = getSharedPreferences("LauncherPrefs", MODE_PRIVATE);

        Button btnOptBing = findViewById(R.id.btnOptBing);
        Button btnOptAerial = findViewById(R.id.btnOptAerial);
        Button btnOptLocalFolder = findViewById(R.id.btnOptLocalFolder);

        btnOptBing.setOnClickListener(v -> {
            prefs.edit().putString("wallpaper_type", "bing").apply();
            Toast.makeText(this, "Bing Wallpaper Applied", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnOptAerial.setOnClickListener(v -> {
            prefs.edit().putString("wallpaper_type", "aerial").apply();
            Toast.makeText(this, "Aerial Wallpaper Applied", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnOptLocalFolder.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                
                prefs.edit()
                     .putString("wallpaper_type", "custom")
                     .putString("custom_image_uri", imageUri.toString())
                     .apply();

                Toast.makeText(this, "Custom Storage Image Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
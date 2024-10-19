package com.example.what_if_try_lang_natin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class About_CCP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_ccp);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton backbtn = findViewById(R.id.about_back_btn);
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(About_CCP.this, Main_Home.class);
            startActivity(intent);
        });

        ImageButton facebookIco = findViewById(R.id.facebook_ico);
        facebookIco.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/culturalcenterofthephilippines/about"));
            startActivity(intent);
        });

        ImageButton instagramIco = findViewById(R.id.instagram_ico);
        instagramIco.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tinyurl.com/9tnzpfm7"));
            startActivity(intent);
        });

        ImageButton webIco = findViewById(R.id.web_ico);
        webIco.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.culturalcenter.gov.ph/"));
            startActivity(intent);
        });
    }
}

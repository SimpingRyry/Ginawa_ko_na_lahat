package com.example.what_if_try_lang_natin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Main_Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton lightButton = findViewById(R.id.light_button);
        ImageButton displayInfoButton = findViewById(R.id.display_info);
        ImageButton aboutCcpButton = findViewById(R.id.about_ccp);

        // Set click listener for the light button
        lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Home.this, Light_custom.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_middle, R.anim.exit_to_middle);
            }
        });

        //display btn
        displayInfoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Home.this, Display_info.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_middle, R.anim.exit_to_middle);
            }
        });

        //about btn
        aboutCcpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Home.this, About_CCP.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_middle, R.anim.exit_to_middle);
            }
        });
    }
}
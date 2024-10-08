package com.example.what_if_try_lang_natin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Light_custom extends AppCompatActivity {

    private String NODEMCU_IP = "http://192.168.100.85"; // Replace with your NodeMCU IP
    private OkHttpClient client;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_light_custom);
        client = new OkHttpClient();
        executorService = Executors.newSingleThreadExecutor(); // Create a single-thread executor

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back Button
        ImageButton backbtn = findViewById(R.id.back_btn);
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Light_custom.this, Main_Home.class);
            startActivity(intent);
        });

        // LED Control Switch
        Switch buildingSwitch = findViewById(R.id.switch_building);
        buildingSwitch.setChecked(false); // Set initial state to OFF

        // Image buttons for building_switch
        ImageButton standard_m = findViewById(R.id.standard_m);
        ImageButton flickery_m = findViewById(R.id.flickery_m);
        ImageButton wave_m = findViewById(R.id.wave_m);
        ImageButton building_color1 = findViewById(R.id.building_color1);
        ImageButton building_color2 = findViewById(R.id.building_color2);

        // Image buttons for fountain_switch
        ImageButton fstandard_m = findViewById(R.id.fstandard_m);
        ImageButton fflickery_m = findViewById(R.id.fflickery_m);
        ImageButton fwave_m = findViewById(R.id.fwave_m);
        ImageButton fountain_color1 = findViewById(R.id.fountain_color1);
        ImageButton fountain_color2 = findViewById(R.id.fountain_color2);

        // Initially disable all buttons
        setBuildingButtonsEnabled(false, standard_m, flickery_m, wave_m, building_color1, building_color2);
        setFountainButtonsEnabled(false, fstandard_m, fflickery_m, fwave_m, fountain_color1, fountain_color2);

        buildingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Enable buttons for building_switch
                setBuildingButtonsEnabled(true, standard_m, flickery_m, wave_m, building_color1, building_color2);
                // Turn LED ON
                controlLED(NODEMCU_IP + "/building_on");
            } else {
                // Disable buttons for building_switch
                setBuildingButtonsEnabled(false, standard_m, flickery_m, wave_m, building_color1, building_color2);
                // Reset images to unlit state
                resetBuildingButtonImages(standard_m, flickery_m, wave_m);
                // Turn LED OFF
                controlLED(NODEMCU_IP + "/building_off");
            }
        });

        // Fountain Control Switch
        Switch fountainSwitch = findViewById(R.id.switch_fountain);
        fountainSwitch.setChecked(false); // Set initial state to OFF

        fountainSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Enable buttons for fountain_switch
                setFountainButtonsEnabled(true, fstandard_m, fflickery_m, fwave_m, fountain_color1, fountain_color2);
                // Turn Fountain LED ON
                controlLED(NODEMCU_IP + "/fountain_on");
            } else {
                // Disable buttons for fountain_switch
                setFountainButtonsEnabled(false, fstandard_m, fflickery_m, fwave_m, fountain_color1, fountain_color2);
                // Reset images to unlit state
                resetFountainButtonImages(fstandard_m, fflickery_m, fwave_m);
                // Turn Fountain LED OFF
                controlLED(NODEMCU_IP + "/fountain_off");
            }
        });

        // Set click listeners for building buttons
        setBuildingButtonClickListeners(standard_m, flickery_m, wave_m, building_color1, building_color2);

        // Set click listeners for fountain buttons
        setFountainButtonClickListeners(fstandard_m, fflickery_m, fwave_m, fountain_color1, fountain_color2);
    }

    private void setBuildingButtonsEnabled(boolean isEnabled, ImageButton... buttons) {
        for (ImageButton button : buttons) {
            button.setEnabled(isEnabled);
        }
    }

    private void setFountainButtonsEnabled(boolean isEnabled, ImageButton... buttons) {
        for (ImageButton button : buttons) {
            button.setEnabled(isEnabled);
        }
    }

    private void resetBuildingButtonImages(ImageButton standard_m, ImageButton flickery_m, ImageButton wave_m) {
        standard_m.setImageResource(R.drawable.unlit_bulb);
        flickery_m.setImageResource(R.drawable.unlit_bulb);
        wave_m.setImageResource(R.drawable.unlit_bulb);
    }

    private void resetFountainButtonImages(ImageButton fstandard_m, ImageButton fflickery_m, ImageButton fwave_m) {
        fstandard_m.setImageResource(R.drawable.unlit_bulb);
        fflickery_m.setImageResource(R.drawable.unlit_bulb);
        fwave_m.setImageResource(R.drawable.unlit_bulb);
    }

    private void setBuildingButtonClickListeners(ImageButton standard_m, ImageButton flickery_m, ImageButton wave_m, ImageButton building_color1, ImageButton building_color2) {
        standard_m.setOnClickListener(v -> {
            standard_m.setImageResource(R.drawable.lighted_bulb);
            flickery_m.setImageResource(R.drawable.unlit_bulb);
            wave_m.setImageResource(R.drawable.unlit_bulb);
            controlLED(NODEMCU_IP + "/building_standard");
        });

        flickery_m.setOnClickListener(v -> {
            standard_m.setImageResource(R.drawable.unlit_bulb);
            flickery_m.setImageResource(R.drawable.lighted_bulb);
            wave_m.setImageResource(R.drawable.unlit_bulb);
            controlLED(NODEMCU_IP + "/building_flicker");
        });

        wave_m.setOnClickListener(v -> {
            standard_m.setImageResource(R.drawable.unlit_bulb);
            flickery_m.setImageResource(R.drawable.unlit_bulb);
            wave_m.setImageResource(R.drawable.lighted_bulb);
            controlLED(NODEMCU_IP + "/building_wave");
        });

        building_color1.setOnClickListener(v -> controlLED(NODEMCU_IP + "/building_white"));
        building_color2.setOnClickListener(v -> controlLED(NODEMCU_IP + "/building_mountain_dew"));
    }

    private void setFountainButtonClickListeners(ImageButton fstandard_m, ImageButton fflickery_m, ImageButton fwave_m, ImageButton fountain_color1, ImageButton fountain_color2) {
        fstandard_m.setOnClickListener(v -> {
            fstandard_m.setImageResource(R.drawable.lighted_bulb);
            fflickery_m.setImageResource(R.drawable.unlit_bulb);
            fwave_m.setImageResource(R.drawable.unlit_bulb);
            controlLED(NODEMCU_IP + "/fountain_standard");
        });

        fflickery_m.setOnClickListener(v -> {
            fstandard_m.setImageResource(R.drawable.unlit_bulb);
            fflickery_m.setImageResource(R.drawable.lighted_bulb);
            fwave_m.setImageResource(R.drawable.unlit_bulb);
            controlLED(NODEMCU_IP + "/fountain_flicker");
        });

        fwave_m.setOnClickListener(v -> {
            fstandard_m.setImageResource(R.drawable.unlit_bulb);
            fflickery_m.setImageResource(R.drawable.unlit_bulb);
            fwave_m.setImageResource(R.drawable.lighted_bulb);
            controlLED(NODEMCU_IP + "/fountain_wave");
        });

        fountain_color1.setOnClickListener(v -> controlLED(NODEMCU_IP + "/fountain_white"));
        fountain_color2.setOnClickListener(v -> controlLED(NODEMCU_IP + "/fountain_mountain_dew"));
    }

    private void controlLED(String url) {
        executorService.execute(() -> {
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    runOnUiThread(() ->
                            Toast.makeText(Light_custom.this, "LED Controlled Successfully", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(Light_custom.this, "Failed to control LED", Toast.LENGTH_SHORT).show()
                    );
                }
            } catch (IOException e) {
                Log.e("LED Control", "Error: " + e.getMessage());
                runOnUiThread(() ->
                        Toast.makeText(Light_custom.this, "Failed to control LED", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}

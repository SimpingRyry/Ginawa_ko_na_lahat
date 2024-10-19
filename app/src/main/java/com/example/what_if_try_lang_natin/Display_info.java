package com.example.what_if_try_lang_natin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Display_info extends AppCompatActivity {

    private TextView temperatureTxt, humidityTxt, airTxt;
    private Switch waterPumpSwitch;
    private DatabaseReference databaseReference;
    private String NODEMCU_IP;
    private OkHttpClient client;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display_info);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back Button
        ImageButton backbtn = findViewById(R.id.display_back_btn);
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Display_info.this, Main_Home.class);
            startActivity(intent);
        });

        // Initialize TextViews
        temperatureTxt = findViewById(R.id.temperature_txt);
        humidityTxt = findViewById(R.id.humidity_txt);
        airTxt = findViewById(R.id.air_txt);

        // Initialize the water pump switch
        waterPumpSwitch = findViewById(R.id.switch_water_pump);

        // Initialize OkHttpClient and ExecutorService
        client = new OkHttpClient();
        executorService = Executors.newSingleThreadExecutor();

        // Get the reference from Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Retrieve NodeMCU IP
        retrieveNodeMCUIP();

        // Handle water pump switch change
        waterPumpSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                controlWaterPump(NODEMCU_IP + "/water_pump_on");
            } else {
                controlWaterPump(NODEMCU_IP + "/water_pump_off");
            }
        });

        // Retrieve and display the data
        fetchAndDisplayData();
    }

    private void retrieveNodeMCUIP() {
        databaseReference.child("ipAddress").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NODEMCU_IP = dataSnapshot.getValue(String.class);
                if (NODEMCU_IP != null) {
                    NODEMCU_IP = "http://" + NODEMCU_IP;
                    Log.d("NodeMCU_IP", "Retrieved NodeMCU IP: " + NODEMCU_IP);
                } else {
                    Log.e("NodeMCU_IP", "NodeMCU IP is null");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("NodeMCU_IP", "Error retrieving IP: " + databaseError.getMessage());
            }
        });
    }

    private void controlWaterPump(String url) {
        executorService.execute(() -> {
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    Log.e("WaterPump_Control", "Failed to control water pump: " + response);
                } else {
                    Log.d("WaterPump_Control", "Water pump controlled successfully: " + response.body().string());
                }
            } catch (IOException e) {
                Log.e("WaterPump_Control", "IOException: " + e.getMessage());
            }
        });
    }

    private void fetchAndDisplayData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        Double temperature = dataSnapshot.child("temperature").getValue(Double.class);
                        Long humidity = dataSnapshot.child("humidity").getValue(Long.class);
                        String airQuality = dataSnapshot.child("air_quality").getValue(String.class);

                        if (temperature != null) {
                            temperatureTxt.setText(temperature + " °C");
                        } else {
                            temperatureTxt.setText("N/A");
                        }

                        if (humidity != null) {
                            humidityTxt.setText(humidity + " %");
                        } else {
                            humidityTxt.setText("N/A");
                        }

                        if (airQuality != null) {
                            airTxt.setText(airQuality);
                        } else {
                            airTxt.setText("N/A");
                        }
                    } else {
                        Toast.makeText(Display_info.this, "No data found in database", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Display_info", "Error parsing data: " + e.getMessage());
                    Toast.makeText(Display_info.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Display_info", "Error retrieving data: " + databaseError.getMessage());
                Toast.makeText(Display_info.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

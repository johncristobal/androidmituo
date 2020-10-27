package com.miituo.miituolibrary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.miituo.miituolibrary.R;

import org.json.JSONArray;

public class VehicleOdometer extends AppCompatActivity {

    public static JSONArray autoRead=new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_odometer);
    }
}
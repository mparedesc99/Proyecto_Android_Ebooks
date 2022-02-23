package com.example.proyecto_android_ebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();// Esto es para ocultar la barra superior que aparece por defecto con el nombre de la aplicación

    }
}
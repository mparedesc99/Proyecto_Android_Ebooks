package com.example.proyecto_android_ebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

    tx = (TextView) findViewById(R.id.toLogIn);
    tx.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                Intent it=new Intent(view.getContext(),SignUpActivity.class);
                startActivity(it);
        }
    });
    }


}
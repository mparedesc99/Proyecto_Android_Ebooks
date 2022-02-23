package com.example.proyecto_android_ebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    TextView tx;
    TextInputLayout mail;
    TextInputLayout password;


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
        mail = findViewById(R.id.mailLogin);
        password = findViewById(R.id.passwordLogin);

}

    public void login (View view){
        Toast.makeText(this, mail.getEditText().getText(), Toast.LENGTH_SHORT).show();
        if (mail.getEditText().getText().toString().equals("admin")&&password.getEditText().getText().toString().equals("admin")){
            Intent it=new Intent(view.getContext(),MainActivity.class);
            startActivity(it);
        }

    }


}
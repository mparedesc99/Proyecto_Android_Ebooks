package com.example.proyecto_android_ebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    //variables a usar
    private TextView lg_email,lg_passwd,btn_register,btnResetPasswd;
    private Button btn_login;
    private FirebaseAuth mAuth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Proyecto_Android_Ebooks);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ocultacion de la barra principal
        getSupportActionBar().hide();
        //asigancion de cada variable
        lg_email = (TextView) findViewById(R.id.lg_email);
        lg_passwd = (TextView) findViewById(R.id.lg_passwd);
        btn_register = (TextView) findViewById(R.id.lg_btn_register);
        btnResetPasswd = (TextView) findViewById(R.id.lg_btn_ResetPasswd);
        btn_login = (Button) findViewById(R.id.lg_btn_login);
        //obtenemos la instancia
        mAuth = FirebaseAuth.getInstance();
        //asignacion de el escuchador al boton registro
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });
        //asignacion de el escuchador al boton login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        //asignacion de el escuchador al boton reset de la passwd
        btnResetPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
                finish();
            }
        });

}

    private void login() {
        //asigancion de cada variable
        String email = lg_email.getText().toString();
        String passwd = lg_passwd.getText().toString();
        //comprobacion de si estan vacios entonces mostrar un error con el mensaje
        if (email.isEmpty()){
            lg_email.setError("El email no puede estar vacio");
            lg_email.requestFocus();
        }else if (passwd.isEmpty()){
            lg_passwd.setError("La contraseña no puede estar vacia");
            lg_passwd.requestFocus();
        }else {
            //llamamos a la funcion de mauth sign para validar el email y la contraseña en authentication
            mAuth.signInWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //mensaje de registro correcto
                        Toast.makeText(LoginActivity.this, "Inició sesión con éxito", Toast.LENGTH_SHORT).show();
                        //se cambia de activity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        //finalizamos la activity actual
                        finish();
                    }else{
                        //mensaje de error
                        Toast.makeText(LoginActivity.this, "Error de inicio de sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        setDayNight();
    }

    public void setDayNight(){
        // 0 = modo oscuro
        // 1 = modo claro
        SharedPreferences sp = getSharedPreferences("sp", this.MODE_PRIVATE);
        int theme = sp.getInt("Theme", 1);
        if (theme==0){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}
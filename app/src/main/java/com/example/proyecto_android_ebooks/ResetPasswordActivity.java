package com.example.proyecto_android_ebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    //variables a usar
    private TextView rec_email,rec_btnVolver;
    private Button rec_btn_enviar;
    private FirebaseAuth mAuth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //ocultacion de la barra principal
        getSupportActionBar().hide();
        //obtenemos la instancia
        mAuth = FirebaseAuth.getInstance();
        //asigancion de cada variable
        rec_email = (TextView) findViewById(R.id.rec_email);
        rec_btnVolver = (TextView) findViewById(R.id.rec_btn_volver);
        rec_btn_enviar = (Button) findViewById(R.id.rec_btn_lenviar);
        //asignacion de el escuchador al boton enviar
        rec_btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = rec_email.getText().toString();
                if (email.isEmpty()){
                    rec_email.setError("El email no puede estar vacio");
                    rec_email.requestFocus();
                }else {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ResetPasswordActivity.this, "Comprueba tú email", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(ResetPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        rec_btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                finish();
            }
        });


    }
}
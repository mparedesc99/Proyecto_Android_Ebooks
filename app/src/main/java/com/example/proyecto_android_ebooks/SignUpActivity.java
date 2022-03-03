package com.example.proyecto_android_ebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {
    //variables a usar
    private TextView rg_name,rg_email,rg_passwd,rg_passwdAgain,rg_btn_toLogin;
    private EditText rg_date;
    private CheckBox rg_vendedor,rg_terminos;
    private Button rg_btn_register;
    private FirebaseAuth mAuth;
    // Guardar el último año, mes y día del mes
    private int ultimoAnio, ultimoMes, ultimoDiaDelMes;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //ocultacion de la barra principal
        getSupportActionBar().hide();
        //asigancion de cada variable
        rg_date = (EditText) findViewById(R.id.rg_date);
        rg_name = (TextView) findViewById(R.id.rg_name);
        rg_email = (TextView) findViewById(R.id.rg_email);
        rg_passwd = (TextView) findViewById(R.id.rg_passwd);
        rg_passwdAgain = (TextView) findViewById(R.id.rg_passwdAgain);
        rg_btn_toLogin = (TextView) findViewById(R.id.rg_toLogIn);
        rg_vendedor = (CheckBox) findViewById(R.id.rg_checkVendedor);
        rg_terminos = (CheckBox) findViewById(R.id.rg_checkTerminos);
        rg_btn_register = (Button) findViewById(R.id.rg_btn_login);
        //obtenemos la instancia
        mAuth = FirebaseAuth.getInstance();
        //asignacion de el escuchador al boton registro
        rg_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //llamamos al metodo createUser
                createUser();
            }
        });
        //asignacion de el escuchador al boton toLogin
        rg_btn_toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cambiamos de activity a la de el login
                startActivity(new Intent(view.getContext(),LoginActivity.class));
                //cerramos la actual que es la de el registro
                finish();
            }
        });
        //asignacion de el escuchador al edittext de fecha
        rg_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí es cuando dan click así que mostramos el DatePicker
                // Le pasamos lo que haya en las globales
                DatePickerDialog dialogoFecha = new DatePickerDialog(SignUpActivity.this, listenerDeDatePicker, ultimoAnio, ultimoMes, ultimoDiaDelMes);
                //Mostrar
                dialogoFecha.show();
            }
        });
    }


    private void createUser() {
        //asigancion de cada variable
        String name = rg_name.getText().toString();
        String date = rg_date.getText().toString();
        String email = rg_email.getText().toString();
        String passwd = rg_passwd.getText().toString();
        String passwdAgain = rg_passwdAgain.getText().toString();
        Boolean vendedor = rg_vendedor.isChecked();
        Boolean terminos = rg_terminos.isChecked();
        //comprobacion de si estan vacios entonces mostrar un error con el mensaje
        if (name.isEmpty()){
            rg_name.setError("El nombre no puede estar vacio");
            rg_name.requestFocus();
        }else if (date.isEmpty()){
            rg_date.setError("La fecha no puede estar vacia");
            rg_date.requestFocus();
        }else if (email.isEmpty()){
            rg_email.setError("El email no puede estar vacio");
            rg_email.requestFocus();
        }else if (passwd.isEmpty()){
            rg_passwd.setError("La contraseña no puede estar vacia");
            rg_passwd.requestFocus();
        }else if (passwdAgain.isEmpty()){
            rg_passwdAgain.setError("La contraseña repetida no puede estar vacia");
            rg_passwdAgain.requestFocus();
        }else if (!passwd.equals(passwdAgain)){
            rg_passwd.setError("Las contraseñas deben coincidir");
            rg_passwdAgain.setError("Las contraseñas deben coincidir");
            rg_passwd.requestFocus();
            rg_passwdAgain.requestFocus();
        }else if (terminos == false){
            rg_terminos.requestFocus();
            Toast.makeText(SignUpActivity.this, "Debes aceptar los terminos y condiciones", Toast.LENGTH_SHORT).show();
        }else {
            //una vez los campos esten listos creamos al user
            User user = new User(name,date,email,passwd,vendedor);
            //llamamos a la funcion de mauth para crear el usuario en authentication con el email y contraseña
            mAuth.createUserWithEmailAndPassword(email,passwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //con estas dos lineas vamos a crear el usuario al mismo tiempo en la base de datos real time con todos sus campos pasandole el user
                        //con esto obtenemos el uuid de usuario en linea
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        //generemos el apartado dentro de la base de datos con el nombre Users
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        //pasamos el uuid y el usuario creado
                        databaseReference.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //se manda la notificacion
                                firebaseUser.sendEmailVerification();
                                //mensaje de registro correcto
                                Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                // se cambia de activity
                                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                // finalizamos la activity actual
                                finish();
                            }
                        });
                    }else{
                        //mensaje de error
                        Toast.makeText(SignUpActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
            
        }

    }

    // Crear un listener del datepicker;
    private DatePickerDialog.OnDateSetListener listenerDeDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int anio, int mes, int diaDelMes) {
            // Esto se llama cuando seleccionan una fecha. Nos pasa la vista, pero más importante, nos pasa:
            // El año, el mes y el día del mes. Es lo que necesitamos para saber la fecha completa
            // Refrescamos las globales
            ultimoAnio = anio;
            ultimoMes = mes;
            ultimoDiaDelMes = diaDelMes;
            // Y refrescamos la fecha
            refrescarFechaEnEditText();
        }
    };

    public void refrescarFechaEnEditText() {
        rg_date.setText(ultimoDiaDelMes+"-"+(ultimoMes+1)+"-"+ultimoAnio);
    }


}
package com.example.proyecto_android_ebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertLibrosActivity extends AppCompatActivity {
    //variables a usar
    private Button ins_btnInsert,ins_btnVolver;
    private EditText ins_isbn,ins_titulo,ins_autor,ins_descripcion,ins_texto ;
    private DatabaseReference database;
    private String uuid;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(InsertLibrosActivity.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_libros);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //asigancion de cada variable
        ins_btnInsert = (Button) findViewById(R.id.ins_btnInsert);
        ins_btnVolver = (Button) findViewById(R.id.ins_btnVolver);
        ins_isbn = (EditText) findViewById(R.id.ins_isbn);
        ins_titulo = (EditText) findViewById(R.id.ins_titulo);
        ins_autor = (EditText) findViewById(R.id.ins_autor);
        ins_descripcion = (EditText) findViewById(R.id.ins_descripcion);
        ins_texto = (EditText) findViewById(R.id.ins_txt);
        //obtenemos la instancia
        database = FirebaseDatabase.getInstance().getReference();
        //obtenemos el uuid de user en linea
        uuid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        //asignacion de el escuchador al boton volver
        ins_btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsertLibrosActivity.this,MainActivity.class));
                finish();
            }
        });
        //asignacion de el escuchador al boton insert
        ins_btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData();
            }
        });

    }

    private void InsertData() {
        //asigancion de cada variable
        String isbn = ins_isbn.getText().toString();
        String titulo = ins_titulo.getText().toString();
        String autor = ins_autor.getText().toString();
        String descripcion = ins_descripcion.getText().toString();
        String historia = ins_texto.getText().toString();
        // genera un uuid aleatorio
        String id = database.push().getKey();
        //creamos el objeto libro
        Libro libro = new Libro(id,isbn,titulo,autor,descripcion, historia);
        //primero guardamos los libros por vendedor
        database.child(uuid).child(id).setValue(libro).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
        //segundo guardamos los libros en ebooks donde se almacenaran todos de manera global
        database.child("Ebooks").child(id).setValue(libro).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(InsertLibrosActivity.this,"Libro insertado",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
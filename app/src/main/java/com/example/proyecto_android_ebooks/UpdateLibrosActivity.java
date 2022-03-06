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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateLibrosActivity extends AppCompatActivity {

    //variables a usar
    private Button upd_btnUpdate,upd_btnDelete,upd_btnVolver;
    private EditText upd_isbn,upd_titulo,upd_autor,upd_descripcion, udp_txt;
    private DatabaseReference databaseReference;
    private String uuid,id;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UpdateLibrosActivity.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_libros);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        upd_btnUpdate = (Button) findViewById(R.id.upd_btnUpdate);
        upd_btnDelete = (Button) findViewById(R.id.upd_btnDelete);
        upd_btnVolver = (Button) findViewById(R.id.upd_btnVolver);
        upd_isbn = (EditText) findViewById(R.id.upd_isbn);
        upd_titulo = (EditText) findViewById(R.id.upd_titulo);
        upd_autor = (EditText) findViewById(R.id.upd_autor);
        upd_descripcion = (EditText) findViewById(R.id.upd_descripcion);
        udp_txt = (EditText) findViewById(R.id.upd_txt);
        //obtenemos el uuid de el usuario en linea
        uuid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        //obtenemos la instancia
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //usamos el paquete bundle para el traspaso de informacion
        Bundle libro = getIntent().getExtras();
        if(libro !=null){
            //valores traidos del mainActivity de el libro clickeado
            String isbn = libro.getString("isbn");
            String titulo = libro.getString("titulo");
            String autor = libro.getString("autor");
            String descripcion = libro.getString("descripcion");
            String txt = libro.getString("historia");
            //id de el libro
            id = libro.getString("id");
            upd_isbn.setText(isbn);
            upd_titulo.setText(titulo);
            upd_autor.setText(autor);
            upd_descripcion.setText(descripcion);
            udp_txt.setText(txt);
        }

        upd_btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBook();
            }
        });

        upd_btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook();
            }
        });

        upd_btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateLibrosActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void deleteBook() {
        //obtenemos la referencia de el libro sobre la base de datos real time pasandole el uuid del vendedor y el id del libro
        databaseReference.child(uuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(UpdateLibrosActivity.this,"Libro eliminado",Toast.LENGTH_LONG).show();
                //Primero eliminamos el libro de el vendedor en linea pasandole el uuid y el id del libro
                FirebaseDatabase.getInstance().getReference(uuid).child(id).removeValue();
                //Segundo eliminamos el libro de el vendedor en linea en Ebooks con el id del libro
                FirebaseDatabase.getInstance().getReference("Ebooks").child(id).removeValue();


                //como referencia se le pasa users desde ahi obtendremos el uuid de cada user para ir buscando coincidencias
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                //se ha usado el singlevalue porque sino hace un bucle al buscar
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot datasnapshot : snapshot.getChildren()){
                            //aqui consigo el uuid de un usuario y lo paso por referencia
                            String uuidUser = datasnapshot.getKey();
                            //el uuid se le pasa a la nueva declaracion de referenceuser para ir buscando dentro el cada user especifico
                            DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference(datasnapshot.getKey());
                            referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot datasnapshot : snapshot.getChildren()){
                                        //si coinciden el id del libro con el de el user entra
                                        if (datasnapshot.getKey().equals(id)){
                                            //tercero eliminamos los libros en el usuario
                                            FirebaseDatabase.getInstance().getReference(uuidUser).child(id).removeValue();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                Toast.makeText(UpdateLibrosActivity.this,"Libro eliminado",Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateLibrosActivity.this,MainActivity.class));
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateBook() {
        //asigancion de cada variable
        String isbn = upd_isbn.getText().toString();
        String titulo = upd_titulo.getText().toString();
        String autor = upd_autor.getText().toString();
        String descripcion = upd_descripcion.getText().toString();
        String txt =udp_txt.getText().toString();
        //creamos el objeto libro
        Libro libro = new Libro(id,isbn,titulo,autor,descripcion, txt);
        //primero actualizamos los libros por vendedor
        databaseReference.child(uuid).child(id).setValue(libro).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
        //segundo actualizamos los libros en ebooks donde se almacenaran todos de manera global
        databaseReference.child("Ebooks").child(id).setValue(libro).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });

        //como referencia se le pasa users desde ahi obtendremos el uuid de cada user para ir buscando coincidencias
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        //se ha usado el singlevalue porque sino hace un bucle al buscar
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()){
                    //aqui consigo el uuid de un usuario y lo paso por referencia
                    String uuidUser = datasnapshot.getKey();
                    //el uuid se le pasa a la nueva declaracion de referenceuser para ir buscando dentro el cada user especifico
                    DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference(datasnapshot.getKey());
                    referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot datasnapshot : snapshot.getChildren()){
                                //si coinciden el id del libro con el de el user entra
                                if (datasnapshot.getKey().equals(id)){
                                    //tercero actualizamos los libros en el usuario
                                    databaseReference.child(uuidUser).child(id).setValue(libro).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    });
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Toast.makeText(UpdateLibrosActivity.this,"Libro actualizado",Toast.LENGTH_LONG).show();
    }




}
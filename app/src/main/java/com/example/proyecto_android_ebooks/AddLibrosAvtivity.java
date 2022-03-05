package com.example.proyecto_android_ebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//implementacion para el click en el recyclerview implements MyAdapter.ListItemClickListener
public class AddLibrosAvtivity extends AppCompatActivity implements MyAdapter.ListItemClickListener{
    //variables a usar
    private RecyclerView recyclerView;
    private ArrayList<Libro> list;
    private MyAdapter myAdapter;
    private DatabaseReference databaseReference, databaseUser,referenceCopy;
    private String uuid;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddLibrosAvtivity.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_libros);
        //ocultacion de la barra principal
        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.add_recyclerView);
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(this,list,this);
        recyclerView.setAdapter(myAdapter);
        //obtenemos el uuid de user en linea
        uuid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        //obtenemos la instancia
        databaseUser = FirebaseDatabase.getInstance().getReference();
        //como referencia se le pasa los ebooks
        databaseReference = FirebaseDatabase.getInstance().getReference("Ebooks");
        //obtenemos todos los libros de la base de datos a la que le hemos pasado el uuid anteriormente en referenceUser
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()){
                    Libro libro =  datasnapshot.getValue(Libro.class);
                    list.add(libro);
                }
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    //implementacion para el click en el recyclerview
    @Override
    public void onListItemClick(int position) {
            //asigancion de cada variable
            String isbn = list.get(position).getIsbn().toString();
            String titulo = list.get(position).getTitulo().toString();
            String autor = list.get(position).getAutor().toString();
            String descripcion = list.get(position).getDescripcion().toString();
            String id = list.get(position).getId();
            String txt = list.get(position).getHistoria();
            //creamos el objeto libro
            Libro libro = new Libro(id,isbn,titulo,autor,descripcion, txt);
            //primero guardamos los libros por vendedor
            databaseUser.child(uuid).child(id).setValue(libro).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AddLibrosAvtivity.this,"Libro agregado",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddLibrosAvtivity.this,MainActivity.class));
                        finish();
                    }
                }
            });
    }
}
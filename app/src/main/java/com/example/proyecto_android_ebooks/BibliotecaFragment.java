package com.example.proyecto_android_ebooks;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//este fragment va vacio
public class BibliotecaFragment extends Fragment implements MyAdapter.ListItemClickListener{

    private RecyclerView recyclerView;
    private ArrayList<Libro> list;
    private MyAdapter myAdapter;
    private DatabaseReference databaseReference,referenceUser;
    private String uuid;
    private boolean isVendedor;

    public BibliotecaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_biblioteca, container, false);

        recyclerView = view.findViewById(R.id.recyclerBiblioteca);
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myAdapter = new MyAdapter(getContext(),list,this);
        recyclerView.setAdapter(myAdapter);
        //obtenemos el uuid de el usuario en linea
        uuid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        //como referencia se le pasa el uuid para crearla con ese nombre
        databaseReference = FirebaseDatabase.getInstance().getReference(uuid);
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
        //obtenemos la referencia de el usuario sobre la base de datos real time pasandole su uuid
        referenceUser = FirebaseDatabase.getInstance().getReference("Users").child(uuid);
        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Obtenemos los valores de un usuario en especifico en este caso solo necesitamos el boolean vendedor
                User user = snapshot.getValue(User.class);
                isVendedor = user.getVendedor();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return view;
    }

    //implementacion para el click en el recyclerview
    @Override
    public void onListItemClick(int position) {
        if (isVendedor == true){
            //mandamos a traves de el intent los valores de el libro clickeado
            Intent it = new Intent(getContext(), UpdateLibrosActivity.class);
            it.putExtra("id", list.get(position).getId());
            it.putExtra("isbn", list.get(position).getIsbn().toString());
            it.putExtra("titulo", list.get(position).getTitulo().toString());
            it.putExtra("autor", list.get(position).getAutor().toString());
            it.putExtra("descripcion", list.get(position).getDescripcion().toString());
            startActivity(it);

        }else {
            //mandamos a traves de el intent los valores de el libro clickeado
            Intent it = new Intent(getContext(), ViewLibrosActivity.class);
            it.putExtra("id", list.get(position).getId());
            it.putExtra("isbn", list.get(position).getIsbn().toString());
            it.putExtra("titulo", list.get(position).getTitulo().toString());
            it.putExtra("autor", list.get(position).getAutor().toString());
            it.putExtra("descripcion", list.get(position).getDescripcion().toString());
            startActivity(it);

        }
    }

}
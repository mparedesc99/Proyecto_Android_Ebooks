package com.example.proyecto_android_ebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    //variables a usar
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private DatabaseReference referenceUser;
    private FloatingActionButton add;
    private String uuid;
    private boolean isVendedor;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //se cierra sesion si se vuelve al login
        Toast.makeText(MainActivity.this,"Sesión cerrada",Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Esto es para ocultar la barra superior que aparece por defecto con el nombre de la aplicación
        getSupportActionBar().hide();
        //declaracion de el fragment precargado con el que inicia
        fragment = new BibliotecaFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,fragment).commit();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.btnBar);
        bottomNavigationView.setBackground(null);
        //conseguimos el click de el item del menu y cambiamos de fragment
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()){
                   case R.id.biblioteca:
                       fragment = new BibliotecaFragment();
                       break;
                   case R.id.busqueda:
                       fragment = new BusquedaFragment();
                       break;
                   case R.id.perfil:
                       fragment = new PerfilFragment();
                       break;
                   case R.id.ajustes:
                       fragment = new AjustesFragment();
                       break;
               }
               getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,fragment).commit();
               return true;
            }
        });

        //obtenemos el uuid de el usuario en linea
        uuid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        add = (FloatingActionButton) findViewById(R.id.mn_btnAdd);

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
        //asignacion de el escuchador al boton add si es vendedor inflara una vista y si no otra
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (isVendedor == true){
                   startActivity(new Intent(MainActivity.this, InsertLibrosActivity.class));
                   finish();
               }else {
                   startActivity(new Intent(MainActivity.this, AddLibrosAvtivity.class));
                   finish();
               }
            }
        });

    }

}
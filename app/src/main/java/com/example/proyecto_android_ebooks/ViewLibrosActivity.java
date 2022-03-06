package com.example.proyecto_android_ebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewLibrosActivity extends AppCompatActivity {
    //variables a usar
    private Button view_btnDelete,view_btnVolver;
    private TextView view_isbn,view_titulo,view_autor,view_descripcion, txt;
    private ArrayList<Libro> list;
    private DatabaseReference databaseReference;
    private String uuid,id;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewLibrosActivity.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_libros);

        //asigancion de cada variable
        view_btnDelete = (Button) findViewById(R.id.view_btnDelete);
        view_btnVolver = (Button) findViewById(R.id.view_btnVolver);
        view_isbn = (TextView) findViewById(R.id.view_isbnText);
        view_titulo = (TextView) findViewById(R.id.view_tituloText);
        view_autor = (TextView) findViewById(R.id.view_autorText);
        view_descripcion = (TextView) findViewById(R.id.view_descripcionText);
        txt=(TextView) findViewById(R.id.view_Text);
        //obtenemos el uuid de el usuario en linea
        uuid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        //usamos el paquete bundle para el traspaso de informacion
        Bundle libro = getIntent().getExtras();
        if(libro !=null){
            //valores traidos del mainActivity de el libro clickeado
            String isbn = libro.getString("isbn");
            String titulo = libro.getString("titulo");
            String autor = libro.getString("autor");
            String descripcion = libro.getString("descripcion");
            String texto = libro.getString("historia");
            //id de el libro
            id = libro.getString("id");
            view_isbn.setText(isbn);
            view_titulo.setText(titulo);
            view_autor.setText(autor);
            view_descripcion.setText(descripcion);
            txt.setText(texto);
        }

        view_btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewLibrosActivity.this,MainActivity.class));
                finish();
            }
        });

        view_btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook();
            }
        });

    }

    private void deleteBook() {
        //obtenemos la referencia de el libro sobre la base de datos real time pasandole el uuid del vendedor y el id del libro
        databaseReference = FirebaseDatabase.getInstance().getReference(uuid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(ViewLibrosActivity.this,"Libro eliminado",Toast.LENGTH_LONG).show();
                //eliminamos el libro de el cliente en linea pasandole el uuid y el id del libro
                FirebaseDatabase.getInstance().getReference(uuid).child(id).removeValue();
                startActivity(new Intent(ViewLibrosActivity.this,MainActivity.class));
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}
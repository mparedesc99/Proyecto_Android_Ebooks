package com.example.proyecto_android_ebooks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PerfilFragment extends Fragment {

    private TextView pr_name, pr_date, pr_email;
    private CheckBox pr_writter;
    private DatabaseReference databaseReference;
    private String uuid;

    public PerfilFragment() {
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
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        pr_name = (TextView) view.findViewById(R.id.pr_nameText);
        pr_date = (TextView) view.findViewById(R.id.pr_dateText);
        pr_email = (TextView) view.findViewById(R.id.pr_emailText);
        pr_writter = (CheckBox) view.findViewById(R.id.pr_writter);

        //obtenemos el uuid de el usuario en linea
        uuid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        //como referencia se le pasa la tabla users y accedemos al user en linea con su uuid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uuid);
        //obtenemos todos los datos del user
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user =  snapshot.getValue(User.class);
                    pr_name.setText(user.getName().toString());
                    pr_date.setText(user.getDate().toString());
                    pr_email.setText(user.getEmail().toString());
                    pr_writter.setChecked(user.getVendedor());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        return view;
    }
}
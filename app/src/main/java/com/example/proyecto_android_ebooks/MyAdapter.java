package com.example.proyecto_android_ebooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    //variables a usar
    private Context context;
    private ArrayList<Libro> list;
    private ListItemClickListener mOnClickListener;
    //implementacion para el click en el recyclerview ListItemClickListener onClickListener
    public MyAdapter(Context context, ArrayList<Libro> list, ListItemClickListener onClickListener) {
        this.context = context;
        this.list = list;
        this.mOnClickListener = onClickListener;
    }
    //implementacion para el click en el recyclerview
    interface ListItemClickListener{
        void onListItemClick(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_libros,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //obtenemos la poss de el libro y seteamos cada valor
        Libro libro = list.get(position);
        holder.isbn.setText(libro.getIsbn());
        holder.titulo.setText(libro.getTitulo());
        holder.autor.setText(libro.getAutor());
        holder.descripcion.setText(libro.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    //implementacion para el click en el recyclerview implements View.OnClickListener
    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        //variables a usar
        TextView isbn,titulo,autor,descripcion;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // asignamos las variables
            isbn = itemView.findViewById(R.id.tvISBN);
            titulo = itemView.findViewById(R.id.tvTitulo);
            autor = itemView.findViewById(R.id.tvAutor);
            descripcion = itemView.findViewById(R.id.tvDescripcion);
            //implementacion para el click en el recyclerview
            itemView.setOnClickListener(this);
        }
        //metodo de View.OnClickListener
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }
    }

}

package com.example.proyecto_android_ebooks;

public class Libro {
    private String id,isbn,titulo,autor,descripcion, historia;

    public Libro() {
        //constructor requerido
    }

    public Libro(String id,String isbn, String titulo, String autor, String descripcion, String historia) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.descripcion = descripcion;
        this.historia = historia;
    }

    public String getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getHistoria() {
        return historia;
    }

}

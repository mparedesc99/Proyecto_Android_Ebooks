package com.example.proyecto_android_ebooks;
//Clase usuario con su constructor y atributos
public class User {
    String name,date,email,passwd;
    Boolean vendedor;

    public User() {
        //constructor requerido
    }

    public User(String name, String date, String email, String passwd, Boolean vendedor) {
        this.name = name;
        this.date = date;
        this.email = email;
        this.passwd = passwd;
        this.vendedor = vendedor;
    }

    public String getName() {
        return name;
    }

    public String getDate() {return date;}

    public String getEmail() {
        return email;
    }

    public String getPasswd() {
        return passwd;
    }

    public Boolean getVendedor() {
        return vendedor;
    }
}

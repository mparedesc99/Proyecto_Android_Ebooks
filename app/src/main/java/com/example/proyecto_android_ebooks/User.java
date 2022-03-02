package com.example.proyecto_android_ebooks;
//Clase usuario con su constructor y atributos
public class User {
    String name,email,passwd;
    Boolean vendedor;

    public User() {
        //constructor requerido
    }

    public User(String name, String email, String passwd, Boolean vendedor) {
        this.name = name;
        this.email = email;
        this.passwd = passwd;
        this.vendedor = vendedor;
    }

    public String getName() {
        return name;
    }

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

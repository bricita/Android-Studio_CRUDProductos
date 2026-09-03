package com.example.semana2_trabajo.modelo;

public class Producto {
    private Integer id; // Usamos Integer para que pueda ser null
    private String nombre;
    private double precio;

    // Constructor vacío (necesario para muchas librerías de JSON)
    public Producto() {}

    // Constructor completo
    public Producto(Integer id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}

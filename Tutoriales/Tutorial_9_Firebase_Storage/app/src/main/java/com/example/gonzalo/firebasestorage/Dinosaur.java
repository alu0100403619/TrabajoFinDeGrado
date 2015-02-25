package com.example.gonzalo.firebasestorage;

/**
 * Created by Gonzalo on 25/02/2015.
 */
public class Dinosaur {

    private double altura;
    private long aparecido;
    private long desaparecido;
    private double longitud;
    private String orden;
    private double peso;

    public Dinosaur() {
        orden = "";
        altura = longitud = peso = 0.0;
        aparecido = desaparecido = 0;
    }

    public Dinosaur (double hight, long appear, long disappear, double length,
                     String order, double weight) {

        setAltura(hight);
        setAparecido(appear);
        setDesaparecido(disappear);
        setLongitud(length);
        setOrden(order);
        setPeso(weight);
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public void setAparecido(long aparecido) {
        this.aparecido = aparecido;
    }

    public void setDesaparecido(long desaparecido) {
        this.desaparecido = desaparecido;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public double getAltura() {
        return altura;
    }

    public long getAparecido() {
        return aparecido;
    }

    public long getDesaparecido() {
        return desaparecido;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getOrden() {
        return orden;
    }

    public double getPeso() {
        return peso;
    }


}

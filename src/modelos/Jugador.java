package modelos;

import java.util.ArrayList;

public class Jugador {

    private String nombre;
    private int puntos;
    private ArrayList<Carta> cartas;

    public Jugador(String nombre){
        this.nombre = nombre;
        puntos = 0;
        cartas = new ArrayList<>();
    }

    public String getNombre(){
        return nombre;
    }

    public int getPuntos(){
        return puntos;
    }

    public ArrayList<Carta> getCartas(){
        return cartas;
    }

    public void recibirCarta(Carta carta){
        cartas.add(carta);
    }

    public Carta jugarCarta(int numeroCarta){
        return cartas.remove(numeroCarta);
    }

    public String mostrarCartas(){
        StringBuilder stringCartas = new StringBuilder();
        int nroCarta = 0;
        for (Carta carta : cartas){
            stringCartas.append("(" + nroCarta++ + ") - " + carta.toString()  + "\n");
        }
        return stringCartas.toString();
    }

    public void darPuntos(int puntos){
        this.puntos += puntos;
    }
}

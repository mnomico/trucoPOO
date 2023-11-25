package modelos;

import java.util.ArrayList;

public class Jugador {

    private String nombre;
    private int puntos;
    private ArrayList<Carta> cartas;
    private ArrayList<Carta> cartasJugadas;

    public Jugador(String nombre){
        this.nombre = nombre;
        puntos = 0;
        cartas = new ArrayList<>();
        cartasJugadas = new ArrayList<>();
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

    public Carta getUltimaCartaJugada(){
        if (cartasJugadas.isEmpty()){
            return null;
        } else return cartasJugadas.get(cartasJugadas.size()-1);
    }

    public void recibirCarta(Carta carta){
        cartas.add(carta);
    }

    public void jugarCarta(int numeroCarta){
        Carta carta = cartas.remove(numeroCarta);
        cartasJugadas.add(carta);
    }

    public String mostrarCartas(){
        StringBuilder stringCartas = new StringBuilder();
        int nroCarta = 0;
        for (Carta carta : cartas){
            stringCartas.append(nroCarta++ + " - " + carta.toString()  + "\n");
        }
        return stringCartas.toString();
    }
}

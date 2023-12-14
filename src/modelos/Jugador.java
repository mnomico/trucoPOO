package modelos;

import java.util.ArrayList;
import java.util.Arrays;

public class Jugador {

    private final String nombre;
    private int puntos;
    private final ArrayList<Carta> cartas;
    private int tanto;

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

    public int getTanto(){
        return tanto;
    }

    public ArrayList<Carta> devolverCartas(){
        return cartas;
    }

    public void removerCartas(){
        cartas.clear();
    }

    public void recibirCarta(Carta carta){
        cartas.add(carta);
    }

    public Carta jugarCarta(int numeroCarta){
        return cartas.remove(numeroCarta);
    }

    public ArrayList<String> mostrarCartas(){
        ArrayList<String> cartas = new ArrayList<>();
        for (Carta carta : this.cartas){
            cartas.add(carta.toString());
        }
        return cartas;
    }

    /*
    public String mostrarCartas(){
        StringBuilder stringCartas = new StringBuilder();
        int nroCarta = 1;
        for (Carta carta : cartas){
            stringCartas.append("\t           (").append(nroCarta++).append(") - ").append(carta.toString()).append("\n");
        }
        return stringCartas.toString();
    }
    */

    public void darPuntos(int puntos){
        this.puntos += puntos;
    }

    public void calcularTanto(){

        int[] numerosCartas = new int[3];

        for (int i = 0; i < 3; i++){
            int numero = cartas.get(i).getNumero();
            if (numero < 10) {
                numerosCartas[i] = numero;
            }
        }

        // Si las tres cartas tienen el mismo palo, retorna 20 + el valor de las dos cartas más altas
        Palo palo0 = cartas.get(0).getPalo();
        Palo palo1 = cartas.get(1).getPalo();
        Palo palo2 = cartas.get(2).getPalo();

        if (palo0 == palo1 && palo1 == palo2){
            Arrays.sort(numerosCartas);
            tanto = 20 + numerosCartas[2] + numerosCartas[1];
            return;
        }

        // Si hay por lo menos dos cartas con el mismo palo, retorna 20 + el valor de dichas cartas

        if (palo0 == palo1){
            tanto = 20 + numerosCartas[0] + numerosCartas[1];
            return;
        }

        if (palo1 == palo2){
            tanto = 20 + numerosCartas[1] + numerosCartas[2];
            return;
        }

        if (palo2 == palo0){
            tanto = 20 + numerosCartas[2] + numerosCartas[0];
            return;
        }

        // Si las cartas son de distintos palos, retorna la carta con número más alto
        Arrays.sort(numerosCartas);
        tanto = numerosCartas[2];
    }

}

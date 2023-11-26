package modelos;

import java.util.ArrayList;
import java.util.Arrays;

public class Jugador {

    private String nombre;
    private int puntos;
    private ArrayList<Carta> cartas;
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

    public void calcularTanto(){

        // TODO esto no funciona, hay que buscar otra manera

        Carta[] arrayCarta = new Carta[3];

        for (int i = 0; i < 3; i++){
            Carta carta = cartas.get(i);
            if (carta.getNumero() >= 10) {
                carta = new Carta(0, carta.getPalo());
            }
            arrayCarta[i] = carta;
        }

        Arrays.sort(arrayCarta);

        // Si las tres cartas tienen el mismo palo, retorna 20 + el valor de las dos cartas más altas
        Palo palo0 = arrayCarta[0].getPalo();
        Palo palo1 = arrayCarta[1].getPalo();
        Palo palo2 = arrayCarta[2].getPalo();

        if (palo0 == palo1 && palo1 == palo2){
            tanto = 20 + arrayCarta[2].getNumero() + arrayCarta[1].getNumero();
            return;
        }

        // Si hay por lo menos dos cartas con el mismo palo, retorna 20 + el valor de dichas cartas

        if (palo0 == palo1){
            tanto = 20 + arrayCarta[0].getNumero() + arrayCarta[1].getNumero();
            return;
        }

        if (palo1 == palo2){
            tanto = 20 + arrayCarta[1].getNumero() + arrayCarta[2].getNumero();
            return;
        }

        if (palo2 == palo0){
            tanto = 20 + arrayCarta[2].getNumero() + arrayCarta[0].getNumero();
            return;
        }

        // Si las cartas son de distintos palos, retorna la carta con número más alto
        tanto = arrayCarta[2].getNumero();
    }

}

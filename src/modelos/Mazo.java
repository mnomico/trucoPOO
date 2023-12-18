package modelos;

import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;

public class Mazo implements Serializable {
    private final Stack<Carta> mazo = new Stack<>(); // pila en donde se guardan las cartas

    public Mazo(){
        // array en donde se guardan los palos
        Palo[] palos = Palo.values();
        for (Palo palo : palos){
            for (int numero = 1; numero <= 12; numero++) {
                if (numero != 8 && numero != 9) {
                    Carta nuevaCarta = new Carta(numero, palo); // cargo la carta con nro y palo
                    mazo.add(nuevaCarta); // la agrego al mazo
                }
            }
        }
    }

    public void mezclarMazo(){
        Collections.shuffle(mazo);
    }

    public void repartirCartas(Jugador jugador1, Jugador jugador2){
        Carta carta;
        for (int i = 1; i <= 3; i++){
            carta = mazo.pop();
            jugador1.recibirCarta(carta);
            carta = mazo.pop();
            jugador2.recibirCarta(carta);
        }
    }

    public void recibirCarta(Carta carta){
        mazo.push(carta);
    }

}
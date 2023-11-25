package modelos;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ModeloTruco extends Observable {
    private ArrayList<Observer> observers;

    private Mazo mazo;
    private int numeroMano;
    private int numeroRonda;

    private Jugador jugadorActual;
    private Jugador ganadorRonda;

    private Jugador jugador1;
    private Carta cartaJ1;

    private Jugador jugador2;
    private Carta cartaJ2;

    private boolean envidoCantado;
    private boolean trucoCantado;

    public ModeloTruco(){
        observers = new ArrayList<>();
        numeroMano = 0;
        numeroRonda = 0;
        mazo = new Mazo();
    }

    public void setObservers(Observer observer){
        observers.add(observer);
    }

    public void notificar(Evento evento) {
        for (Observer observer : observers){
            observer.update(this, evento);
        }
        cambiarTurno();
        for (Observer observer : observers){
            observer.update(this, Evento.CAMBIO_TURNO);
        }
    }

    public int getNumeroMano(){
        return numeroMano;
    }

    public int getNumeroRonda(){
        return numeroRonda;
    }

    public Jugador getJugadorActual(){
        return jugadorActual;
    }

    public Jugador getGanadorRonda(){
        return ganadorRonda;
    }

    public Carta getCartaJugada(Jugador jugador){
        if (jugador == jugador1){
            return cartaJ1;
        } else if (jugador == jugador2) {
            return cartaJ2;
        }
        return null;
    }

    public void ingresarJugador(Jugador jugador){
        if (jugador1 == null){
            jugador1 = jugador;
        } else if (jugador2 == null){
            jugador2 = jugador;
        }
    }

    public void iniciarJuego(){
        envidoCantado = false;
        trucoCantado = false;
        jugadorActual = jugador1;
        iniciarMano();
    }

    public void iniciarMano(){
        numeroMano++;
        numeroRonda++;

        mazo.mezclarMazo();
        mazo.repartirCartas(jugador1, jugador2);
    }

    public void cambiarTurno(){
        if (jugadorActual == jugador1){
            jugadorActual = jugador2;
        } else jugadorActual = jugador1;
    }

    public String getEstadoPartida(){
        return jugador1.getNombre() + ": " + jugador1.getPuntos() + " puntos." + "\n" +
                jugador2.getNombre() + ": " + jugador2.getPuntos() + " puntos.";
    }

    public void jugarCarta(int numeroCarta) {

        if (jugadorActual == jugador1) {
            cartaJ1 = jugadorActual.jugarCarta(numeroCarta);
        } else if (jugadorActual == jugador2) {
            cartaJ2 = jugadorActual.jugarCarta(numeroCarta);
        }

        if (cartaJ1 == null || cartaJ2 == null) {
            notificar(Evento.JUGAR_CARTA);
        } else {
            ganadorRonda = determinarGanador();
            limpiarRonda();
            notificar(Evento.FIN_RONDA);
            if (numeroRonda == 3) {
                notificar(Evento.FIN_MANO);
            }
        }
    }

    public Jugador determinarGanador(){

        // Calculo cual carta es la ganadora.
        int diferencia = cartaJ1.getValor().compareTo(cartaJ2.getValor());

        if (diferencia < 0){
            // La carta del jugador 2 gana
            return jugador2;
        } else if (diferencia > 0) {
            // La carta del jugador 1 gana
            return jugador1;
        }
        // Parda
        return null;
    }

    public void limpiarRonda(){
        if (numeroRonda != 3){
            numeroRonda++;
        } else {
            numeroRonda = 0;
        }
        mazo.recibirCarta(cartaJ1);
        mazo.recibirCarta(cartaJ2);
        cartaJ1 = null;
        cartaJ2 = null;
    }

}
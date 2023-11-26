package modelos;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ModeloTruco extends Observable {
    private ArrayList<Observer> observers;

    private Mazo mazo;
    private int numeroMano;
    private int numeroRonda;
    private int puntosRonda;

    private Jugador jugadorActual;
    private Jugador jugadorMano;
    private Jugador ganadorRonda;
    private Jugador ganadorMano;
    private ArrayList<Jugador> ganadoresRondas;

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
        ganadoresRondas = new ArrayList<>();
    }

    public void setObservers(Observer observer){
        observers.add(observer);
    }

    public void notificarJugarCarta(){
        for (Observer observer : observers){
            observer.update(this, Evento.JUGAR_CARTA);
        }
        cambiarTurno();
    }

    public void notificarTruco(){
        for (Observer observer : observers){
            observer.update(this, Evento.TRUCO);
        }

    }

    public void notificarCambioTurno(){
        for (Observer observer : observers){
            observer.update(this, Evento.CAMBIO_TURNO);
        }
    }

    public void notificarFinRonda(){
        for (Observer observer : observers){
            observer.update(this, Evento.FIN_RONDA);
        }
    }

    public void notificarFinMano(){
        for (Observer observer : observers){
            observer.update(this, Evento.FIN_MANO);
        }
    }

    public void notificarMostrarMenu(){
        for (Observer observer : observers){
            observer.update(this, Evento.MOSTRAR_MENU);
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

    public Jugador getGanadorMano(){
        return ganadorMano;
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
        jugadorMano = jugador2;
        iniciarMano();
    }

    public void iniciarMano(){
        numeroMano++;
        numeroRonda++;
        puntosRonda = 1;

        cambiarJugadorMano();
        jugadorActual = jugadorMano;

        mazo.mezclarMazo();
        mazo.repartirCartas(jugador1, jugador2);

    }

    public void cambiarTurno(){
        if (jugadorActual == jugador1){
            jugadorActual = jugador2;
        } else jugadorActual = jugador1;
    }

    public void cambiarJugadorMano(){
        if (jugadorMano == jugador1){
            jugadorMano = jugador2;
        } else jugadorMano = jugador1;
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

        notificarJugarCarta();

        if (cartaJ1 == null || cartaJ2 == null){
            notificarCambioTurno();
        } else {
            ganadorRonda = determinarGanadorRonda();
            limpiarRonda();
            notificarFinRonda();
            if (numeroRonda > 3) {
                ganadorMano = determinarGanadorMano();
                ganadorMano.darPuntos(puntosRonda);

                limpiarMano();
                limpiarRonda();

                notificarFinMano();
                if (jugador1.getPuntos() >= 30 || jugador2.getPuntos() >= 30){
                    // TODO notificarGanadorPartida();
                } else {
                    iniciarMano();
                    notificarMostrarMenu();
                }
            } else {
                notificarMostrarMenu();
            }
        }
    }

    public void cantarTruco(){
        trucoCantado = true;
        notificarTruco();
    }

    public Jugador determinarGanadorRonda(){

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

    public Jugador determinarGanadorMano(){

        int rondasJ1 = 0;
        int rondasJ2 = 0;
        int pardas = 0;

        for (Jugador jugadorGanador : ganadoresRondas){
            if (jugadorGanador == jugador1){
                rondasJ1++;
            } else if (jugadorGanador == jugador2){
                rondasJ2++;
            } else if (jugadorGanador == null){
                pardas++;
            }
        }

        if (rondasJ1 >= 2){
            return jugador1;
        } else if (rondasJ2 >= 2){
            return jugador2;
        } else if (pardas == 1){
            // Si ambos jugadores ganan una ronda pero empaten en una, gana el primero que haya ganado una ronda
            for (Jugador jugadorGanador : ganadoresRondas){
                if (jugadorGanador != null){
                    return jugadorGanador;
                }
            }
        }
        // Si todas las rondas se empatan, gana el jugador que fue mano
        return jugadorMano;
    }

    public void limpiarRonda(){
        numeroRonda++;
        mazo.recibirCarta(cartaJ1);
        mazo.recibirCarta(cartaJ2);
        cartaJ1 = null;
        cartaJ2 = null;
        jugadorActual = ganadorRonda;
        ganadoresRondas.add(ganadorRonda);
    }

    public void limpiarMano(){
        numeroRonda = 1;
        numeroMano++;
        trucoCantado = false;
        envidoCantado = false;
        ganadoresRondas.clear();
        cambiarJugadorMano();
    }

}

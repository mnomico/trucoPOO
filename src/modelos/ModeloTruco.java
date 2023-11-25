package modelos;

import java.util.Observable;

public class ModeloTruco extends Observable {

    private Mazo mazo;
    private int numeroMano;
    private int numeroRonda;

    private Jugador jugadorActual;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador ganadorRonda;

    private boolean envidoCantado;
    private boolean trucoCantado;

    public ModeloTruco(){
        numeroMano = 0;
        numeroRonda = 0;
        mazo = new Mazo();
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

    public void jugarCarta(int numeroCarta){
        jugadorActual.jugarCarta(numeroCarta);
        notificar(Evento.JUGAR_CARTA);
    }

    private void notificar(Evento evento){
        this.setChanged();
        this.notifyObservers(evento);
    }

}

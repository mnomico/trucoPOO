package modelos;

import java.util.ArrayList;
import vista.Observador;


public class ModeloTruco implements Observado {
    private final ArrayList<Observador> observers;

    private final Mazo mazo;
    private int numeroMano;
    private int numeroRonda;
    private int puntosTruco;
    private int puntosEnvido;

    private Jugador jugadorActual;
    private Jugador jugadorMano;
    private Jugador ganadorRonda;
    private Jugador ganadorMano;
    private Jugador ganadorEnvido;
    private final ArrayList<Jugador> ganadoresRondas;
    private Jugador jugadorOriginal;

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

    public void setObservers(Observador observer){
        observers.add(observer);
    }

    /////////////////////////////////////////
    ///////////// NOTIFICADORES /////////////
    /////////////////////////////////////////

    public void notificar(Object arg){
        for (Observador observer : observers){
            observer.update(this, arg);
        }
    }

    public void notificarJugarCarta(){
        notificar(Evento.JUGAR_CARTA);
        cambiarTurno();
    }

    public void notificarApuesta(Apuesta apuesta){
        notificar(apuesta);
        cambiarTurno();
        notificar(Evento.RESPONDER_APUESTA);
    }

    /////////////////////////////////////////
    //////////////// GETTERS ////////////////
    /////////////////////////////////////////

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

    public Jugador getGanadorEnvido(){
        return ganadorEnvido;
    }

    public Carta getCartaJugada(Jugador jugador){
        if (jugador == jugador1){
            return cartaJ1;
        } else if (jugador == jugador2) {
            return cartaJ2;
        }
        return null;
    }

    public String getEstadoPartida(){
        return jugador1.getNombre() + ": " + jugador1.getPuntos() + " - " +
               jugador2.getNombre() + ": " + jugador2.getPuntos();
    }

    public boolean getTrucoCantado(){
        return trucoCantado;
    }

    public boolean getEnvidoCantado(){
        return envidoCantado;
    }

    public String getTantos(){
        return "TANTO " + jugador1.getNombre() + ": " + jugador1.getTanto() + "\n" +
               "TANTO " + jugador2.getNombre() + ": " + jugador2.getTanto();
    }

    /////////////////////////////////////////
    ///////// MÉTODOS PRINCIPALES ///////////
    /////////////////////////////////////////

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
        puntosTruco = 1;

        cambiarJugadorMano();
        jugadorActual = jugadorMano;

        mazo.mezclarMazo();
        mazo.repartirCartas(jugador1, jugador2);
        jugador1.calcularTanto();
        jugador2.calcularTanto();
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

    public void jugarCarta(int numeroCarta) {

        if (jugadorActual == jugador1) {
            cartaJ1 = jugadorActual.jugarCarta(numeroCarta);
        } else if (jugadorActual == jugador2) {
            cartaJ2 = jugadorActual.jugarCarta(numeroCarta);
        }

        notificarJugarCarta();

        if (cartaJ1 == null || cartaJ2 == null){
            notificar(Evento.CAMBIO_TURNO);
        } else {
            ganadorRonda = determinarGanadorRonda();
            limpiarRonda();
            notificar(Evento.FIN_RONDA);
            if (numeroRonda > 3) {
                ganadorMano = determinarGanadorMano();
                ganadorMano.darPuntos(puntosTruco);

                limpiarMano();

                notificar(Evento.FIN_MANO);
                if (finPartida()){
                    notificar(Evento.FIN_PARTIDA);
                } else {
                    iniciarMano();
                    notificar(Evento.MOSTRAR_MENU);
                }
            } else {
                notificar(Evento.MOSTRAR_MENU);
            }
        }
    }

    // Chequea si alguno de los jugadores tiene 30 puntos o más y retorna true si eso sucede
    public boolean finPartida(){
        if (jugador1.getPuntos() >= 30){
            jugadorActual = jugador1;
            return true;
        } else if (jugador2.getPuntos() >= 30){
            jugadorActual = jugador2;
            return true;
        }
        return false;
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

    public void irseAlMazo(){
        notificar(Evento.IRSE_AL_MAZO);
        cambiarTurno();
        ganadorMano = jugadorActual;
        ganadorMano.darPuntos(puntosTruco);
        notificar(Evento.FIN_MANO);
        limpiarMano();
        iniciarMano();
        notificar(Evento.MOSTRAR_MENU);
    }

    public void guardarCartasJugadasAlMazo(){
        if (cartaJ1 != null){
            mazo.recibirCarta(cartaJ1);
        }
        if (cartaJ2 != null){
            mazo.recibirCarta(cartaJ2);
        }
        cartaJ1 = null;
        cartaJ2 = null;
    }

    public void guardarCartasNoJugadasAlMazo(){
        ArrayList<Carta> cartasJ1 = jugador1.devolverCartas();
        ArrayList<Carta> cartasJ2 = jugador2.devolverCartas();

        if (!cartasJ1.isEmpty()){
            for (Carta carta : cartasJ1){
                mazo.recibirCarta(carta);
            }
        }
        jugador1.removerCartas();

        if (!cartasJ2.isEmpty()){
            for (Carta carta : cartasJ2){
                mazo.recibirCarta(carta);
            }
        }
        jugador2.removerCartas();
    }

    public void limpiarRonda(){
        numeroRonda++;
        guardarCartasJugadasAlMazo();
        // Si se produce una parda
        if (ganadorRonda == null){
            jugadorActual = jugadorMano;
        } else {
            jugadorActual = ganadorRonda;
        }
        ganadoresRondas.add(ganadorRonda);
    }

    public void limpiarMano(){
        numeroRonda = 0;
        trucoCantado = false;
        envidoCantado = false;
        guardarCartasJugadasAlMazo();
        guardarCartasNoJugadasAlMazo();
        ganadoresRondas.clear();
    }

    /////////////////////////////////////////
    //////// MÉTODOS SOBRE APUESTAS /////////
    /////////////////////////////////////////

    public void cantarTruco(){
        trucoCantado = true;
        notificar(Apuesta.TRUCO);
        jugadorOriginal = jugadorActual;
        cambiarTurno();
        notificar(Evento.RESPONDER_APUESTA);
    }

    public void cantarEnvido(){
        envidoCantado = true;
        notificar(Apuesta.ENVIDO);
        jugadorOriginal = jugadorActual;
        cambiarTurno();
        notificar(Evento.RESPONDER_APUESTA);
    }

    public Jugador calcularEnvido(){
        int tantoJ1 = jugador1.getTanto();
        int tantoJ2 = jugador2.getTanto();

        if (tantoJ1 > tantoJ2){
            return jugador1;
        } else if (tantoJ2 > tantoJ1){
            return jugador2;
        } else return jugadorMano;
    }

    public void quiero(Apuesta apuesta){
        switch (apuesta){
            case TRUCO -> puntosTruco = 2;
            case RETRUCO -> puntosTruco = 3;
            case VALECUATRO -> puntosTruco = 4;
            case ENVIDO -> puntosEnvido = 2;
            case REAL_ENVIDO -> puntosEnvido = 3;
            // TODO case FALTA_ENVIDO
            case ENVIDO_ENVIDO -> puntosEnvido = 4;
            case ENVIDO_REAL_ENVIDO -> puntosEnvido = 5;
            // TODO case ENVIDO_FALTA_ENVIDO
            // TODO case REAL_ENVIDO_FALTA_ENVIDO
            case ENVIDO_ENVIDO_REAL_ENVIDO -> puntosEnvido = 7;
            // TODO case ENVIDO_REAL_ENVIDO_FALTA_ENVIDO
            // TODO case ENVIDO_ENVIDO_REAL_ENVIDO_FALTA_ENVIDO
        }

        // Retorna el turno al jugador que apostó inicialmente
        notificar(Evento.DIJO_QUIERO);

        jugadorActual = jugadorOriginal;

        switch (apuesta){
            case TRUCO, RETRUCO, VALECUATRO -> notificar(Evento.MOSTRAR_MENU);
            default -> {
                ganadorEnvido = calcularEnvido();
                notificar(Evento.RESULTADO_ENVIDO);
                ganadorEnvido.darPuntos(puntosEnvido);
                if (finPartida()){
                    notificar(Evento.FIN_PARTIDA);
                } else {
                    notificar(Evento.MOSTRAR_MENU);
                }
            }
        }
    }

    public void noQuiero(Apuesta apuesta){
        switch (apuesta){
            case TRUCO -> puntosTruco = 1;
            case RETRUCO -> puntosTruco = 2;
            case VALECUATRO -> puntosTruco = 3;
            case ENVIDO, REAL_ENVIDO, FALTA_ENVIDO -> puntosEnvido = 1;
            case ENVIDO_ENVIDO, ENVIDO_REAL_ENVIDO, ENVIDO_FALTA_ENVIDO -> puntosEnvido = 2;
            case REAL_ENVIDO_FALTA_ENVIDO -> puntosEnvido = 3;
            case ENVIDO_ENVIDO_REAL_ENVIDO -> puntosEnvido = 4;
            case ENVIDO_REAL_ENVIDO_FALTA_ENVIDO -> puntosEnvido = 5;
            case ENVIDO_ENVIDO_REAL_ENVIDO_FALTA_ENVIDO -> puntosEnvido = 7;
        }

        // Retorna el turno al jugador que apostó inicialmente
        switch (apuesta){
            case TRUCO, RETRUCO, VALECUATRO -> {
                notificar(Evento.DIJO_NO_QUIERO);
                cambiarTurno();
                ganadorMano = jugadorActual;
                ganadorMano.darPuntos(puntosTruco);
                notificar(Evento.FIN_MANO);
                if (finPartida()){
                    notificar(Evento.FIN_PARTIDA);
                } else {
                    limpiarMano();
                    iniciarMano();
                    notificar(Evento.MOSTRAR_MENU);
                }
            }
            default -> {
                jugadorActual = jugadorOriginal;
                notificar(Evento.MOSTRAR_MENU);
            }
        }
    }

    public void redoblarEnvido(Apuesta apuesta){
        notificarApuesta(apuesta);
    }

    public void redoblarApuesta(Apuesta apuesta){
        switch (apuesta){
            case TRUCO -> notificarApuesta(Apuesta.RETRUCO);
            case RETRUCO -> notificarApuesta(Apuesta.VALECUATRO);
            case ENVIDO -> notificarApuesta(apuesta);
            // Envido se puede responder con ENVIDO, REAL_ENVIDO, FALTA_ENVIDO
            case REAL_ENVIDO -> notificarApuesta(Apuesta.REAL_ENVIDO_FALTA_ENVIDO);
            case ENVIDO_ENVIDO -> notificarApuesta(Apuesta.ENVIDO_ENVIDO_REAL_ENVIDO);
            case ENVIDO_ENVIDO_REAL_ENVIDO -> notificarApuesta(Apuesta.ENVIDO_ENVIDO_REAL_ENVIDO_FALTA_ENVIDO);
        }
    }

}

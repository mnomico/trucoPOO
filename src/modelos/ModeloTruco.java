package modelos;

import java.util.ArrayList;
import vistas.Observador;


public class ModeloTruco implements Observado {
    private final ArrayList<Observador> observers;

    private final Mazo mazo;
    private int numeroMano;
    private int numeroRonda;
    private int puntosTruco;
    private int puntosEnvido;

    private int jugadorActual;
    private int jugadorMano;
    private int ganadorRonda;
    private int ganadorMano;
    private int ganadorEnvido;
    private int jugadorOriginal;
    private int jugadorQuieroTruco;
    private final ArrayList<Integer> ganadoresRondas;

    private Jugador jugador1;
    private Carta cartaJ1;

    private Jugador jugador2;
    private Carta cartaJ2;

    private Carta cartaGanadora;

    private boolean envidoCantado;
    private boolean trucoCantado;
    private Apuesta trucoActual;

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

    public int getJugadorActual(){
        return jugadorActual;
    }

    public String getNombreJugadorActual(){
        if (jugadorActual == 1){
            return jugador1.getNombre();
        } else if (jugadorActual == 2){
            return jugador2.getNombre();
        }
        return "";
    }

    public ArrayList<String> getCartas(int jugador){
        if (jugador == 1){
            return jugador1.mostrarCartas();
        } else if (jugador == 2){
            return jugador2.mostrarCartas();
        }
        return new ArrayList<>();
    }

    public int getGanadorRonda(){
        return ganadorRonda;
    }

    public String getNombreGanadorRonda(){
        if (ganadorRonda == 1){
            return jugador1.getNombre();
        } else if (ganadorRonda == 2){
            return jugador2.getNombre();
        }
        return "";
    }

    public int getGanadorMano(){
        return ganadorMano;
    }

    public String getNombreGanadorMano(){
        if (ganadorMano == 1){
            return jugador1.getNombre();
        } else if (ganadorMano == 2){
            return jugador2.getNombre();
        }
        return "";
    }

    public int getJugadorQuieroTruco(){
        return jugadorQuieroTruco;
    }

    public int getGanadorEnvido(){
        return ganadorEnvido;
    }

    public String getNombreGanadorEnvido(){
        if (ganadorEnvido == 1){
            return jugador1.getNombre();
        } else if (ganadorEnvido == 2){
            return jugador2.getNombre();
        }
        return "";
    }

    public String getCartaJugada(int jugador){
        if (jugador == 1){
            return cartaJ1.toString();
        } else if (jugador == 2) {
            return cartaJ2.toString();
        }
        return "";
    }

    public String getCartaGanadora(){
        if (cartaGanadora == null){
            return "";
        }
        return cartaGanadora.toString();
    }

    public String getEstadoPartida(){
        return "\t" + jugador1.getNombre() + ": " + jugador1.getPuntos() + " - " +
               jugador2.getNombre() + ": " + jugador2.getPuntos();
    }

    public boolean getTrucoCantado(){
        return trucoCantado;
    }

    public Apuesta getTrucoActual(){
        return trucoActual;
    }

    public boolean getEnvidoCantado(){
        return envidoCantado;
    }

    public String getTantos(){
        return "\tTANTO " + jugador1.getNombre() + ": " + jugador1.getTanto() + "\n" +
               "\tTANTO " + jugador2.getNombre() + ": " + jugador2.getTanto();
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
        jugadorActual = 1;
        jugadorMano = 2;
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
        if (jugadorActual == 1){
            jugadorActual = 2;
        } else jugadorActual = 1;
    }

    public void cambiarJugadorMano(){
        if (jugadorMano == 1){
            jugadorMano = 2;
        } else jugadorMano = 1;
    }

    public void jugarCarta(int numeroCarta) {

        if (jugadorActual == 1) {
            cartaJ1 = jugador1.jugarCarta(numeroCarta);
        } else if (jugadorActual == 2) {
            cartaJ2 = jugador2.jugarCarta(numeroCarta);
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
                darPuntos(ganadorMano, puntosTruco);

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
            jugadorActual = 1;
            return true;
        } else if (jugador2.getPuntos() >= 30){
            jugadorActual = 2;
            return true;
        }
        return false;
    }

    public int determinarGanadorRonda(){

        // Calculo cual carta es la ganadora.
        int diferencia = cartaJ1.getValor().compareTo(cartaJ2.getValor());

        if (diferencia < 0){
            // La carta del jugador 2 gana
            cartaGanadora = cartaJ2;
            return 2;
        } else if (diferencia > 0) {
            // La carta del jugador 1 gana
            cartaGanadora = cartaJ1;
            return 1;
        }
        // Parda
        cartaGanadora = null;
        return 0;
    }

    public int determinarGanadorMano(){

        int rondasJ1 = 0;
        int rondasJ2 = 0;
        int pardas = 0;

        for (int jugadorGanador : ganadoresRondas){
            if (jugadorGanador == 1){
                rondasJ1++;
            } else if (jugadorGanador == 2){
                rondasJ2++;
            } else if (jugadorGanador == 0){
                pardas++;
            }
        }

        if (rondasJ1 >= 2){
            return 1;
        } else if (rondasJ2 >= 2){
            return 2;
        } else if (pardas == 1){
            // Si ambos jugadores ganan una ronda pero empaten en una, gana el primero que haya ganado una ronda
            for (int jugadorGanador : ganadoresRondas){
                if (jugadorGanador != 0){
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
        darPuntos(ganadorMano, puntosTruco);
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
        if (ganadorRonda == 0){
            jugadorActual = jugadorMano;
        } else {
            jugadorActual = ganadorRonda;
        }
        ganadoresRondas.add(ganadorRonda);
    }

    public void limpiarMano(){
        numeroRonda = 0;
        trucoCantado = false;
        trucoActual = null;
        envidoCantado = false;
        guardarCartasJugadasAlMazo();
        guardarCartasNoJugadasAlMazo();
        ganadoresRondas.clear();
        jugadorQuieroTruco = 0;
    }

    /////////////////////////////////////////
    //////// MÉTODOS SOBRE APUESTAS /////////
    /////////////////////////////////////////

    public void cantarTruco(){
        if (!trucoCantado){
            trucoCantado = true;
            trucoActual = Apuesta.TRUCO;
        } else {
            switch (trucoActual){
                case TRUCO -> trucoActual = Apuesta.RETRUCO;
                case RETRUCO -> trucoActual = Apuesta.VALECUATRO;
            }
        }
        notificar(trucoActual);
        jugadorOriginal = jugadorActual;
        cambiarTurno();
        notificar(Evento.RESPONDER_APUESTA);
    }

    // Para el envido inicial
    public void cantarEnvido(Apuesta apuesta){
        envidoCantado = true;
        notificar(apuesta);
        jugadorOriginal = jugadorActual;
        cambiarTurno();
        notificar(Evento.RESPONDER_APUESTA);
    }

    public void cantarEnvidoTruco(Apuesta apuesta){
        envidoCantado = true;
        notificar(apuesta);
        cambiarTurno();
        notificar(Evento.RESPONDER_APUESTA);
    }

    public int calcularEnvido(){
        int tantoJ1 = jugador1.getTanto();
        int tantoJ2 = jugador2.getTanto();

        if (tantoJ1 > tantoJ2){
            return 1;
        } else if (tantoJ2 > tantoJ1){
            return 2;
        } else return jugadorMano;
    }

    public void quiero(Apuesta apuesta){
        switch (apuesta){
            case TRUCO -> puntosTruco = 2;
            case RETRUCO -> puntosTruco = 3;
            case VALECUATRO -> puntosTruco = 4;
            case ENVIDO -> puntosEnvido = 2;
            case REAL_ENVIDO -> puntosEnvido = 3;
            case ENVIDO_ENVIDO -> puntosEnvido = 4;
            case ENVIDO_REAL_ENVIDO -> puntosEnvido = 5;
            case ENVIDO_ENVIDO_REAL_ENVIDO -> puntosEnvido = 7;
        }

        // Retorna el turno al jugador que apostó inicialmente
        notificar(Evento.DIJO_QUIERO);

        jugadorQuieroTruco = jugadorActual;
        jugadorActual = jugadorOriginal;

        switch (apuesta){
            case TRUCO, RETRUCO -> notificar(Evento.MOSTRAR_MENU);
            case VALECUATRO -> {
                jugadorQuieroTruco = 0;
                notificar(Evento.MOSTRAR_MENU);
            }

            // Casos de falta envido
            case FALTA_ENVIDO, ENVIDO_FALTA_ENVIDO, REAL_ENVIDO_FALTA_ENVIDO,
                    ENVIDO_REAL_ENVIDO_FALTA_ENVIDO,
                    ENVIDO_ENVIDO_REAL_ENVIDO_FALTA_ENVIDO ->
            {
                ganadorEnvido = calcularEnvido();
                notificar(Evento.RESULTADO_ENVIDO);
                int puntosFalta = 0;
                if (ganadorEnvido != 1){
                    puntosFalta = 30 - jugador1.getPuntos();
                } else if (ganadorEnvido != 2){
                    puntosFalta = 30 - jugador2.getPuntos();
                }
                darPuntos(ganadorEnvido, puntosFalta);
                if (finPartida()){
                    notificar(Evento.FIN_PARTIDA);
                } else {
                    notificar(Evento.MOSTRAR_MENU);
                }
            }

            // Para el resto de los casos de envido
            default -> {
                ganadorEnvido = calcularEnvido();
                notificar(Evento.RESULTADO_ENVIDO);
                darPuntos(ganadorEnvido, puntosEnvido);
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
                darPuntos(ganadorMano, puntosTruco);
                notificar(Evento.FIN_MANO);
                if (finPartida()){
                    notificar(Evento.FIN_PARTIDA);
                } else {
                    limpiarMano();
                    iniciarMano();
                    notificar(Evento.MOSTRAR_MENU);
                }
            }

            // Para los casos de envido
            default -> {
                notificar(Evento.DIJO_NO_QUIERO);
                cambiarTurno();
                darPuntos(jugadorActual, puntosEnvido);
                jugadorActual = jugadorOriginal;
                notificar(Evento.MOSTRAR_MENU);
            }
        }
    }

    // Para el primer envido
    public void redoblarEnvido(Apuesta apuesta){
        notificarApuesta(apuesta);
    }

    public void redoblarApuesta(Apuesta apuesta){
        switch (apuesta){
            case TRUCO -> {
                trucoActual = Apuesta.RETRUCO;
                notificarApuesta(Apuesta.RETRUCO);
            }
            case RETRUCO -> {
                trucoActual = Apuesta.VALECUATRO;
                notificarApuesta(Apuesta.VALECUATRO);
            }
            case ENVIDO -> notificarApuesta(apuesta);
            // Envido se puede responder con ENVIDO, REAL_ENVIDO, FALTA_ENVIDO
            case REAL_ENVIDO -> notificarApuesta(Apuesta.REAL_ENVIDO_FALTA_ENVIDO);
            case ENVIDO_ENVIDO -> notificarApuesta(Apuesta.ENVIDO_ENVIDO_REAL_ENVIDO);
            case ENVIDO_ENVIDO_REAL_ENVIDO -> notificarApuesta(Apuesta.ENVIDO_ENVIDO_REAL_ENVIDO_FALTA_ENVIDO);
        }
    }

    public void darPuntos(int jugador, int puntos){
        if (jugador == 1){
            jugador1.darPuntos(puntos);
        } else if (jugador == 2){
            jugador2.darPuntos(puntos);
        }
    }

}

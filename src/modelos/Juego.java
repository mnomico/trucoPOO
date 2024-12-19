package modelos;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;
import services.Serializador;
public class Juego extends ObservableRemoto implements IJuego, Serializable {
    private final ArrayList<IControladorRemoto> observers;

    // Mazo, variables para mano y ronda
    private final Mazo mazo;
    private int numeroMano;
    private int numeroRonda;
    private int puntosTruco;
    private int puntosEnvido;

    // Variables para estado de los jugadores
    private int jugadorActual; // Representa el jugador que tiene que jugar actualmente
    private int jugadorMano;
    private int ganadorRonda; // Representa el ganador de la ronda anterior, el cual empieza la ronda siguiente
    private int ganadorMano;
    private int ganadorEnvido;
    private int jugadorOriginal;// Se utiliza para cuando se hace el canto, para que luego de la disputa se retorne
                                // el turno al jugador que cantó primero
    private int jugadorQuieroTruco;
    private final int[] ganadoresRondas;

    // Jugador 1
    private Jugador jugador1;
    private Carta cartaJ1;

    // Jugador 2
    private Jugador jugador2;
    private Carta cartaJ2;

    // Carta jugada con mayor valor en una ronda
    private Carta cartaGanadora;

    // Variables de estado de cantos
    private boolean envidoCantado;
    private boolean trucoCantado;
    private Apuesta trucoActual;

    public Juego() throws RemoteException {
        observers = new ArrayList<>();
        numeroMano = 0;
        numeroRonda = 0;
        mazo = new Mazo();
        ganadoresRondas = new int[3];
    }

    @Override
    public void setObservers(IControladorRemoto observer) throws RemoteException{
        observers.add(observer);
    }

    /////////////////////////////////////////
    ///////////// NOTIFICADORES /////////////
    /////////////////////////////////////////

    @Override
    public void notificarObservadores(Object arg) throws RemoteException {
        for (IControladorRemoto observer : observers){
            observer.actualizar(this, arg);
        }
    }

    @Override
    public void notificarObservadoresJugarCarta() throws RemoteException {
        notificarObservadores(Evento.JUGAR_CARTA);
        cambiarTurno();
    }

    @Override
    public void notificarObservadoresApuesta(Apuesta apuesta) throws RemoteException {
        notificarObservadores(apuesta);
        cambiarTurno();
        notificarObservadores(Evento.RESPONDER_APUESTA);
    }

    /////////////////////////////////////////
    //////////////// GETTERS ////////////////
    /////////////////////////////////////////

    @Override
    public int getNumeroMano() throws RemoteException {
        return numeroMano;
    }

    @Override
    public int getNumeroRonda() throws RemoteException{
        return numeroRonda;
    }

    @Override
    public int getJugadorActual() throws RemoteException{
        return jugadorActual;
    }

    @Override
    public String getNombreJugadorActual() throws RemoteException{
        if (jugadorActual == 1){
            return jugador1.getNombre();
        } else if (jugadorActual == 2){
            return jugador2.getNombre();
        }
        return "";
    }

    @Override
    public ArrayList<String> getCartas(int jugador) throws RemoteException{
        if (jugador == 1){
            return jugador1.mostrarCartas();
        } else if (jugador == 2){
            return jugador2.mostrarCartas();
        }
        return new ArrayList<>();
    }

    @Override
    public int getGanadorRonda() throws RemoteException{
        return ganadorRonda;
    }

    @Override
    public String getNombreGanadorRonda() throws RemoteException{
        if (ganadorRonda == 1){
            return jugador1.getNombre();
        } else if (ganadorRonda == 2){
            return jugador2.getNombre();
        }
        return "";
    }

    @Override
    public int getGanadorMano() throws RemoteException{
        return ganadorMano;
    }

    @Override
    public String getNombreGanadorMano() throws RemoteException{
        if (ganadorMano == 1){
            return jugador1.getNombre();
        } else if (ganadorMano == 2){
            return jugador2.getNombre();
        }
        return "";
    }

    @Override
    public int getJugadorQuieroTruco() throws RemoteException{
        return jugadorQuieroTruco;
    }

    @Override
    public int getGanadorEnvido() throws RemoteException{
        return ganadorEnvido;
    }

    @Override
    public String getNombreGanadorEnvido() throws RemoteException{
        if (ganadorEnvido == 1){
            return jugador1.getNombre();
        } else if (ganadorEnvido == 2){
            return jugador2.getNombre();
        }
        return "";
    }

    @Override
    public String getCartaJugada(int jugador) throws RemoteException{
        if (jugador == 1){
            return cartaJ1.toString();
        } else if (jugador == 2) {
            return cartaJ2.toString();
        }
        return "";
    }

    @Override
    public String getCartaGanadora() throws RemoteException{
        if (cartaGanadora == null){
            return "";
        }
        return cartaGanadora.toString();
    }

    @Override
    public String getEstadoPartida() throws RemoteException{
        return "\t" + jugador1.getNombre() + ": " + jugador1.getPuntos() + " - " +
                jugador2.getNombre() + ": " + jugador2.getPuntos();
    }

    @Override
    public boolean getTrucoCantado() throws RemoteException{
        return trucoCantado;
    }

    @Override
    public Apuesta getTrucoActual() throws RemoteException{
        return trucoActual;
    }

    @Override
    public boolean getEnvidoCantado() throws RemoteException{
        return envidoCantado;
    }

    @Override
    public String getTantos() throws RemoteException{
        return "\tTANTO " + jugador1.getNombre() + ": " + jugador1.getTanto() + "\n" +
                "\tTANTO " + jugador2.getNombre() + ": " + jugador2.getTanto();
    }

    /////////////////////////////////////////
    ///////// MÉTODOS PRINCIPALES ///////////
    /////////////////////////////////////////

    @Override
    public void ingresarJugador(String jugador) throws RemoteException{
        // Si no hay jugadores se registra el primer jugador
        // Una vez ingresado el jugador, cuando se registre el segundo se inicia el juego
        if (jugador1 == null){
            jugador1 = new Jugador(jugador);
        } else if (jugador2 == null){
            jugador2 = new Jugador(jugador);
            iniciarJuego();
        }
    }

    @Override
    public int obtenerJugador() throws RemoteException{
        // Si no hay jugadores, retorna el id 1, si ya ingresó un jugador
        // retorna el id 2, y si ambos jugadores ya ingresaron se retorna 0
        if (jugador1 == null) {
            return 1;
        } else if (jugador2 == null) {
            return 2;
        }
        return 0;
    }

    @Override
    public void iniciarJuego() throws RemoteException{
        envidoCantado = false;
        trucoCantado = false;
        jugadorActual = 1;
        jugadorMano = 2;
        iniciarMano();
    }

    @Override
    public void iniciarMano() throws RemoteException{
        numeroMano++;
        numeroRonda++;
        puntosTruco = 1;

        cambiarJugadorMano();
        jugadorActual = jugadorMano;

        mazo.mezclarMazo();
        mazo.repartirCartas(jugador1, jugador2);
        jugador1.calcularTanto();
        jugador2.calcularTanto();

        notificarObservadores(Evento.MOSTRAR_MENU);
    }

    @Override
    public void cambiarTurno() throws RemoteException{
        if (jugadorActual == 1){
            jugadorActual = 2;
        } else jugadorActual = 1;
    }

    @Override
    public void cambiarJugadorMano() throws RemoteException{
        if (jugadorMano == 1){
            jugadorMano = 2;
        } else jugadorMano = 1;
    }

    @Override
    public void jugarCarta(int numeroCarta) throws IOException {

        if (jugadorActual == 1) {
            cartaJ1 = jugador1.jugarCarta(numeroCarta);
        } else if (jugadorActual == 2) {
            cartaJ2 = jugador2.jugarCarta(numeroCarta);
        }

        notificarObservadoresJugarCarta();

        // Si todavía falta que un jugador juegue su carta, se cambia el turno
        if (cartaJ1 == null || cartaJ2 == null){
            notificarObservadores(Evento.CAMBIO_TURNO);
        } else {
            // Si ambos jugaron sus cartas, se determina el ganador de la ronda,
            ganadorRonda = determinarGanadorRonda();

            limpiarRonda();
            notificarObservadores(Evento.FIN_RONDA);

            // Si ya se jugaron todas las rondas, se procede a determinar el ganador de la mano
            // y se distribuyen los puntos, verificando también si ya se tienen los puntos suficientes para ganar
            if (numeroRonda > 3) {
                ganadorMano = determinarGanadorMano();
                darPuntos(ganadorMano, puntosTruco);

                limpiarMano();

                notificarObservadores(Evento.FIN_MANO);
                if (finPartida()){
                    guardarPuntaje(getNombreJugadorActual());
                    notificarObservadores(Evento.FIN_PARTIDA);
                } else {
                    iniciarMano();
                }
            } else {
                notificarObservadores(Evento.MOSTRAR_MENU);
            }
        }
    }

    @Override
    public boolean finPartida() throws RemoteException{
        // Verifica si alguno de los jugadores tiene 30 puntos o más
        if (jugador1.getPuntos() >= 30){
            jugadorActual = 1;
            return true;
        } else if (jugador2.getPuntos() >= 30){
            jugadorActual = 2;
            return true;
        }
        return false;
    }

    @Override
    public int determinarGanadorRonda() throws RemoteException{
        // Calculo cual carta es la ganadora a partir de los valores que tienen las cartas
        int diferencia = cartaJ1.getValor().compareTo(cartaJ2.getValor());

        if (diferencia < 0){
            cartaGanadora = cartaJ2;
            return 2;
        } else if (diferencia > 0) {
            cartaGanadora = cartaJ1;
            return 1;
        }
        // Parda
        cartaGanadora = null;
        return 0;
    }

    @Override
    public int determinarGanadorMano() throws RemoteException{

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

    // TODO ver si esto funciona bien
    @Override
    public void irseAlMazo() throws IOException{
        notificarObservadores(Evento.IRSE_AL_MAZO);
        cambiarTurno();
        ganadorMano = jugadorActual;

        /*
        darPuntos(ganadorMano, puntosTruco);
        notificarObservadores(Evento.FIN_MANO);
        limpiarMano();
        iniciarMano();
        */

        darPuntos(ganadorMano, puntosTruco);
        limpiarMano();
        notificarObservadores(Evento.FIN_MANO);
        if (finPartida()){
            guardarPuntaje(getNombreJugadorActual());
            notificarObservadores(Evento.FIN_PARTIDA);
        } else {
            iniciarMano();
        }
    }

    @Override
    public void guardarCartasJugadasAlMazo() throws RemoteException {
        if (cartaJ1 != null){
            mazo.recibirCarta(cartaJ1);
        }
        if (cartaJ2 != null){
            mazo.recibirCarta(cartaJ2);
        }
        cartaJ1 = null;
        cartaJ2 = null;
    }

    @Override
    public void guardarCartasNoJugadasAlMazo() throws RemoteException{
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

    @Override
    public void limpiarRonda() throws RemoteException{
        guardarCartasJugadasAlMazo();
        // Si se produce una parda, se da el turno al jugador mano, sino al que ganó la ronda
        if (ganadorRonda == 0){
            jugadorActual = jugadorMano;
        } else {
            jugadorActual = ganadorRonda;
        }
        //ganadoresRondas.add(ganadorRonda);
        ganadoresRondas[numeroRonda-1] = ganadorRonda;
        numeroRonda++;
    }

    @Override
    public void limpiarMano() throws RemoteException{
        numeroRonda = 0;
        trucoCantado = false;
        trucoActual = null;
        envidoCantado = false;
        guardarCartasJugadasAlMazo();
        guardarCartasNoJugadasAlMazo();
        //ganadoresRondas.clear();
        Arrays.fill(ganadoresRondas, 0);
        jugadorQuieroTruco = 0;
    }

    /////////////////////////////////////////
    //////// MÉTODOS SOBRE APUESTAS /////////
    /////////////////////////////////////////

    @Override
    public void cantarTruco() throws RemoteException{
        if (!trucoCantado){
            trucoCantado = true;
            trucoActual = Apuesta.TRUCO;
        } else {
            // TODO creo que este else no sirve para nada, redoblarApuesta lo resuelve
            switch (trucoActual){
                case TRUCO -> trucoActual = Apuesta.RETRUCO;
                case RETRUCO -> trucoActual = Apuesta.VALECUATRO;
            }
        }
        notificarObservadores(trucoActual);
        jugadorOriginal = jugadorActual;
        cambiarTurno();
        notificarObservadores(Evento.RESPONDER_APUESTA);
    }

    // Para el envido inicial
    @Override
    public void cantarEnvido(Apuesta apuesta) throws RemoteException{
        envidoCantado = true;
        notificarObservadores(apuesta);
        jugadorOriginal = jugadorActual;
        cambiarTurno();
        notificarObservadores(Evento.RESPONDER_APUESTA);
    }

    // Para cantar envido cuando primero se canta truco
    @Override
    public void cantarEnvidoTruco(Apuesta apuesta) throws RemoteException{
        envidoCantado = true;
        notificarObservadores(apuesta);
        cambiarTurno();
        notificarObservadores(Evento.RESPONDER_APUESTA);
    }

    @Override
    public int calcularEnvido() throws RemoteException{
        int tantoJ1 = jugador1.getTanto();
        int tantoJ2 = jugador2.getTanto();

        if (tantoJ1 > tantoJ2){
            return 1;
        } else if (tantoJ2 > tantoJ1){
            return 2;
        } else return jugadorMano;
    }

    @Override
    public void quiero(Apuesta apuesta) throws IOException {
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
        notificarObservadores(Evento.DIJO_QUIERO);

        jugadorQuieroTruco = jugadorActual;
        jugadorActual = jugadorOriginal;

        switch (apuesta){
            case TRUCO, RETRUCO -> notificarObservadores(Evento.MOSTRAR_MENU);
            case VALECUATRO -> {
                jugadorQuieroTruco = 0;
                notificarObservadores(Evento.MOSTRAR_MENU);
            }

            // Casos de falta envido
            case FALTA_ENVIDO, ENVIDO_FALTA_ENVIDO, REAL_ENVIDO_FALTA_ENVIDO,
                    ENVIDO_REAL_ENVIDO_FALTA_ENVIDO,
                    ENVIDO_ENVIDO_REAL_ENVIDO_FALTA_ENVIDO ->
            {
                ganadorEnvido = calcularEnvido();
                notificarObservadores(Evento.RESULTADO_ENVIDO);
                int puntosFalta = 0;
                if (ganadorEnvido != 1){
                    puntosFalta = 30 - jugador1.getPuntos();
                } else if (ganadorEnvido != 2){
                    puntosFalta = 30 - jugador2.getPuntos();
                }
                darPuntos(ganadorEnvido, puntosFalta);
                if (finPartida()){
                    guardarPuntaje(getNombreJugadorActual());
                    notificarObservadores(Evento.FIN_PARTIDA);
                } else {
                    notificarObservadores(Evento.MOSTRAR_MENU);
                }
            }

            // Para el resto de los casos de envido
            default -> {
                ganadorEnvido = calcularEnvido();
                notificarObservadores(Evento.RESULTADO_ENVIDO);
                darPuntos(ganadorEnvido, puntosEnvido);
                if (finPartida()){
                    guardarPuntaje(getNombreJugadorActual());
                    notificarObservadores(Evento.FIN_PARTIDA);
                } else {
                    notificarObservadores(Evento.MOSTRAR_MENU);
                }
            }
        }
    }

    @Override
    public void noQuiero(Apuesta apuesta) throws IOException {
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
                notificarObservadores(Evento.DIJO_NO_QUIERO);
                cambiarTurno();
                ganadorMano = jugadorActual;
                darPuntos(ganadorMano, puntosTruco);
                notificarObservadores(Evento.FIN_MANO);
                if (finPartida()){
                    guardarPuntaje(getNombreJugadorActual());
                    notificarObservadores(Evento.FIN_PARTIDA);
                } else {
                    limpiarMano();
                    iniciarMano();
                    notificarObservadores(Evento.MOSTRAR_MENU);
                }
            }

            // Para los casos de envido
            default -> {
                notificarObservadores(Evento.DIJO_NO_QUIERO);
                cambiarTurno();
                darPuntos(jugadorActual, puntosEnvido);
                jugadorActual = jugadorOriginal;
                notificarObservadores(Evento.MOSTRAR_MENU);
            }
        }
    }

    // Para el primer envido
    @Override
    public void redoblarEnvido(Apuesta apuesta) throws RemoteException{
        notificarObservadoresApuesta(apuesta);
    }

    @Override
    public void redoblarApuesta(Apuesta apuesta) throws RemoteException{
        switch (apuesta){
            case TRUCO -> {
                trucoActual = Apuesta.RETRUCO;
                notificarObservadoresApuesta(Apuesta.RETRUCO);
            }
            case RETRUCO -> {
                trucoActual = Apuesta.VALECUATRO;
                notificarObservadoresApuesta(Apuesta.VALECUATRO);
            }
            case ENVIDO -> notificarObservadoresApuesta(apuesta);
            // Envido se puede responder con ENVIDO, REAL_ENVIDO, FALTA_ENVIDO
            case REAL_ENVIDO -> notificarObservadoresApuesta(Apuesta.REAL_ENVIDO_FALTA_ENVIDO);
            case ENVIDO_ENVIDO -> notificarObservadoresApuesta(Apuesta.ENVIDO_ENVIDO_REAL_ENVIDO);
            case ENVIDO_ENVIDO_REAL_ENVIDO -> notificarObservadoresApuesta(Apuesta.ENVIDO_ENVIDO_REAL_ENVIDO_FALTA_ENVIDO);
        }
    }

    @Override
    public void darPuntos(int jugador, int puntos) throws RemoteException{
        if (jugador == 1){
            jugador1.darPuntos(puntos);
        } else if (jugador == 2){
            jugador2.darPuntos(puntos);
        }
    }

    public void guardarPuntaje(String jugador) throws IOException {
        File archivoPuntajes = new File("scoreboard.dat");

        if (!archivoPuntajes.exists()) {
            archivoPuntajes.createNewFile();
        }

        Serializador scoreboard = new Serializador("scoreboard.dat");
        Object[] puntajes = scoreboard.readObjects();

        ArrayList<Object[]> listaPuntajes = new ArrayList<>();
        boolean existe = false;

        // Actualizar puntaje o añadir un nuevo jugador
        if (puntajes != null) {
            for (Object o : puntajes) {
                Object[] puntaje = (Object[]) o;

                // Verificar si el jugador ya existe
                if (puntaje[0].equals(jugador)) {
                    puntaje[1] = (int) puntaje[1] + 1; // Incrementar puntaje
                    existe = true;
                }
                listaPuntajes.add(puntaje);
            }
        }

        // Si el jugador no existía, se añade uno nuevo
        if (!existe) {
            Object[] nuevoPuntaje = {jugador, 1};
            listaPuntajes.add(nuevoPuntaje);
        }

        // Guardar todos los puntajes actualizados en el archivo
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoPuntajes))) {
            for (Object[] puntaje : listaPuntajes) {
                oos.writeObject(puntaje);
            }
        }
    }
}
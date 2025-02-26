package modelos;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;
import services.Serializador;
public class Juego extends ObservableRemoto implements IJuego, Serializable {
    private final ArrayList<IControladorRemoto> observers;

    // Mazo, variables para mano y ronda
    private final Mazo mazo;
    private final ArrayList<Mano> manos;
    private Mano manoActual;

    // Variables para estado de los jugadores
    private int jugadorActual; // Representa el jugador que tiene que jugar actualmente
    private int jugadorMano;

    private int jugadorOriginal;// Se utiliza para cuando se hace el canto, para que luego de la disputa se retorne
                                // el turno al jugador que cantó primero
    private int jugadorQuieroTruco;

    // Jugadores
    private Jugador jugador1;
    private Jugador jugador2;

    private Apuesta trucoActual;

    public Juego() throws RemoteException {
        observers = new ArrayList<>();
        manos = new ArrayList<>();
        mazo = new Mazo();
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
        return manos.size();
    }

    @Override
    public int getNumeroRonda() throws RemoteException{
        return manos.getLast().getNumeroRonda();
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
        return manos.getLast().getGanadorRonda();
    }

    @Override
    public String getNombreGanadorRonda() throws RemoteException{
        int ganadorRonda = getGanadorRonda();
        if (ganadorRonda == 1){
            return jugador1.getNombre();
        } else if (ganadorRonda == 2){
            return jugador2.getNombre();
        }
        return "";
    }

    @Override
    public int getGanadorMano() throws RemoteException{
        return manos.getLast().getGanadorMano();
    }

    @Override
    public String getNombreGanadorMano() throws RemoteException{
        int ganadorMano = getGanadorMano();
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
        return manos.getLast().getGanadorEnvido();
    }

    @Override
    public String getNombreGanadorEnvido() throws RemoteException{
        int ganadorEnvido = getGanadorEnvido();
        if (ganadorEnvido == 1){
            return jugador1.getNombre();
        } else if (ganadorEnvido == 2){
            return jugador2.getNombre();
        }
        return "";
    }

    @Override
    public String getCartaJugada(int jugador) throws RemoteException{
        return manos.getLast().getCarta(jugador).toString();
    }

    @Override
    public String getCartaGanadora() throws RemoteException{
        Carta cartaGanadora = manos.getLast().getCartaGanadora();
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
        return manos.getLast().getTrucoCantado();
    }

    @Override
    public Apuesta getTrucoActual() throws RemoteException{
        return trucoActual;
    }

    @Override
    public boolean getEnvidoCantado() throws RemoteException{
        return manos.getLast().getEnvidoCantado();
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
        jugadorActual = 1;
        jugadorMano = 2;
        iniciarMano();
    }

    @Override
    public void iniciarMano() throws RemoteException{
        manos.add(new Mano(cambiarJugadorMano()));

        //cambiarJugadorMano();
        jugadorActual = jugadorMano;

        mazo.mezclarMazo();
        mazo.repartirCartas(jugador1, jugador2);
        jugador1.calcularTanto();
        jugador2.calcularTanto();
        manoActual = manos.getLast();

        notificarObservadores(Evento.MOSTRAR_MENU);
    }

    @Override
    public int cambiarTurno() throws RemoteException{
        if (jugadorActual == 1){
            jugadorActual = 2;
        } else jugadorActual = 1;
        return jugadorActual;
    }

    @Override
    public int cambiarJugadorMano() throws RemoteException{
        if (jugadorMano == 1){
            jugadorMano = 2;
        } else jugadorMano = 1;
        return jugadorMano;
    }

    @Override
    public void jugarCarta(int numeroCarta) throws IOException {
        Carta cartaAJugar = null;
        if (jugadorActual == 1) {
            cartaAJugar = jugador1.jugarCarta(numeroCarta);
        } else if (jugadorActual == 2) {
            cartaAJugar = jugador2.jugarCarta(numeroCarta);
        }

        manoActual.jugarCarta(cartaAJugar, jugadorActual);

        notificarObservadoresJugarCarta();

        // Si todavía falta que un jugador juegue su carta, se cambia el turno
        if (manoActual.getCarta(1) == null || manoActual.getCarta(2) == null){
            notificarObservadores(Evento.CAMBIO_TURNO);
        } else {
            notificarObservadores(Evento.FIN_RONDA);
            limpiarRonda();

            // Si ya se jugaron todas las rondas, se procede a determinar el ganador de la mano
            // y se distribuyen los puntos, verificando también si ya se tienen los puntos suficientes para ganar

            if (manoActual.getNumeroRonda() > 2) {
                manoActual.determinarGanador();
                darPuntos(manoActual.getGanadorMano(), manoActual.getPuntosTruco());

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
    public void irseAlMazo() throws IOException{
        notificarObservadores(Evento.IRSE_AL_MAZO);
        cambiarTurno();
        manoActual.gana(jugadorActual);

        //darPuntos(ganadorMano, puntosTruco);
        if (!manoActual.getEnvidoCantado() && !manoActual.getTrucoCantado()) {
            darPuntos(manoActual.getGanadorMano(), 2);
        } else {
            darPuntos(manoActual.getGanadorMano(), manoActual.getPuntosTruco());
        }
        limpiarMano();
        notificarObservadores(Evento.FIN_MANO);
        if (finPartida()) {
            guardarPuntaje(getNombreJugadorActual());
            notificarObservadores(Evento.FIN_PARTIDA);
            return;
        }
        iniciarMano();
    }

    @Override
    public void guardarCartasJugadasAlMazo() throws RemoteException {
        Carta carta = manoActual.getCarta(1);
        if (carta != null) {
            mazo.recibirCarta(carta);
        }
        carta = manoActual.getCarta(2);
        if (carta != null) {
            mazo.recibirCarta(carta);
        }
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
        int ganadorRonda = manoActual.getGanadorRonda();
        if (ganadorRonda == 0){
            jugadorActual = jugadorMano;
        } else {
            jugadorActual = ganadorRonda;
        }
        manoActual.nuevaRonda();
    }

    @Override
    public void limpiarMano() throws RemoteException{
        trucoActual = null;
        guardarCartasJugadasAlMazo();
        guardarCartasNoJugadasAlMazo();
        jugadorQuieroTruco = 0;
    }

    /////////////////////////////////////////
    //////// MÉTODOS SOBRE APUESTAS /////////
    /////////////////////////////////////////

    @Override
    public void cantarTruco() throws RemoteException{
        if (!manoActual.getTrucoCantado()){
            manoActual.cantarTruco();
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
        //envidoCantado = true;
        manoActual.cantarEnvido();
        notificarObservadores(apuesta);
        jugadorOriginal = jugadorActual;
        cambiarTurno();
        notificarObservadores(Evento.RESPONDER_APUESTA);
    }

    // Para cantar envido cuando primero se canta truco
    @Override
    public void cantarEnvidoTruco(Apuesta apuesta) throws RemoteException{
        //envidoCantado = true;
        manoActual.cantarEnvido();
        notificarObservadores(apuesta);
        cambiarTurno();
        notificarObservadores(Evento.RESPONDER_APUESTA);
    }

    @Override
    public void quiero(Apuesta apuesta) throws IOException {
        manoActual.aumentarPuntos(apuesta, true);

        // Retorna el turno al jugador que apostó inicialmente
        notificarObservadores(Evento.DIJO_QUIERO);

        if (apuesta == Apuesta.TRUCO || apuesta == Apuesta.RETRUCO || apuesta == Apuesta.VALECUATRO) {
            jugadorQuieroTruco = jugadorActual;
            if (manoActual.getCarta(jugadorQuieroTruco) == null) {
                jugadorActual = jugadorQuieroTruco;
            } else jugadorActual = jugadorOriginal;
        } else jugadorActual = jugadorOriginal;

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
                //ganadorEnvido = calcularEnvido();
                int ganadorEnvido = manos.getLast().calcularGanadorEnvido(jugador1.getTanto(), jugador2.getTanto());
                //notificarObservadores(Evento.RESULTADO_ENVIDO);
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
                    notificarObservadores(Evento.RESULTADO_ENVIDO);
                } else {
                    notificarObservadores(Evento.MOSTRAR_MENU);
                    notificarObservadores(Evento.RESULTADO_ENVIDO);
                }
            }

            // Para el resto de los casos de envido
            default -> {
                int ganadorEnvido = manos.getLast().calcularGanadorEnvido(jugador1.getTanto(), jugador2.getTanto());
                notificarObservadores(Evento.RESULTADO_ENVIDO);
                darPuntos(ganadorEnvido, manoActual.getPuntosEnvido());
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
        manoActual.aumentarPuntos(apuesta, false);

        // Retorna el turno al jugador que apostó inicialmente
        switch (apuesta){
            case TRUCO, RETRUCO, VALECUATRO -> {
                notificarObservadores(Evento.DIJO_NO_QUIERO);
                cambiarTurno();
                manoActual.gana(jugadorActual);
                darPuntos(jugadorActual, manoActual.getPuntosTruco());
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
                darPuntos(jugadorActual, manoActual.getPuntosEnvido());
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
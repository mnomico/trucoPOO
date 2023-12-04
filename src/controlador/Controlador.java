package controlador;

import modelos.*;
import vistas.IVista;
import vistas.Observador;

public class Controlador implements Observador {
    private final ModeloTruco modelo;
    private final IVista vista;
    private final Jugador jugador;

    public Controlador(IVista vista, ModeloTruco modelo, Jugador jugador){
        vista.setControlador(this);
        this.modelo = modelo;
        this.modelo.ingresarJugador(jugador);
        this.vista = vista;
        this.jugador = jugador;
    }

    public Jugador getJugador(){
        return jugador;
    }

    public String getEstadoPartida(){
        return modelo.getEstadoPartida();
    }

    public String getTantos(){
        return modelo.getTantos();
    }

    public int getNumeroMano(){
        return modelo.getNumeroMano();
    }

    public int getNumeroRonda(){
        return modelo.getNumeroRonda();
    }

    public Jugador getJugadorActual(){
        return modelo.getJugadorActual();
    }

    public String getNombreJugadorActual(){
        return getJugadorActual().getNombre();
    }

    public Jugador getGanadorRonda(){
        return modelo.getGanadorRonda();
    }

    public Jugador getGanadorMano(){
        return modelo.getGanadorMano();
    }

    public Jugador getJugadorQuieroTruco(){
        return modelo.getJugadorQuieroTruco();
    }

    public Jugador getGanadorEnvido(){
        return modelo.getGanadorEnvido();
    }

    public Carta getCartaJugada(Jugador jugador){
        return modelo.getCartaJugada(jugador);
    }

    public boolean getTrucoCantado(){
        return modelo.getTrucoCantado();
    }

    public Apuesta getTrucoActual(){
        return modelo.getTrucoActual();
    }

    public boolean getEnvidoCantado(){
        return modelo.getEnvidoCantado();
    }

    public boolean esMiTurno(){
        return modelo.getJugadorActual() == jugador;
    }

    public void jugarCarta(int numeroCarta){
        modelo.jugarCarta(numeroCarta);
    }

    public void cantarTruco(){
        modelo.cantarTruco();
    }

    public void cantarEnvido(Apuesta apuesta){
        modelo.cantarEnvido(apuesta);
    }

    public void irseAlMazo(){
        modelo.irseAlMazo();
    }

    public void quiero(Apuesta apuesta){
        modelo.quiero(apuesta);
    }

    public void noQuiero(Apuesta apuesta){
        modelo.noQuiero(apuesta);
    }

    public void redoblarEnvido(Apuesta apuesta){
        modelo.redoblarEnvido(apuesta);
    }

    public void redoblarApuesta(Apuesta apuesta){
        modelo.redoblarApuesta(apuesta);
    }

    public void update(Observado o, Object arg) {
        if (arg instanceof Evento){
            switch ((Evento) arg){

                case MOSTRAR_MENU -> vista.mostrarMenuPrincipal();

                case JUGAR_CARTA -> {
                    String nombreJugadorActual = getNombreJugadorActual();
                    String cartaJugada = getCartaJugada(getJugadorActual()).toString();
                    vista.mostrarCartaJugada(nombreJugadorActual, cartaJugada);
                }

                case RESPONDER_APUESTA -> {
                    if (esMiTurno()) {
                        vista.mostrarResponderApuesta();
                    } else {
                        String nombreJugadorActual = getNombreJugadorActual();
                        vista.mostrarEsperandoRespuesta(nombreJugadorActual);
                    }
                }

                case DIJO_QUIERO -> {
                    String nombreJugadorActual = getNombreJugadorActual();
                    vista.mostrarDijoQuiero(nombreJugadorActual);
                }

                case DIJO_NO_QUIERO -> {
                    String nombreJugadorActual = getNombreJugadorActual();
                    vista.mostrarDijoNoQuiero(nombreJugadorActual);
                }

                case RESULTADO_ENVIDO -> {
                    String ganadorEnvido = getGanadorEnvido().getNombre();
                    String tantos = getTantos();
                    vista.mostrarResultadoEnvido(ganadorEnvido, tantos);
                }

                case IRSE_AL_MAZO -> {
                    String nombreJugadorActual = getNombreJugadorActual();
                    vista.mostrarIrseAlMazo(nombreJugadorActual);
                }

                case FIN_RONDA -> {
                    if (getGanadorRonda() == null){
                        vista.mostrarGanadorRonda(null);
                    } else {
                        String ganadorRonda = getGanadorRonda().getNombre();
                        vista.mostrarGanadorRonda(ganadorRonda);
                    }
                }

                case FIN_MANO -> {
                    String ganadorMano = getGanadorMano().getNombre();
                    vista.mostrarGanadorMano(ganadorMano);
                }

                case CAMBIO_TURNO -> vista.mostrarTurno(esMiTurno());

                case FIN_PARTIDA -> {
                    String jugadorGanador = getNombreJugadorActual();
                    vista.mostrarFinPartida(jugadorGanador);
                }

            }

        } else if (arg instanceof Apuesta apuestaActual){
            // Muestra la apuesta y luego muestra para responder
            String jugadorActual = getJugadorActual().getNombre();
            vista.mostrarApuesta(jugadorActual, apuestaActual);
        }
    }
}

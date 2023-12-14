package controlador;

import modelos.*;
import vistas.IVista;
import vistas.Observador;
import java.util.ArrayList;

public class Controlador implements Observador {
    private final ModeloTruco modelo;
    private final IVista vista;
    //private final Jugador jugador;
    private final int jugador;

    public Controlador(IVista vista, ModeloTruco modelo, int numeroJugador){
        vista.setControlador(this);
        this.modelo = modelo;
        this.vista = vista;
        this.jugador = numeroJugador;
    }

    public int getJugador(){
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

    public int getJugadorActual(){
        return modelo.getJugadorActual();
    }

    public String getNombreJugadorActual(){
        return modelo.getNombreJugadorActual();
    }

    public int getGanadorRonda(){
        return modelo.getGanadorRonda();
    }

    public String getNombreGanadorRonda(){
        return modelo.getNombreGanadorRonda();
    }

    public int getGanadorMano(){
        return modelo.getGanadorMano();
    }

    public String getNombreGanadorMano(){
        return modelo.getNombreGanadorMano();
    }

    public int getJugadorQuieroTruco(){
        return modelo.getJugadorQuieroTruco();
    }

    public int getGanadorEnvido(){
        return modelo.getGanadorEnvido();
    }

    public String getNombreGanadorEnvido(){
        return modelo.getNombreGanadorEnvido();
    }

    public ArrayList<String> getCartas(){
        return modelo.getCartas(jugador);
    }

    public String getCartaJugada(int jugador){
        return modelo.getCartaJugada(jugador);
    }

    public String getCartaGanadora(){
        return modelo.getCartaGanadora();
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

    public void cantarEnvidoTruco(Apuesta apuesta){
        modelo.cantarEnvidoTruco(apuesta);
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
                    String cartaJugada = getCartaJugada(getJugadorActual());
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
                    String ganadorEnvido = getNombreGanadorEnvido();
                    String tantos = getTantos();
                    vista.mostrarResultadoEnvido(ganadorEnvido, tantos);
                }

                case IRSE_AL_MAZO -> {
                    String nombreJugadorActual = getNombreJugadorActual();
                    vista.mostrarIrseAlMazo(nombreJugadorActual);
                }

                case FIN_RONDA -> {
                    if (getGanadorRonda() == 0){
                        vista.mostrarGanadorRonda(null);
                    } else {
                        String ganadorRonda = getNombreGanadorRonda();
                        vista.mostrarGanadorRonda(ganadorRonda);
                    }
                }

                case FIN_MANO -> {
                    String ganadorMano = getNombreGanadorMano();
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
            String jugadorActual = getNombreJugadorActual();
            vista.mostrarApuesta(jugadorActual, apuestaActual);
        }
    }
}

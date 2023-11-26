package controlador;

import modelos.Carta;
import modelos.Jugador;
import modelos.ModeloTruco;
import modelos.Apuesta;
import vista.IVista;

public class Controlador {
    private final IVista vista;
    private ModeloTruco modelo;
    private final Jugador jugador;

    public Controlador(IVista vista, ModeloTruco modelo, Jugador jugador){
        this.vista = vista;
        this.vista.setControlador(this);
        this.modelo = modelo;
        this.modelo.ingresarJugador(jugador);
        this.jugador = jugador;
    }

    public Jugador getJugador(){
        return jugador;
    }

    public String getEstadoPartida(){
        return modelo.getEstadoPartida();
    }

    public Jugador getJugadorActual(){
        return modelo.getJugadorActual();
    }

    public Jugador getGanadorRonda(){
        return modelo.getGanadorRonda();
    }

    public Jugador getGanadorMano(){
        return modelo.getGanadorMano();
    }

    public Carta getCartaJugada(Jugador jugador){
        return modelo.getCartaJugada(jugador);
    }

    public boolean getTrucoCantado(){
        return modelo.getTrucoCantado();
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

    public void quiero(Apuesta apuesta){
        modelo.quiero(apuesta);
    }

    public void noQuiero(Apuesta apuesta){
        modelo.noQuiero(apuesta);
    }

    public void redoblarApuesta(Apuesta apuesta){
        modelo.redoblarApuesta(apuesta);
    }

    public void redoblarApuesta(Apuesta apuesta, Apuesta apuestaARedoblar){
        modelo.redoblarApuesta(apuesta, apuestaARedoblar);
    }

}

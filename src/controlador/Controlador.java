package controlador;

import modelos.Carta;
import modelos.Jugador;
import modelos.ModeloTruco;
import modelos.Apuesta;
import vista.IVista;

public class Controlador {
    private final ModeloTruco modelo;
    private final Jugador jugador;

    public Controlador(IVista vista, ModeloTruco modelo, Jugador jugador){
        vista.setControlador(this);
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

    public Jugador getGanadorRonda(){
        return modelo.getGanadorRonda();
    }

    public Jugador getGanadorMano(){
        return modelo.getGanadorMano();
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

    public void cantarEnvido(){
        modelo.cantarEnvido();
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

}

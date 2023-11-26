package controlador;

import modelos.Carta;
import modelos.Jugador;
import modelos.ModeloTruco;
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

    public void setModelo(ModeloTruco modelo){
        this.modelo = modelo;
    }

    public Jugador getJugador(){
        return jugador;
    }

    public int getNumeroRonda(){
        return modelo.getNumeroRonda();
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

    public boolean esMiTurno(){
        return modelo.getJugadorActual() == jugador;
    }

    public void cambiarTurno(){
        modelo.cambiarTurno();
    }

    public void jugarCarta(int numeroCarta){
        modelo.jugarCarta(numeroCarta);
    }

    public void cantarTruco(){
        modelo.cantarTruco();
    }

}

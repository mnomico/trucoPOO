package controlador;

import modelos.Evento;
import modelos.Jugador;
import modelos.ModeloTruco;
import vista.IVista;

import java.util.Observable;
import java.util.Observer;

public class Controlador implements Observer {
    private final IVista vista;
    private ModeloTruco modelo;
    private final Jugador jugador;

    public Controlador(IVista vista, ModeloTruco modelo, Jugador jugador){
        this.vista = vista;
        this.vista.setControlador(this);
        this.modelo = modelo;
        this.modelo.addObserver(this);
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



    public boolean esMiTurno(){
        return modelo.getJugadorActual() == jugador;
    }

    public void cambiarTurno(){
        modelo.cambiarTurno();
    }

    public void jugarCarta(int numeroCarta){
        modelo.jugarCarta(numeroCarta);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Evento){
            switch ((Evento) arg){
                case JUGAR_CARTA -> {
                    String nombreJugadorActual = getJugadorActual().getNombre();
                    String cartaJugada = getJugadorActual().getUltimaCartaJugada().toString();
                    vista.mostrarJugarCarta(nombreJugadorActual, cartaJugada);
                    /*if (!esMiTurno()){
                        cambiarTurno();
                        vista.mostrarMenuPrincipal();
                    }

                     */
                }
            }
        }
    }
}

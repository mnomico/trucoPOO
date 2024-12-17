package vistas;

import controlador.Controlador;
import modelos.Apuesta;

import java.rmi.RemoteException;

public interface IVista {

    int returnFrameXPos();

    int returnFrameYPos();

    void setLocation(int x, int y);

    void setVisible(boolean flag);

    //void setControlador(Controlador controlador);

    void mostrarMenuPrincipal();

    void mostrarOpcionesRonda();

    void mostrarPuntos() throws RemoteException;

    void mostrarCartas();

    // FUNCIONES PARA MOSTRAR

    public void mostrarCartaJugada(String jugadorActual, String cartaJugada);

    public void mostrarResponderApuesta();

    public void mostrarEsperandoRespuesta(String jugadorActual);

    public void mostrarDijoQuiero(String jugadorActual);

    public void mostrarDijoNoQuiero(String jugadorActual);

    public void mostrarResultadoEnvido(String ganadorEnvido, String tantos);

    public void mostrarIrseAlMazo(String jugadorActual);

    public void mostrarGanadorRonda(String ganadorRonda);

    public void mostrarGanadorMano(String ganadorMano);

    public void mostrarTurno(boolean esMiTurno);

    public void mostrarFinPartida(String jugadorGanador);

    public void mostrarApuesta(String jugadorActual, Apuesta apuestaActual);

}

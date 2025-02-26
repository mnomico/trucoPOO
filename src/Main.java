import modelos.Juego;
import controlador.Controlador;
import vistas.ConsolaGrafica;
import vistas.IVista;
import vistas.VistaGrafica;

import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws RemoteException {
        Juego juego = new Juego();

        Controlador controlador1 = new Controlador(juego);
        Controlador controlador2 = new Controlador(juego);

        IVista vista1 = new VistaGrafica(controlador1);
        IVista vista2 = new VistaGrafica(controlador2);

        controlador1.ingresarJugador("Jugador 1");
        controlador2.ingresarJugador("Jugador 2");
    }
}
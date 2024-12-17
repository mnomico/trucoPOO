import modelos.Jugador;
import modelos.ModeloTruco;
import controlador.Controlador;
import vistas.ConsolaGrafica;
import vistas.IVista;
import vistas.VistaGrafica;

import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws RemoteException {
        ModeloTruco juego = new ModeloTruco();

        Controlador controlador1 = new Controlador(juego);
        Controlador controlador2 = new Controlador(juego);

        IVista vista1 = new VistaGrafica(controlador1);
        IVista vista2 = new ConsolaGrafica(controlador2);

        controlador1.ingresarJugador("Jugador 1");
        controlador2.ingresarJugador("Jugador 2");

        //juego.setObservers(controlador1);
        //juego.setObservers(controlador2);

        //vista1.setVisible(true);
        //vista2.setVisible(true);

        //vista1.mostrarMenuPrincipal();
        //vista2.mostrarMenuPrincipal();
    }
}

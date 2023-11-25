import modelos.Jugador;
import modelos.ModeloTruco;
import controlador.Controlador;
import vista.ConsolaGrafica;
import vista.IVista;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ModeloTruco juego = new ModeloTruco();
        Jugador jugador1 = new Jugador("Maria");
        Jugador jugador2 = new Jugador("Laurence");

        juego.ingresarJugador(jugador1);
        juego.ingresarJugador(jugador2);

        juego.iniciarJuego();

        IVista vista1 = new ConsolaGrafica();
        IVista vista2 = new ConsolaGrafica();

        Controlador controlador1 = new Controlador(vista1, juego, jugador1);
        Controlador controlador2 = new Controlador(vista2, juego, jugador2);

        juego.setObservers(controlador1);
        juego.setObservers(controlador2);

        vista1.setControlador(controlador1);
        vista2.setControlador(controlador2);

        vista1.mostrarMenuPrincipal();
        vista2.mostrarMenuPrincipal();

    }
}

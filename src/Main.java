import modelos.Jugador;
import modelos.ModeloTruco;
import controlador.Controlador;
import vistas.ConsolaGrafica;
import vistas.IVista;
import vistas.VistaGrafica;

public class Main {
    public static void main(String[] args) {
        ModeloTruco juego = new ModeloTruco();

        Jugador jugador1 = new Jugador("JUGADOR 1");
        Jugador jugador2 = new Jugador("JUGADOR 2");

        juego.ingresarJugador(jugador1);
        juego.ingresarJugador(jugador2);

        juego.iniciarJuego();

        IVista vista1 = new VistaGrafica();
        IVista vista2 = new ConsolaGrafica();

        vista2.setLocation(vista1.returnFrameXPos(), vista1.returnFrameYPos());

        Controlador controlador1 = new Controlador(vista1, juego, 1);
        Controlador controlador2 = new Controlador(vista2, juego, 2);

        vista1.setControlador(controlador1);
        vista2.setControlador(controlador2);

        juego.setObservers(controlador1);
        juego.setObservers(controlador2);

        vista1.mostrarMenuPrincipal();
        vista2.mostrarMenuPrincipal();
    }
}

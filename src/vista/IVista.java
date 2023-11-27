package vista;

import controlador.Controlador;

public interface IVista {

    int returnFrameXPos();

    int returnFrameYPos();

    void setLocation(int x, int y);

    void setControlador(Controlador controlador);

    void mostrarMenuPrincipal();

    void mostrarOpcionesRonda();

    void mostrarPuntos();

    void mostrarCartas();

}

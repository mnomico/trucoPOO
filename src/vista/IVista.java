package vista;

import controlador.Controlador;

public interface IVista {

    void setControlador(Controlador controlador);

    void mostrarMenuPrincipal();

    void mostrarJugarCarta(String jugador, String carta);

}

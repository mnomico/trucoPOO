package vista;

import controlador.Controlador;

public abstract class Flujo {

    protected final ConsolaGrafica vista;
    protected final Controlador controlador;

    public Flujo(ConsolaGrafica vista, Controlador controlador) {
        this.vista = vista;
        this.controlador = controlador;
    }

    public abstract Flujo procesarEntrada(String string);

    public abstract void mostrarSiguienteTexto();

}

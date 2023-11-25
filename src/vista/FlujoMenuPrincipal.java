package vista;

import controlador.Controlador;

public class FlujoMenuPrincipal extends Flujo{

    public FlujoMenuPrincipal(ConsolaGrafica vista, Controlador controlador){
        super(vista, controlador);
    }

    @Override
    public Flujo procesarEntrada(String input) {
        switch (input){
            case "1" -> {
                return new FlujoRonda(vista, controlador);
            }
        }
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {

        vista.println(" Truco - Menú principal - ");
        vista.println(" 1 - Jugar");
        vista.println("Ingrese una opción");

    }
}

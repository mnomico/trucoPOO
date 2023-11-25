package vista;

import controlador.Controlador;

public class FlujoPrimerRonda extends Flujo{

    public FlujoPrimerRonda(ConsolaGrafica vista, Controlador controlador){
        super(vista, controlador);
    }

    enum Estados {
        COMIENZO_RONDA,
        JUGAR_CARTA,

        ENVIDO,
    }

    private Estados estadoActual = Estados.COMIENZO_RONDA;

    @Override
    public Flujo procesarEntrada(String input) {
        switch (estadoActual){
            case COMIENZO_RONDA -> {
                switch (input){
                    // Juega una carta
                    case "1" -> estadoActual = Estados.JUGAR_CARTA;
                    // Canta truco
                    case "2" -> procesarTruco();
                    // Canta envido
                    case "3" -> estadoActual = Estados.ENVIDO;
                    // Se va al mazo
                    case "4" -> procesarIrse();
                    // Opción inválida
                    default -> vista.println("Opción inválida");
                }
            }
            case JUGAR_CARTA -> {
                switch (input) {
                    case "0" -> procesarJugarCarta(0);
                    case "1" -> procesarJugarCarta(1);
                    case "2" -> procesarJugarCarta(2);
                    default -> vista.println("Opción inválida");
                }
            }
        }

        return this;
    }

    public Flujo procesarJugarCarta(int numeroCarta){
        controlador.jugarCarta(numeroCarta);
        controlador.cambiarTurno();
        return new FlujoRonda(vista, controlador);
    }

    public Flujo procesarTruco(){
        return this;
    }

    public Flujo procesarIrse(){
        return this;
    }

    @Override
    public void mostrarSiguienteTexto() {
        if (estadoActual == Estados.COMIENZO_RONDA) {
            vista.println("");
            // Muestra los jugadores y sus puntos
            vista.println(controlador.getEstadoPartida());
            // Muestra las cartas
            vista.println("Cartas:" + "\n");
            vista.println(controlador.getJugador().mostrarCartas());
        }
        if (!controlador.esMiTurno()){
            vista.println("TURNO DE " + controlador.getJugadorActual().getNombre());
            vista.println("Esperando respuesta...");
        } else {
            switch (estadoActual) {
                case COMIENZO_RONDA -> {
                    vista.println("ES TU TURNO.");

                    // Muestra las opciones
                    vista.println("OPCIONES:");
                    vista.println("1 - Jugar carta");
                    vista.println("2 - Cantar truco");
                    vista.println("3 - Cantar envido");
                    vista.println("4 - Irse al mazo");
                    vista.println("Elija una opción:");
                }
                case JUGAR_CARTA -> {
                    // Muestra las cartas que puede jugar
                    vista.println("\n" + "Cartas:");
                    vista.println(controlador.getJugador().mostrarCartas());
                    vista.println("Elija una carta:");
                }
                case ENVIDO -> {
                    //
                    vista.println("\n" + "Envido:");
                    vista.println("1 - Envido");
                    vista.println("2 - Real envido");
                    vista.println("3 - Falta envido");
                    vista.println("Elija una opción:");
                }
            }
        }
    }
}

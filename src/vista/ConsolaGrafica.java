package vista;

import controlador.Controlador;
import modelos.Apuesta;
import modelos.Evento;
import modelos.Observado;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ConsolaGrafica implements Observador, IVista {

    // Elementos de la consola gráfica
    private final JFrame frame;
    private JTextArea console;
    private JTextField inputField;
    private JButton enterButton;
    private JPanel panel2;
    private JScrollPane scrollBar;

    // Controlador
    private Controlador controlador;

    public ConsolaGrafica() {
        frame = new JFrame ("Truco");
        frame.setContentPane(panel2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(400,600);

        Action procesarInputField = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                console.append(inputField.getText() + "\n");
                procesarEntrada(inputField.getText());
                console.setCaretPosition(console.getDocument().getLength());
                inputField.setText("");
            }
        };

        enterButton.addActionListener(procesarInputField);
        inputField.addActionListener(procesarInputField);
    }

    public int returnFrameXPos(){
        return frame.getX();
    }

    public int returnFrameYPos(){
        return frame.getY();
    }

    public void setLocation(int x, int y){
        frame.setLocation(x + frame.getWidth(), y);
    }

    enum Estado{
        MENU_PRINCIPAL,
        ELEGIR_CARTA,
        RESPONDER_APUESTA,
        FIN_PARTIDA,
    }

    private Estado estadoActual = Estado.MENU_PRINCIPAL;
    private Apuesta apuestaActual;

    private void procesarEntrada(String input) {
        if (controlador.esMiTurno()) {
            enviarEntrada(input);
        } else println("\n" + "Todavía no es tu turno.");
    }

    public void enviarEntrada(String input) {
        input = input.trim();
        if (input.isEmpty()){
            return;
        }
        switch (estadoActual){
            case MENU_PRINCIPAL -> procesarMenuPrincipal(input);
            case ELEGIR_CARTA -> procesarCartaElegida(input);
            case RESPONDER_APUESTA -> procesarApuesta(input);
        }
    }

    public void procesarMenuPrincipal(String input){
        switch (input){
            // Elige jugar una carta
            case "1" -> {
                mostrarCartas();
                println("Elija una carta:");
                estadoActual = Estado.ELEGIR_CARTA;
            }
            // Elige cantar truco
            case "2" -> {
                if (controlador.getTrucoCantado()){
                    println("\n" + "Ya se cantó truco en esta mano.");
                    mostrarOpcionesRonda();
                } else {
                    controlador.cantarTruco();
                }
            }
            // Elige cantar envido
            case "3" -> {
                if (controlador.getNumeroRonda() != 1){
                    println("\n Sólo se puede cantar envido en la primera ronda.");
                    mostrarOpcionesRonda();
                } else if (controlador.getEnvidoCantado()){
                    println("\n" + "Ya se cantó envido en esta mano.");
                    mostrarOpcionesRonda();
                } else {
                    mostrarEnvidoInicial();
                    estadoActual = Estado.RESPONDER_APUESTA;
                }
            }
            // Elige irse al mazo
            case "4" -> controlador.irseAlMazo();
            // Ingreso inválido
            default -> {
                println("\n" + "Opción inválida");
                mostrarOpcionesRonda();
            }
        }
    }

    public void procesarCartaElegida(String input) {
        switch (input) {
            case "0" -> controlador.jugarCarta(0);
            case "1" -> controlador.jugarCarta(1);
            case "2" -> controlador.jugarCarta(2);
            default -> {
                println("Opción inválida" + "\n");
                procesarMenuPrincipal("1");
            }
        }
    }

    public void println(String texto) {
        console.append(texto + "\n");
    }

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarMenuPrincipal() {
        frame.setTitle("Truco - JUGADOR: " + controlador.getJugador().getNombre());

        // Muestra los jugadores y sus puntos
        println("\n --- PUNTOS ---");
        mostrarPuntos();

        // Muestra el número de la mano
        println("\n --- MANO " + controlador.getNumeroMano() + " - RONDA " + controlador.getNumeroRonda() + " ---");

        // Muestra las cartas del jugador
        mostrarCartas();

        if (controlador.esMiTurno()) {
            println("--- ES TU TURNO ---");
            mostrarOpcionesRonda();
        } else {
            println("--- TURNO DE " + controlador.getJugadorActual().getNombre() + " ---");
            println("Esperando respuesta...");
        }
        estadoActual = Estado.MENU_PRINCIPAL;
    }

    public void mostrarOpcionesRonda(){
        println("\n --- OPCIONES ---");
        println("1 - Jugar carta");
        println("2 - Cantar truco");
        println("3 - Cantar envido");
        println("4 - Irse al mazo");
        println("Elija una opción:");
    }

    public void mostrarPuntos(){
        println(controlador.getEstadoPartida());
    }

    public void mostrarCartas(){
        println("\n --- CARTAS ---");
        println(controlador.getJugador().mostrarCartas());
    }

    public void procesarApuesta(String input){
        // Para el primer envido
        if (apuestaActual == null){
            switch (input) {
                case "1" -> {
                    apuestaActual = Apuesta.ENVIDO;
                    controlador.cantarEnvido(apuestaActual);
                }
                case "2" -> {
                    apuestaActual = Apuesta.REAL_ENVIDO;
                    controlador.cantarEnvido(apuestaActual);
                }
                case "3" -> {
                    apuestaActual = Apuesta.FALTA_ENVIDO;
                    controlador.cantarEnvido(apuestaActual);
                }
                default -> {
                    println("Opción inválida");
                    mostrarEnvidoInicial();
                }
            }
            return;
        }

        switch (apuestaActual){

            case TRUCO, RETRUCO, ENVIDO_ENVIDO, REAL_ENVIDO -> {
                switch (input){
                    case "1" -> controlador.quiero(apuestaActual);
                    case "2" -> controlador.noQuiero(apuestaActual);
                    case "3" -> controlador.redoblarApuesta(apuestaActual);
                    default -> {
                        println("Opción inválida");
                        mostrarResponderApuesta();
                    }
                }
            }
            case VALECUATRO, FALTA_ENVIDO, ENVIDO_FALTA_ENVIDO, REAL_ENVIDO_FALTA_ENVIDO, ENVIDO_ENVIDO_REAL_ENVIDO_FALTA_ENVIDO -> {
                switch (input){
                    case "1" -> controlador.quiero(apuestaActual);
                    case "2" -> controlador.noQuiero(apuestaActual);
                    default -> {
                        println("Opción inválida");
                        mostrarResponderApuesta();
                    }
                }
            }
            case ENVIDO -> {
                switch (input){
                    case "1" -> controlador.quiero(apuestaActual);
                    case "2" -> controlador.noQuiero(apuestaActual);
                    case "3" -> controlador.redoblarEnvido(Apuesta.ENVIDO_ENVIDO);
                    case "4" -> controlador.redoblarEnvido(Apuesta.ENVIDO_REAL_ENVIDO);
                    case "5" -> controlador.redoblarEnvido(Apuesta.ENVIDO_FALTA_ENVIDO);
                    default -> {
                        println("Opción inválida");
                        mostrarResponderApuesta();
                    }
                }
            }

            case ENVIDO_REAL_ENVIDO -> {
                switch (input){
                    case "1" -> controlador.quiero(apuestaActual);
                    case "2" -> controlador.noQuiero(apuestaActual);
                    case "3" -> controlador.redoblarEnvido(Apuesta.ENVIDO_REAL_ENVIDO_FALTA_ENVIDO);
                    default -> {
                        println("Opción inválida");
                        mostrarResponderApuesta();
                    }
                }
            }

            case ENVIDO_ENVIDO_REAL_ENVIDO -> {
                switch (input){
                    case "1" -> controlador.quiero(apuestaActual);
                    case "2" -> controlador.noQuiero(apuestaActual);
                    case "3" -> controlador.redoblarEnvido(Apuesta.ENVIDO_ENVIDO_REAL_ENVIDO_FALTA_ENVIDO);
                    default -> {
                        println("Opción inválida");
                        mostrarResponderApuesta();
                    }
                }
            }
        }
    }

    public void mostrarEnvidoInicial(){
        println("\n -- ENVIDO --");
        println("(1) - ENVIDO");
        println("(2) - REAL ENVIDO");
        println("(3) - FALTA ENVIDO");
        println("Elija una opción:");
    }

    public void mostrarResponderApuesta(){
        println("\n -- RESPONDÉ --");
        println("(1) - Quiero");
        println("(2) - No quiero");
        switch(apuestaActual){
            case TRUCO -> println("(3) - RETRUCO");
            case RETRUCO -> println("(3) - VALECUATRO");
            case ENVIDO -> {
                println("(3) - ENVIDO");
                println("(4) - REAL ENVIDO");
                println("(5) - FALTA ENVIDO");
            }
            case ENVIDO_ENVIDO -> println("(3) - REAL ENVIDO");
            case ENVIDO_ENVIDO_REAL_ENVIDO, ENVIDO_REAL_ENVIDO, REAL_ENVIDO -> println("(3) - FALTA ENVIDO");
        }
        println("Elija una opción:");
        estadoActual = Estado.RESPONDER_APUESTA;
    }

    public void mostrarApuesta(){
        String jugadorActual = controlador.getJugadorActual().getNombre();
        println("\n * " + jugadorActual + " canta " + apuestaActual.toString());
        estadoActual = Estado.RESPONDER_APUESTA;
    }

    @Override
    public void update(Observado o, Object arg) {
        if (arg instanceof Evento){
            switch ((Evento) arg){

                case MOSTRAR_MENU -> mostrarMenuPrincipal();

                case JUGAR_CARTA -> {
                    String nombreJugadorActual = controlador.getJugadorActual().getNombre();
                    String cartaJugada = controlador.getCartaJugada(controlador.getJugadorActual()).toString();
                    println("\n * " + nombreJugadorActual + " juega " + cartaJugada + "\n");
                }

                case RESPONDER_APUESTA -> {
                    if (controlador.esMiTurno()) {
                        mostrarResponderApuesta();
                    } else {
                        String jugadorActual = controlador.getJugadorActual().getNombre();
                        println("Esperando respuesta de " + jugadorActual + "...");
                    }
                }

                case DIJO_QUIERO -> {
                    String jugadorActual = controlador.getJugadorActual().getNombre();
                    println("\n" + jugadorActual + " dijo QUIERO");
                }

                case DIJO_NO_QUIERO -> {
                    String jugadorActual = controlador.getJugadorActual().getNombre();
                    println("\n" + jugadorActual + " dijo NO QUIERO");
                }

                case RESULTADO_ENVIDO -> {
                    println("\n --- RESULTADO ENVIDO ---");
                    String ganadorEnvido = controlador.getGanadorEnvido().getNombre();
                    println(controlador.getTantos());
                    println(" --- GANA " + ganadorEnvido + "---");
                }

                case IRSE_AL_MAZO -> {
                    String jugadorActual = controlador.getJugadorActual().getNombre();
                    println("\n" + jugadorActual + " se fue al mazo.");
                }

                case FIN_RONDA -> {
                    if (controlador.getGanadorRonda() == null){
                        println("\n" + "Ronda parda");
                    } else {
                        String ganadorRonda = controlador.getGanadorRonda().getNombre();
                        println(ganadorRonda + " ha ganado la ronda.");
                    }
                    println("\n ----------------------------------------------- ");
                }

                case FIN_MANO -> {
                    String ganadorMano = controlador.getGanadorMano().getNombre();
                    println("\n" + ganadorMano + " ha ganado la mano.");
                    println("\n ----------------------------------------------- ");
                }

                case CAMBIO_TURNO -> {
                    if (controlador.esMiTurno()) {
                        println("--- ES TU TURNO ---");
                        mostrarOpcionesRonda();
                    } else {
                        println("--- TURNO DE " + controlador.getJugadorActual().getNombre() + " ---");
                        println("Esperando respuesta...");
                    }
                }

                case FIN_PARTIDA -> {
                    println("\n --- FIN DE LA PARTIDA ---");
                    mostrarPuntos();
                    String jugadorActual = controlador.getJugadorActual().getNombre();
                    println("\n" + jugadorActual + " HA GANADO.");
                    estadoActual = Estado.FIN_PARTIDA;
                }

            }

        } else if (arg instanceof Apuesta){
            // Muestra la apuesta y luego muestra para responder
            apuestaActual = (Apuesta) arg;
            mostrarApuesta();
        }
    }

}

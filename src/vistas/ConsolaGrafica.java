package vistas;

import controlador.Controlador;
import modelos.Apuesta;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ConsolaGrafica implements IVista {

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

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
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

    // ESTADOS DE FLUJO

    enum Estado{
        MENU_PRINCIPAL,
        ELEGIR_CARTA,
        RESPONDER_APUESTA,
        FIN_PARTIDA,
    }
    private Estado estadoActual = Estado.MENU_PRINCIPAL;
    private Apuesta apuestaActual;

    // MÉTODOS SOBRE ENTRADA DE DATOS

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
                println("\t           (X) - Volver atrás");
                println("Elija una opción:");
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

    // MÉTODOS SOBRE SALIDA DE DATOS

    public void println(String texto) {
        console.append(texto + "\n");
    }

    @Override
    public void mostrarMenuPrincipal() {

        // Muestra los jugadores y sus puntos
        println("\n\t ------------ PUNTOS ------------");
        mostrarPuntos();

        // Muestra el número de la mano
        println("\n\t ---- MANO " + controlador.getNumeroMano() + " - RONDA " + controlador.getNumeroRonda() + " ----");

        // Muestra las cartas del jugador
        mostrarCartas();

        if (controlador.esMiTurno()) {
            println("\t-------- ES TU TURNO --------");
            mostrarOpcionesRonda();
        } else {
            println("\t--- TURNO DE " + controlador.getJugadorActual().getNombre() + " ---");
            println("\nEsperando respuesta...");
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
        println("\n\t ------------ CARTAS ------------");
        println(controlador.getJugador().mostrarCartas());
    }

    public void mostrarEnvidoInicial(){
        println("\n -- ENVIDO --");
        println("(1) - ENVIDO");
        println("(2) - REAL ENVIDO");
        println("(3) - FALTA ENVIDO");
        println("Elija una opción:");
    }

    ////////////////////////////
    /// MÉTODOS PARA MOSTRAR ///
    ////////////////////////////

    public void mostrarCartaJugada(String jugadorActual, String cartaJugada){
        println("\n\t * " + jugadorActual + " juega " + cartaJugada + "\n");
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

    public void mostrarEsperandoRespuesta(String jugadorActual){
        println("Esperando respuesta de " + jugadorActual + "...");
    }

    public void mostrarDijoQuiero(String jugadorActual){
        println("\n\t * " +  jugadorActual + " dijo QUIERO");
    }

    public void mostrarDijoNoQuiero(String jugadorActual){
        println("\n\t * " + jugadorActual + " dijo NO QUIERO");
    }

    public void mostrarResultadoEnvido(String ganadorEnvido, String tantos){
        println("\n\t --- RESULTADO ENVIDO ---");
        println(tantos);
        println("\t --- GANA " + ganadorEnvido + "---");
    }

    public void mostrarIrseAlMazo(String jugadorActual){
        println("\n\t * " + jugadorActual + " se fue al mazo.");
    }

    public void mostrarGanadorRonda(String ganadorRonda){
        if (ganadorRonda == null){
            println(" * PARDA *");
        } else {
            println("\n\t * " + ganadorRonda + " ganó la ronda.");
        }
    }

    public void mostrarGanadorMano(String ganadorMano){
        println("\n\t * " + ganadorMano + " ganó la mano.");
    }

    public void mostrarTurno(boolean esMiTurno){
        if (esMiTurno){
            println("\t--- ES TU TURNO ---");
            mostrarOpcionesRonda();
        } else {
            println("\t--- TURNO DE " + controlador.getJugadorActual().getNombre() + " ---");
            println("\nEsperando respuesta...");
        }
    }

    public void mostrarFinPartida(String jugadorGanador){
        println("\n\t --- FIN DE LA PARTIDA ---");
        mostrarPuntos();
        println("\n\t ** " + jugadorGanador + " HA GANADO. **");
        estadoActual = ConsolaGrafica.Estado.FIN_PARTIDA;
    }

    public void mostrarApuesta(String jugadorActual, Apuesta apuesta){
        apuestaActual = apuesta;
        println("\n\t * " + jugadorActual + " canta " + apuestaActual.toString());
        estadoActual = Estado.RESPONDER_APUESTA;
    }

}

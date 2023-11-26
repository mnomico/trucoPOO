package vista;

import controlador.Controlador;
import modelos.Apuesta;
import modelos.Evento;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class ConsolaGrafica implements Observer, IVista {

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

        this.frame = new JFrame ("Truco");
        frame.setContentPane(panel2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(350,600);

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

    enum Estado{
        MENU_PRINCIPAL,
        ELEGIR_CARTA,
        RESPONDER_APUESTA,
    }

    private Estado estadoActual = Estado.MENU_PRINCIPAL;
    private Apuesta apuestaActual;

    private void procesarEntrada(String input) {
        if (controlador.esMiTurno()) {
            enviarEntrada(input);
        } else println("Todavía no es tu turno.");
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
            case "1" -> {
                mostrarCartas();
                println("Elija una carta:");
                estadoActual = Estado.ELEGIR_CARTA;
            }
            case "2" -> {
                controlador.cantarTruco();
            }
            default -> {
                println("Opción inválida" + "\n");
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
        println("PUNTAJES:");
        mostrarPuntos();

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
        println("OPCIONES:");
        println("1 - Jugar carta");
        println("2 - Cantar truco");
        println("3 - Cantar envido");
        println("4 - Irse al mazo");
        println("Elija una opción:");
    }

    public void mostrarPuntos(){
        println(controlador.getEstadoPartida() + "\n");
    }

    public void mostrarCartas(){
        println(" -- CARTAS --");
        println(controlador.getJugador().mostrarCartas());
    }

    public void procesarApuesta(String input){
        switch (apuestaActual){
            case TRUCO, RETRUCO -> {
                switch (input){
                    //case "1" -> controlador.quiero(apuestaActual);
                    //case "2" -> controlador.noQuiero(apuestaActual);
                    //case "3" -> controlador.redoblarApuesta(apuestaActual);
                    default -> {
                        println("Opción inválida");
                        mostrarResponderApuesta(apuestaActual);
                    }
                }
            }
            case VALECUATRO -> {
                switch (input){
                    //case "1" -> controlador.quiero(apuestaActual);
                    //case "2" -> controlador.noQuiero(apuestaActual);
                    default -> {
                        println("Opción inválida");
                        mostrarResponderApuesta(apuestaActual);
                    }
                }
            }
        }
    }

    public void mostrarResponderApuesta(Apuesta apuesta){
        switch(apuesta){
            case TRUCO -> {
                println(" -- RESPONDÉ --");
                println("(1) - Quiero");
                println("(2) - No quiero");
                println("(3) - Retruco");
                println("Elija una opción:");
            }
        }
        estadoActual = Estado.RESPONDER_APUESTA;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Evento){
            switch ((Evento) arg){
                case MOSTRAR_MENU -> {
                    mostrarMenuPrincipal();
                }
                case JUGAR_CARTA -> {
                    String nombreJugadorActual = controlador.getJugadorActual().getNombre();
                    String cartaJugada = controlador.getCartaJugada(controlador.getJugadorActual()).toString();
                    println("\n * " + nombreJugadorActual + " juega " + cartaJugada + "\n");
                }
                case TRUCO -> {
                    String nombreJugadorActual = controlador.getJugadorActual().getNombre();
                    println("\n" + nombreJugadorActual + " cantó TRUCO.");
                }
                case FIN_RONDA -> {
                    String ganadorRonda = controlador.getGanadorRonda().getNombre();
                    println(ganadorRonda + " ha ganado la ronda.");
                    println("\n ------------------ \n");
                    //mostrarMenuPrincipal();
                }
                case FIN_MANO -> {
                    String ganadorMano = controlador.getGanadorMano().getNombre();
                    println(ganadorMano + " ha ganado la mano.");
                    println("\n ------------------ \n");
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
            }
        } else if (arg instanceof Apuesta){
            // Para responder apuestas
            apuestaActual = (Apuesta) arg;
            mostrarResponderApuesta((Apuesta) arg);
        }
        estadoActual = Estado.MENU_PRINCIPAL;
    }

}

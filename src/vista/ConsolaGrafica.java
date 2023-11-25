package vista;

import controlador.Controlador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConsolaGrafica implements IVista {

    // Elementos de la consola gráfica
    private final JFrame frame;
    private JTextArea console;
    private JTextField inputField;
    private JButton enterButton;
    private JPanel panel2;
    private JScrollPane scrollBar;

    // Flujo
    private Flujo flujoActual;

    // Controlador
    private Controlador controlador;

    public ConsolaGrafica() {
        this.frame = new JFrame ("Truco");
        frame.setContentPane(panel2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800,600);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                console.append(inputField.getText() + "\n");
                procesarEntrada(inputField.getText());
                inputField.setText("");
            }
        });
    }

    private void procesarEntrada(String input) {
        input = input.trim();
        if (input.isEmpty())
            return;
        flujoActual = flujoActual.procesarEntrada(input);
        flujoActual.mostrarSiguienteTexto();
    }

    public void println(String texto) {
        console.append(texto + "\n");
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    public void mostrarJugarCarta(String jugador, String carta){
        println("\n " + jugador + " juega " + carta + "\n");
    }



    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarMenuPrincipal() {
        mostrar();
        flujoActual = new FlujoMenuPrincipal(this, controlador);
        flujoActual.mostrarSiguienteTexto();
    }

}

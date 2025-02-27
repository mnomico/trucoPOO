package vistas;

import controlador.Controlador;
import modelos.Apuesta;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class VistaGrafica implements IVista {

    private final Controlador controlador;

    private final JFrame ventanaPrincipal = new JFrame("Truco");
    private JPanel mesa = new JPanel() {
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
        }
    };
    private final JTextPane consola = new JTextPane();
    private final StyledDocument doc = consola.getStyledDocument();
    private final JPanel opciones = new JPanel();
    private final JPanel cartasJugador = new JPanel();
    private JPanel subpanel = new JPanel();

    private final JPanel cartasJugadas = new JPanel();
    private final JLayeredPane[] paneles;

    private final JButton botonQuiero = new JButton("QUIERO");
    private final JButton botonNoQuiero = new JButton("NO QUIERO");
    private final JButton botonTruco = new JButton("TRUCO");
    private final JButton botonRetruco = new JButton("RETRUCO");
    private final JButton botonValecuatro = new JButton("VALECUATRO");
    private final JButton botonPrimerEnvido = new JButton("ENVIDO");
    private final JButton botonEnvido = new JButton("ENVIDO");
    private final JButton botonRealEnvido = new JButton("REAL ENVIDO");
    private final JButton botonFaltaEnvido = new JButton("FALTA ENVIDO");
    private final JButton botonMazo = new JButton("MAZO");

    private final JLabel carta1 = new JLabel();
    private final JLabel carta2 = new JLabel();
    private final JLabel carta3 = new JLabel();

    private JPanel panelCartas = new JPanel();

    private String cartaJ1;
    private String cartaJ2;

    public VistaGrafica(Controlador controlador){
        this.controlador = controlador;
        controlador.setVista(this);

        // Constantes
        int boardWidth = 1150;
        int boardHeight = 450;

        int textoWidth = 240;
        int textoHeight = 720;

        int cardWidth = 160;
        int cardHeight = 104;

        Color verde = new Color(53, 101, 77);

        // Ventana Principal
        ventanaPrincipal.setLayout(new BorderLayout());
        ventanaPrincipal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ventanaPrincipal.setPreferredSize(new Dimension(boardWidth, boardHeight));
        ventanaPrincipal.setResizable(false);
        ventanaPrincipal.setVisible(true);

        // Mesa
        mesa.setLayout(new BorderLayout());
        mesa.setBackground(verde);
        ventanaPrincipal.add(mesa, BorderLayout.CENTER);

        // Consola
        consola.setLayout(new BorderLayout());
        consola.setBackground(Color.BLACK);
        consola.setForeground(Color.WHITE);
        consola.setEditable(false);
        consola.setPreferredSize(new Dimension(textoWidth, textoHeight));
        SimpleAttributeSet centrado = new SimpleAttributeSet();
        StyleConstants.setAlignment(centrado, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(centrado, Color.WHITE);
        doc.setParagraphAttributes(0, doc.getLength(), centrado, false);

        // Agregar un CaretListener para autoscroll
        doc.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    consola.setCaretPosition(consola.getDocument().getLength());
                });
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // No es necesario hacer nada aquí
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // No es necesario hacer nada aquí
            }
        });

        ventanaPrincipal.add(new JScrollPane(consola), BorderLayout.EAST);

        // Opciones
        opciones.setBackground(verde);

        // Cartas Jugador
        cartasJugador.setLayout(new BoxLayout(cartasJugador, BoxLayout.X_AXIS));
        cartasJugador.setOpaque(true);
        cartasJugador.setBackground(verde);
        cartasJugador.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        //mesa.add(cartasJugador, BorderLayout.SOUTH);

        // Subpanel para opciones y cartas jugador
        subpanel.setLayout(new FlowLayout());
        subpanel.add(cartasJugador);
        subpanel.add(opciones);
        subpanel.setBackground(verde);
        mesa.add(subpanel, BorderLayout.SOUTH);

        // Botón TRUCO
        botonTruco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.esMiTurno() && !controlador.getTrucoCantado()) {
                    cantarTruco();
                }
            }
        });

        // Botón RETRUCO
        botonRetruco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.esMiTurno()) {
                    controlador.cantarTruco();
                }
            }
        });

        // Botón VALECUATRO
        botonValecuatro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.esMiTurno()) {
                    controlador.cantarTruco();
                }
            }
        });

        // Botón PRIMER ENVIDO
        botonPrimerEnvido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.esMiTurno()) {
                    cantarEnvido(Apuesta.ENVIDO);
                }
            }
        });

        // Botón ENVIDO
        botonEnvido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.esMiTurno()) {
                    cantarEnvido(Apuesta.ENVIDO_ENVIDO);
                }
            }
        });

        // Botón REAL ENVIDO
        botonRealEnvido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.esMiTurno()) {
                    cantarEnvido(Apuesta.REAL_ENVIDO);
                }
            }
        });

        // Botón FALTA ENVIDO
        botonFaltaEnvido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.esMiTurno()) {
                    cantarEnvido(Apuesta.FALTA_ENVIDO);
                }
            }
        });

        // Botón QUIERO
        botonQuiero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.quiero(apuestaActual);
                opciones.removeAll();
                //inicializarOpciones();
                //deshabilitarComponentes(opciones);
                mostrarOpcionesRonda();
            }
        });

        // Botón NO QUIERO
        botonNoQuiero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.noQuiero(apuestaActual);
                opciones.removeAll();
                //inicializarOpciones();
                //deshabilitarComponentes(opciones);
                mostrarOpcionesRonda();
            }
        });

        // Botón IRSE AL MAZO
        botonMazo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.esMiTurno()) {
                    irseAlMazo();
                }
            }
        });

        // Cartas jugadas

        paneles = new JLayeredPane[3];
        for (int i = 0; i < 3; i++){
            paneles[i] = new JLayeredPane();
            paneles[i].setPreferredSize(new Dimension((int) (cardWidth * 1.5), cardHeight * 2));
            paneles[i].setBounds(50*i,0, 0, 0);
            cartasJugadas.add(paneles[i]);
        }

        cartasJugadas.setPreferredSize(new Dimension(boardWidth - textoWidth, cardHeight * 2));
        cartasJugadas.setBackground(verde);

        mesa.add(cartasJugadas, BorderLayout.NORTH);

        // Mostrar Ventana Principal
        ventanaPrincipal.pack();
        mesa.repaint();
    }

    private Apuesta apuestaActual;

    @Override
    public int returnFrameXPos() {
        return 0;
    }

    @Override
    public int returnFrameYPos() {
        return 0;
    }

    @Override
    public void setLocation(int x, int y) {

    }

    public void setVisible(boolean flag){
        ventanaPrincipal.setVisible(flag);
    }

    public void inicializarOpciones() {
        opciones.removeAll();
        if (!controlador.getTrucoCantado()) {
            opciones.add(botonTruco);
        }
        //int nroRonda = controlador.getNumeroRonda();
        if (controlador.getNumeroRonda() == 0 && !controlador.getEnvidoCantado()) {
            opciones.add(botonPrimerEnvido);
            opciones.add(botonRealEnvido);
            opciones.add(botonFaltaEnvido);
        } else {
            opciones.remove(botonPrimerEnvido);
            opciones.remove(botonEnvido);
            opciones.remove(botonRealEnvido);
            opciones.remove(botonFaltaEnvido);
        }
        opciones.add(botonMazo);
        opciones.updateUI();
    }

    @Override
    public void mostrarMenuPrincipal() {
        mostrarPuntos();
        mostrarManoRonda();
        mostrarCartas();
        //inicializarOpciones();
        // TODO chequear esto
        mostrarOpcionesRonda();
        mostrarTurno(controlador.esMiTurno());
        apuestaActual = null;
    }

    // TODO chequear esto tambien
    @Override
    public void mostrarOpcionesRonda() {
        boolean trucoCantado = controlador.getTrucoCantado();
        boolean envidoCantado = controlador.getEnvidoCantado();
        int jugadorQuiero = controlador.getJugadorQuieroTruco();
        opciones.removeAll();
        if (!trucoCantado) {
            opciones.add(botonTruco);
        } else if (jugadorQuiero == controlador.getJugador()) {
            opciones.remove(botonMazo);
            switch (controlador.getTrucoActual()) {
                case TRUCO -> {
                    opciones.add(botonRetruco);
                }
                case RETRUCO -> {
                    opciones.add(botonValecuatro);
                }
            }
        }
        if (!envidoCantado && controlador.getNumeroRonda() == 0) {
            opciones.add(botonPrimerEnvido);
            opciones.add(botonRealEnvido);
            opciones.add(botonFaltaEnvido);
        }
        opciones.add(botonMazo);
        opciones.updateUI();
    }

    public void jugarCarta(int nroCarta){
        controlador.jugarCarta(nroCarta);
        mostrarCartas();
        //deshabilitarComponentes(opciones);
    }

    public void cantarTruco(){
        if (controlador.esMiTurno()) {
            controlador.cantarTruco();
            //deshabilitarComponentes(opciones);
            if (apuestaActual != null) {
                opciones.remove(0);
                switch (apuestaActual) {
                    case TRUCO -> {
                        opciones.remove(botonTruco);
                    }
                    case RETRUCO -> {
                        opciones.remove(botonRetruco);
                    }
                    case VALECUATRO -> {
                        opciones.remove(botonValecuatro);
                    }
                }
            }
        }
    }

    public void cantarEnvido(Apuesta envido){
        if (!controlador.getEnvidoCantado()) {
            controlador.cantarEnvido(envido);
        } else {
            controlador.redoblarEnvido(envido);
        }
    }

    public void irseAlMazo(){
        controlador.irseAlMazo();
    }

    @Override
    public void mostrarPuntos() {
        try {
            doc.insertString(doc.getLength(), "\n - PUNTOS - \n", null);
            doc.insertString(doc.getLength(), controlador.getEstadoPartida() + "\n", null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public void mostrarManoRonda(){
        try {
            doc.insertString(doc.getLength(), "\n- Mano " + controlador.getNumeroMano() + " - Ronda " + (controlador.getNumeroRonda() + 1) + "-\n", null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    private void eliminarMouseListeners(JLabel carta) {
        for (MouseListener listener : carta.getMouseListeners()) {
            carta.removeMouseListener(listener);
        }
    }

    @Override
    public void mostrarCartas() {
        String path;
        int nroCarta = 1;

        eliminarMouseListeners(carta1);
        eliminarMouseListeners(carta2);
        eliminarMouseListeners(carta3);

        cartasJugador.removeAll();
        cartasJugador.add(Box.createHorizontalGlue());

        for (String carta : controlador.getCartas()){
            path = "src/vistas/imagenes/cartas/" + carta + ".png";
            switch (nroCarta){

                case 1 -> {
                    carta1.setIcon(new ImageIcon(path));
                    carta1.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e){
                            if (controlador.esMiTurno()) {
                                jugarCarta(0);
                            }
                        }
                    });
                    cartasJugador.add(carta1);
                }

                case 2 -> {
                    carta2.setIcon(new ImageIcon(path));
                    carta2.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e){
                            if (controlador.esMiTurno()) {
                                jugarCarta(1);
                            }
                        }
                    });
                    cartasJugador.add(carta2);
                }

                case 3 -> {
                    carta3.setIcon(new ImageIcon(path));
                    carta3.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e){
                            if (controlador.esMiTurno()) {
                                jugarCarta(2);
                            }
                        }
                    });
                    cartasJugador.add(carta3);
                }
            }

            nroCarta++;
        }
        cartasJugador.add(Box.createHorizontalGlue());
        cartasJugador.updateUI();
        cartasJugador.repaint();
    }

    @Override
    public void mostrarCartaJugada(String jugadorActual, String cartaJugada) {

        String path = "src/vistas/imagenes/cartas/" + cartaJugada + ".png";

        try {
            doc.insertString(doc.getLength(), "\n" + jugadorActual + " jugó " + cartaJugada + "\n", null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }

        int nroRonda = controlador.getNumeroRonda();

        JLabel carta = new JLabel(new ImageIcon(path));

        if (controlador.esMiTurno()){
            carta.setBounds(35,0,carta.getIcon().getIconWidth(),carta.getIcon().getIconHeight());
            cartaJ1 = cartaJugada;
        } else {
            carta.setBounds(0,35,carta.getIcon().getIconWidth(),carta.getIcon().getIconHeight());
            cartaJ2 = cartaJugada;
        }

        paneles[nroRonda].add(carta, JLayeredPane.DEFAULT_LAYER);
        paneles[nroRonda].updateUI();
    }

    @Override
    public void mostrarResponderApuesta() {
        opciones.removeAll();
        opciones.add(botonQuiero);
        opciones.add(botonNoQuiero);
        switch (apuestaActual){
            case TRUCO -> opciones.add(botonRetruco);
            case RETRUCO -> opciones.add(botonValecuatro);
            case ENVIDO -> {
                opciones.add(botonEnvido);
                opciones.add(botonRealEnvido);
                opciones.add(botonFaltaEnvido);
            }
            case ENVIDO_ENVIDO -> opciones.add(botonRealEnvido);
            case ENVIDO_ENVIDO_REAL_ENVIDO, ENVIDO_REAL_ENVIDO, REAL_ENVIDO -> opciones.add(botonFaltaEnvido);
        }
        opciones.updateUI();
    }

    @Override
    public void mostrarEsperandoRespuesta(String jugadorActual) {
        try {
            doc.insertString(doc.getLength(), "\nEsperando respuesta...\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mostrarDijoQuiero(String jugadorActual) {
        try {
            doc.insertString(doc.getLength(), "\n" + jugadorActual + " dijo QUIERO\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mostrarDijoNoQuiero(String jugadorActual) {
        try {
            doc.insertString(doc.getLength(), "\n" + jugadorActual + " dijo NO QUIERO\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mostrarResultadoEnvido(String ganadorEnvido, String tantos) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(ventanaPrincipal, tantos + "\n" + ganadorEnvido + " gana.");
        });
    }

    @Override
    public void mostrarIrseAlMazo(String jugadorActual) {
        try {
            doc.insertString(doc.getLength(), "\n" + jugadorActual + " se fue al mazo.\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // TODO REFACTORIZAR ESTO
    @Override
    public void mostrarGanadorRonda(String ganadorRonda) {
        String cartaGanadora = controlador.getCartaGanadora();
        int nroRonda = controlador.getNumeroRonda();

        if (Objects.equals(cartaGanadora, cartaJ1)){
            Component carta = paneles[nroRonda].getComponentAt(35,0);
            paneles[nroRonda].moveToFront(carta);
        } else if (Objects.equals(cartaGanadora, cartaJ2)){
            Component carta = paneles[nroRonda].getComponentAt(0,35);
            paneles[nroRonda].moveToFront(carta);
        } else {
            // TODO parda
        }

        paneles[nroRonda].revalidate();
        paneles[nroRonda].repaint();

        try {
            if (ganadorRonda != null) {
                doc.insertString(doc.getLength(), "\n------------------------------\n" + ganadorRonda + " ganó la ronda.\n------------------------------\n", null);
            } else {
                doc.insertString(doc.getLength(), "\n------------------------------\nParda.\n------------------------------\n", null);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void mostrarDialogoConBoton(String mensaje) {
        // Crear el diálogo
        JDialog dialog = new JDialog();
        dialog.setModal(true); // Bloquea la interacción con otras ventanas
        dialog.setTitle("Fin de la mano");
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null); // Centrar la ventana

        // Crear un panel para contener los elementos
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(mensaje, SwingConstants.CENTER);
        JButton botonContinuar = new JButton("Continuar");

        // Acción al presionar el botón
        botonContinuar.addActionListener(e -> dialog.dispose());

        // Agregar los componentes al panel
        panel.add(label, BorderLayout.CENTER);
        panel.add(botonContinuar, BorderLayout.SOUTH);

        // Agregar el panel al diálogo
        dialog.add(panel);

        // Hacer visible el diálogo
        dialog.setVisible(true);
    }

    @Override
    public void mostrarGanadorMano(String ganadorMano) throws InterruptedException {
        String mensaje = ganadorMano + " ganó la mano.";
        try {
            doc.insertString(doc.getLength(), "\n------------------------------\n" + mensaje + "\n------------------------------\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            mostrarDialogoConBoton(mensaje);
        });

        SwingUtilities.invokeLater(() -> {
            for (JLayeredPane panel : paneles){
                panel.removeAll();
                panel.revalidate();
                panel.repaint();
            }
        });

    }

    @Override
    public void mostrarTurno(boolean esMiTurno) {
        if (esMiTurno){
            try {
                doc.insertString(doc.getLength(), "\n- ES TU TURNO -\n", null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        } else {
            try {
                doc.insertString(doc.getLength(), "\n- TURNO DE " + controlador.getNombreJugadorActual() + " -\n", null);
                doc.insertString(doc.getLength(), "\nEsperando respuesta...\n", null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void mostrarFinPartida(String jugadorGanador) {
        subpanel.remove(opciones);
        eliminarMouseListeners(carta1);
        eliminarMouseListeners(carta2);
        eliminarMouseListeners(carta3);
        String textoConsola = "\n------------------------------\n- FIN DE LA PARTIDA -\n\n" + jugadorGanador + " ganó.\n------------------------------";
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(ventanaPrincipal, textoConsola);
        });
        mostrarLeaderboard();
    }

    private void mostrarLeaderboard() {
        new Leaderboard("scoreboard.dat");
    }

    @Override
    public void mostrarApuesta(String jugadorActual, Apuesta apuesta) {
        apuestaActual = apuesta;
        try {
            doc.insertString(doc.getLength(), "\n" + jugadorActual + " canta " + apuestaActual.toString() + "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        switch (apuestaActual) {
            case TRUCO, RETRUCO, VALECUATRO -> {
                opciones.remove(botonTruco);
                opciones.remove(botonRetruco);
                opciones.remove(botonValecuatro);
                opciones.remove(botonQuiero);
                opciones.remove(botonNoQuiero);
            }
            default -> {
                opciones.remove(botonPrimerEnvido);
                opciones.remove(botonEnvido);
                opciones.remove(botonRealEnvido);
                opciones.remove(botonFaltaEnvido);
                opciones.remove(botonQuiero);
                opciones.remove(botonNoQuiero);
            }
        }
    }
}
package vistas;

import controlador.Controlador;
import modelos.Apuesta;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class VistaGrafica implements IVista {

    private Controlador controlador;

    private int boardWidth = 1280;
    private int boardHeight = 720;

    private int textoWidth = 240;
    private int textoHeight = 720;

    private int cardWidth = 160;
    private int cardHeight = 104;

    private Color verde = new Color(53, 101, 77);

    private JFrame ventanaPrincipal = new JFrame("Truco");
    private JPanel mesa = new JPanel() {
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
        }
    };
    private JTextPane consola = new JTextPane();
    private StyledDocument doc = consola.getStyledDocument();
    private JPanel opciones = new JPanel();
    private JPanel cartasJugador = new JPanel();
    private JPanel subpanel = new JPanel();

    private final JPanel cartasJugadas = new JPanel();
    private final JLayeredPane[] paneles;

    private JButton botonQuiero = new JButton("QUIERO");
    private JButton botonNoQuiero = new JButton("NO QUIERO");
    private JButton botonTruco = new JButton("TRUCO");
    private JButton botonRetruco = new JButton("RETRUCO");
    private JButton botonValecuatro = new JButton("VALECUATRO");
    private JButton botonPrimerEnvido = new JButton("ENVIDO");
    private JButton botonEnvido = new JButton("ENVIDO");
    private JButton botonRealEnvido = new JButton("REAL ENVIDO");
    private JButton botonFaltaEnvido = new JButton("FALTA ENVIDO");
    private JButton botonMazo = new JButton("MAZO");
    private JButton[] botones = new JButton[]{
        botonQuiero, botonNoQuiero, botonTruco, botonRetruco, botonValecuatro, botonPrimerEnvido, botonEnvido,
            botonRealEnvido, botonFaltaEnvido, botonMazo
    };

    private JLabel carta1 = new JLabel();
    private JLabel carta2 = new JLabel();
    private JLabel carta3 = new JLabel();

    private JPanel panelCartas = new JPanel();

    private String cartaJ1;
    private String cartaJ2;

    //private final JLabel carta1;
    //private final JLabel carta2;

    public VistaGrafica(){
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
        consola.setPreferredSize(new Dimension(textoWidth, textoHeight));
        consola.setEnabled(false);
        SimpleAttributeSet centrado = new SimpleAttributeSet();
        StyleConstants.setAlignment(centrado, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(centrado, Color.WHITE);
        doc.setParagraphAttributes(0, doc.getLength(), centrado, false);

        ventanaPrincipal.add(new JScrollPane(consola), BorderLayout.EAST);

        // Opciones
        inicializarOpciones();
        opciones.setBackground(verde);
        //ventanaPrincipal.add(opciones, BorderLayout.SOUTH);

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
                if (controlador.esMiTurno()) {
                    cantarTruco();
                }
            }
        });

        // Botón RETRUCO
        botonRetruco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.esMiTurno()) {
                    cantarTruco();
                }
            }
        });

        // Botón VALECUATRO
        botonValecuatro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.esMiTurno()) {
                    cantarTruco();
                }
            }
        });

        // Botón PRIMER ENVIDO
        botonPrimerEnvido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.esMiTurno() && !controlador.getEnvidoCantado() && controlador.getNumeroRonda() == 1){
                    opciones.removeAll();
                    opciones.add(botonEnvido);
                    opciones.add(botonRealEnvido);
                    opciones.add(botonFaltaEnvido);
                }
            }
        });

        // Botón ENVIDO
        botonEnvido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.esMiTurno()) {
                    cantarEnvido(Apuesta.ENVIDO);
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
                //deshabilitarComponentes(opciones);
            }
        });

        // Botón NO QUIERO
        botonNoQuiero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.noQuiero(apuestaActual);
                //deshabilitarComponentes(opciones);
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

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public void inicializarOpciones() {
        opciones.add(botonTruco);
        opciones.add(botonPrimerEnvido);
        opciones.add(botonRealEnvido);
        opciones.add(botonFaltaEnvido);
        opciones.add(botonMazo);
    }

    @Override
    public void mostrarMenuPrincipal() {
        mostrarCartas();
        inicializarOpciones();
        //deshabilitarComponentes(opciones);
        if (controlador.esMiTurno()) {
            mostrarTurno(true);
        } else {
            mostrarTurno(false);
        }
        apuestaActual = null;
    }

    @Override
    public void mostrarOpcionesRonda() {

    }

    /*
    public void restaurarOpciones(){
        opciones.removeAll();
        Apuesta trucoActual = controlador.getTrucoActual();
        if (trucoActual != null) {

            switch (trucoActual) {
                case TRUCO -> {
                    opciones.add(botonRetruco);
                    // Si el jugador no es quien cantó quiero truco, no puede cantar retruco
                    if (controlador.getJugadorQuieroTruco() != controlador.getJugador()){
                        botonRetruco.setEnabled(false);
                    }
                }
                case RETRUCO -> {
                    opciones.add(botonValecuatro);
                    // Si el jugador no es quien cantó quiero truco, no puede cantar valecuatro
                    if (controlador.getJugadorQuieroTruco() != controlador.getJugador()){
                        botonValecuatro.setEnabled(false);
                    }
                }
                // case VALECUATRO -> no se agrega botón
            }

        } else {
            opciones.add(botonTruco);
        }

        // Si ya se cantó envido o ya no es la primera ronda, no se puede cantar
        //if (controlador.getEnvidoCantado() || controlador.getNumeroRonda() != 1){
        //
        //} else {
        opciones.add(botonPrimerEnvido);
        //}

        opciones.add(botonMazo);
        opciones.repaint();
    }
     */


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
                        opciones.add(botonRetruco, 0);
                    }
                    case RETRUCO -> {
                        opciones.remove(botonRetruco);
                        opciones.add(botonValecuatro, 0);
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
                    //cartasJugador.add(Box.createRigidArea(new Dimension(20, 0)));
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
                    //cartasJugador.add(Box.createRigidArea(new Dimension(20, 0)));
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
                    //cartasJugador.add(Box.createRigidArea(new Dimension(20, 0)));
                }
            }

            nroCarta++;
        }
        cartasJugador.add(Box.createHorizontalGlue());
        cartasJugador.updateUI();
        cartasJugador.repaint();
    }


    //TODO arreglar que no se muestre la ultima carta jugada
    @Override
    public void mostrarCartaJugada(String jugadorActual, String cartaJugada) {

        String path = "src/vistas/imagenes/cartas/" + cartaJugada + ".png";

        int nroRonda = controlador.getNumeroRonda();

        JLabel carta = new JLabel(new ImageIcon(path));

        if (controlador.esMiTurno()){
            carta.setBounds(35,0,carta.getIcon().getIconWidth(),carta.getIcon().getIconHeight());
            cartaJ1 = cartaJugada;
        } else {
            carta.setBounds(0,35,carta.getIcon().getIconWidth(),carta.getIcon().getIconHeight());
            cartaJ2 = cartaJugada;
        }

        paneles[nroRonda-1].add(carta, JLayeredPane.DEFAULT_LAYER);
        paneles[nroRonda-1].updateUI();
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
            doc.insertString(doc.getLength(), "\nEsperando respuesta...", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mostrarDijoQuiero(String jugadorActual) {
        try {
            doc.insertString(doc.getLength(), "\n" + jugadorActual + " dijo QUIERO", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mostrarDijoNoQuiero(String jugadorActual) {
        try {
            doc.insertString(doc.getLength(), "\n" + jugadorActual + " dijo NO QUIERO", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mostrarResultadoEnvido(String ganadorEnvido, String tantos) {
        JOptionPane.showMessageDialog(ventanaPrincipal, tantos + "\n" + ganadorEnvido + " gana.");
    }

    @Override
    public void mostrarIrseAlMazo(String jugadorActual) {
        try {
            doc.insertString(doc.getLength(), "\n" + jugadorActual + " se fue al mazo.", null);
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
            Component carta = paneles[nroRonda-2].getComponentAt(35,0);
            paneles[nroRonda-2].moveToFront(carta);
        } else if (Objects.equals(cartaGanadora, cartaJ2)){
            Component carta = paneles[nroRonda-2].getComponentAt(0,35);
            paneles[nroRonda-2].moveToFront(carta);
        } else {
            // TODO parda
        }

        paneles[nroRonda-2].revalidate();
        paneles[nroRonda-2].repaint();

        try {
            doc.insertString(doc.getLength(), "\n " + ganadorRonda + " ganó la ronda.\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mostrarGanadorMano(String ganadorMano) {
        try {
            doc.insertString(doc.getLength(), "\n" + controlador.getNombreGanadorMano() + " ganó la mano.", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        for (JLayeredPane panel : paneles){
            panel.removeAll();
            panel.revalidate();
            panel.repaint();
        }
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
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void mostrarFinPartida(String jugadorGanador) {
        String textoConsola = "\n- FIN DE LA PARTIDA -\n" + jugadorGanador + " ganó.";
        JOptionPane.showMessageDialog(ventanaPrincipal, textoConsola);
    }

    @Override
    public void mostrarApuesta(String jugadorActual, Apuesta apuesta) {
        apuestaActual = apuesta;
        try {
            doc.insertString(doc.getLength(), "\n" + jugadorActual + " canta " + apuestaActual.toString(), null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
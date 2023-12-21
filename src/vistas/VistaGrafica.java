package vistas;

import controlador.Controlador;
import modelos.Apuesta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class VistaGrafica implements IVista {

    private Controlador controlador;

    private GridBagConstraints constraints;
    private final JFrame ventanaPrincipal;
    private final JLabel mesa;
    private final JPanel opciones;
    //private final JLabel respuestaOponente;
    private final JTextField mensaje;
    private final JPanel cartasJugadas;
    private final JLayeredPane[] paneles;

    //private final JLabel respuestaJugador;
    private final JPanel cartasJugador;
    private final JLabel puntos;

    private final JButton botonQuiero;
    private final JButton botonNoQuiero;
    private final JButton botonTruco;
    private final JButton botonRetruco;
    private final JButton botonValecuatro;
    private final JButton botonPrimerEnvido;
    private final JButton botonEnvido;
    private final JButton botonRealEnvido;
    private final JButton botonFaltaEnvido;
    private final JButton botonMazo;

    private JLabel carta1;
    private JLabel carta2;
    private JLabel carta3;

    private String cartaJ1;
    private String cartaJ2;

    //private final JLabel carta1;
    //private final JLabel carta2;

    public VistaGrafica(){
        // Ventana Principal
        ventanaPrincipal = new JFrame("Truco");
        ventanaPrincipal.setIconImage(new ImageIcon("src/vistas/imagenes/icono.png").getImage());
        ventanaPrincipal.setLayout(new GridBagLayout());
        ventanaPrincipal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //ventanaPrincipal.setPreferredSize(new Dimension(1200, 600));
        ventanaPrincipal.setResizable(false);

        // GridBagConstraints
        constraints = new GridBagConstraints();

        // Mesa
        mesa = new JLabel(new ImageIcon("src/vistas/imagenes/fondo.jpg"));
        mesa.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        ventanaPrincipal.add(mesa, constraints);

        // Opciones
        constraints = new GridBagConstraints();

        opciones = new JPanel();
        opciones.setLayout(new GridLayout());
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        ventanaPrincipal.add(opciones, constraints);

        // Botones

        // Botón TRUCO
        botonTruco = new JButton("TRUCO");

        botonTruco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cantarTruco();
            }
        });

        // Botón RETRUCO
        botonRetruco = new JButton("RETRUCO");

        botonRetruco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cantarTruco();
            }
        });

        // Botón VALECUATRO
        botonValecuatro = new JButton("VALECUATRO");

        botonValecuatro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cantarTruco();
            }
        });

        // Botón PRIMER ENVIDO
        botonPrimerEnvido = new JButton("ENVIDO");

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
        botonEnvido = new JButton("ENVIDO");

        botonEnvido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cantarEnvido(Apuesta.ENVIDO);
            }
        });

        // Botón REAL ENVIDO
        botonRealEnvido = new JButton("REAL ENVIDO");

        botonRealEnvido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cantarEnvido(Apuesta.REAL_ENVIDO);
            }
        });

        // Botón FALTA ENVIDO
        botonFaltaEnvido = new JButton("FALTA ENVIDO");

        botonFaltaEnvido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cantarEnvido(Apuesta.FALTA_ENVIDO);
            }
        });

        // Botón QUIERO
        botonQuiero = new JButton("QUIERO");

        botonQuiero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.quiero(apuestaActual);
                //deshabilitarComponentes(opciones);
            }
        });

        // Botón QUIERO
        botonNoQuiero = new JButton("NO QUIERO");

        botonNoQuiero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.noQuiero(apuestaActual);
                //deshabilitarComponentes(opciones);
            }
        });

        // Botón IRSE AL MAZO
        botonMazo = new JButton("IRSE AL MAZO");

        botonMazo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                irseAlMazo();
            }
        });

        // Agrego los botones al JPanel opciones
        opciones.add(botonTruco);
        opciones.add(botonPrimerEnvido);
        opciones.add(botonMazo);

        // Deshabilito los botones
        //deshabilitarComponentes(opciones);
        opciones.setVisible(true);

        // Respuesta Oponente
        constraints = new GridBagConstraints();

        mensaje = new JTextField();
        mensaje.setEnabled(false);
        mensaje.setHorizontalAlignment(JTextField.CENTER);
        mensaje.setPreferredSize(new Dimension(1200,30));
        mensaje.setBackground(Color.black);
        mensaje.setDisabledTextColor(Color.white);
        //respuestaOponente = new JLabel(new ImageIcon("src/vistas/imagenes/speechBubble.png"));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        //constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.NORTH;

        //mesa.add(respuestaOponente, constraints);
        mesa.add(mensaje,constraints);

        // Cartas jugadas
        cartasJugadas = new JPanel();
        cartasJugadas.setOpaque(false);
        //cartasJugadas.setLayout(new GridBagLayout());
        cartasJugadas.setBorder(BorderFactory.createLineBorder(Color.black));
        paneles = new JLayeredPane[3];
        for (int i = 0; i < 3; i++){
            paneles[i] = new JLayeredPane();
            paneles[i].setOpaque(false);
            paneles[i].setBorder(BorderFactory.createLineBorder(Color.black));
            paneles[i].setPreferredSize(new Dimension(350,400));
            paneles[i].setBounds(400*i,0,400,400);
            cartasJugadas.add(paneles[i]);
        }

        cartasJugadas.setPreferredSize(new Dimension(0, 300));
        cartasJugadas.setBounds(0,200,1200,400);


        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 2;
        constraints.gridwidth = 3;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        mesa.add(cartasJugadas, constraints);

        /*
        // Respuesta Jugador
        constraints = new GridBagConstraints();

        respuestaJugador = new JLabel(new ImageIcon("src/vistas/imagenes/speechBubble2.png"));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.SOUTH;
        mesa.add(respuestaJugador, constraints);
        */

        // Cartas Jugador
        constraints = new GridBagConstraints();

        cartasJugador = new JPanel();
        cartasJugador.setBorder(BorderFactory.createLineBorder(Color.black));
        cartasJugador.setLayout(new GridBagLayout());
        cartasJugador.setPreferredSize(new Dimension(0, 200));
        cartasJugador.setOpaque(false);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.SOUTH;
        mesa.add(cartasJugador, constraints);

        // Cartas
        carta1 = new JLabel();
        carta1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        carta2 = new JLabel();
        carta2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        carta3 = new JLabel();
        carta3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Puntos
        constraints = new GridBagConstraints();

        puntos = new JLabel(new ImageIcon("src/vistas/imagenes/puntos.png"));
        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        mesa.add(puntos, constraints);

        // Mostrar Ventana Principal
        ventanaPrincipal.pack();
        ventanaPrincipal.setVisible(false);
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

    public void habilitarComponentes(JPanel contenedor){
        for (Component componente : contenedor.getComponents()){
            componente.setEnabled(true);
        }
    }

    public void deshabilitarComponentes(JPanel contenedor){
        if (contenedor != null){
            for (Component componente : contenedor.getComponents()){
                componente.setEnabled(false);
            }
        }
    }

    @Override
    public void mostrarMenuPrincipal() {
        mostrarCartas();
        //deshabilitarComponentes(opciones);
        if (controlador.esMiTurno()) {
            mostrarTurno(true);
            mostrarOpcionesRonda();
            // TODO
            // acá es donde se muestra el speechBubble del jugador indicando que es su turno
        } else {
            mostrarTurno(false);
            // TODO
            // acá es donde se muestra el speechBubble del oponente indicando que es su turno
        }
        apuestaActual = null;
    }

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

    @Override
    public void mostrarOpcionesRonda() {
        habilitarComponentes(opciones);
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
                    case TRUCO -> opciones.add(botonRetruco, 0);
                    case RETRUCO -> opciones.add(botonValecuatro, 0);
                    //case VALECUATRO -> opciones.remove(0);
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

    @Override
    public void mostrarCartas() {
        String path;
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(0,10,0,10);
        int nroCarta = 1;

        cartasJugador.removeAll();

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
                    cartasJugador.add(carta1, constraints);
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
                    cartasJugador.add(carta2, constraints);
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
                    cartasJugador.add(carta3, constraints);
                }
            }
            nroCarta++;
        }
        cartasJugador.updateUI();
    }

    @Override
    public void mostrarCartaJugada(String jugadorActual, String cartaJugada) {

        String path = "src/vistas/imagenes/cartas/" + cartaJugada + ".png";

        int nroRonda = controlador.getNumeroRonda();

        JLabel carta = new JLabel(new ImageIcon(path));

        if (controlador.esMiTurno()){
            carta.setBounds(150,100,carta.getIcon().getIconWidth(),carta.getIcon().getIconHeight());
            cartaJ1 = cartaJugada;
        } else {
            carta.setBounds(100,50,carta.getIcon().getIconWidth(),carta.getIcon().getIconHeight());
            cartaJ2 = cartaJugada;
        }

        paneles[nroRonda-1].add(carta, JLayeredPane.DEFAULT_LAYER);

        paneles[nroRonda-1].updateUI();
    }

    @Override
    public void mostrarResponderApuesta() {
        deshabilitarComponentes(cartasJugador);
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
        habilitarComponentes(opciones);/////////
        opciones.updateUI();
    }

    @Override
    public void mostrarEsperandoRespuesta(String jugadorActual) {

    }

    @Override
    public void mostrarDijoQuiero(String jugadorActual) {
        mensaje.setText(jugadorActual + " dijo QUIERO");
    }

    @Override
    public void mostrarDijoNoQuiero(String jugadorActual) {
        mensaje.setText(jugadorActual + " dijo NO QUIERO");
    }

    @Override
    public void mostrarResultadoEnvido(String ganadorEnvido, String tantos) {
        JOptionPane.showMessageDialog(ventanaPrincipal, tantos + "\n" + ganadorEnvido + " gana.");
    }

    @Override
    public void mostrarIrseAlMazo(String jugadorActual) {
        mensaje.setText(jugadorActual + " se fue al mazo.");
    }

    @Override
    public void mostrarGanadorRonda(String ganadorRonda) {
        String cartaGanadora = controlador.getCartaGanadora();
        int nroRonda = controlador.getNumeroRonda();

        if (Objects.equals(cartaGanadora, cartaJ1)){
            Component carta = paneles[nroRonda-2].getComponentAt(150,100);
            paneles[nroRonda-2].moveToFront(carta);
        } else if (Objects.equals(cartaGanadora, cartaJ2)){
            Component carta = paneles[nroRonda-2].getComponentAt(100,50);
            paneles[nroRonda-2].moveToFront(carta);
        } else {
            // TODO parda
        }

        paneles[nroRonda-2].revalidate();
        paneles[nroRonda-2].repaint();
    }

    @Override
    public void mostrarGanadorMano(String ganadorMano) {
        for (JLayeredPane panel : paneles){
            panel.removeAll();
            panel.revalidate();
            panel.repaint();
        }
        cartasJugador.removeAll();
        restaurarOpciones();
    }

    @Override
    public void mostrarTurno(boolean esMiTurno) {
        if (esMiTurno){
            mensaje.setText("--- ES TU TURNO ---");
        } else {
            mensaje.setText("--- TURNO DE " + controlador.getNombreJugadorActual() + " ---");
        }
    }

    @Override
    public void mostrarFinPartida(String jugadorGanador) {
        String mensaje = "--- FIN DE LA PARTIDA ---\n" + jugadorGanador + " ganó.";
        JOptionPane.showMessageDialog(ventanaPrincipal, mensaje);
    }

    @Override
    public void mostrarApuesta(String jugadorActual, Apuesta apuesta) {
        apuestaActual = apuesta;
        // TODO mostrar lo que se cantó
        mensaje.setText(jugadorActual + " canta " + apuestaActual.toString());
    }
}
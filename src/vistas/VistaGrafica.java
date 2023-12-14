package vistas;

import controlador.Controlador;
import modelos.Apuesta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VistaGrafica implements IVista {

    private Controlador controlador;

    private GridBagConstraints constraints;
    private final JFrame ventanaPrincipal;
    private final JLabel mesa;
    private final JPanel opciones;
    private final JLabel respuestaOponente;
    private final JPanel cartasJugadas;
    private final JLabel respuestaJugador;
    private final JPanel cartasJugador;
    private final JLabel puntos;
    private final JButton botonTruco;
    private final JButton botonEnvido;
    private final JButton botonMazo;
    private JLabel carta1;
    private JLabel carta2;
    private JLabel carta3;
    private JLabel cartaJugada1;
    private JLabel cartaJugada2;

    //private final JLabel carta1;
    //private final JLabel carta2;

    public VistaGrafica(){
        // Ventana Principal
        ventanaPrincipal = new JFrame("Truco");
        ventanaPrincipal.setIconImage(new ImageIcon("src/vistas/imagenes/icono.png").getImage());
        ventanaPrincipal.setLayout(new GridBagLayout());
        ventanaPrincipal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //ventanaPrincipal.setSize(1200, 600);
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

        // Botón ENVIDO
        botonEnvido = new JButton("ENVIDO");

        botonEnvido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cantarEnvido();
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
        opciones.add(botonEnvido);
        opciones.add(botonMazo);

        // Deshabilito los botones
        deshabilitarBotones(opciones);
        opciones.setVisible(false);

        // Respuesta Oponente
        constraints = new GridBagConstraints();

        respuestaOponente = new JLabel(new ImageIcon("src/vistas/imagenes/speechBubble.png"));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weighty = 0.0;
        constraints.insets = new Insets(0,0,0,50);
        constraints.anchor = GridBagConstraints.CENTER;
        mesa.add(respuestaOponente, constraints);

        // Cartas Jugadas
        constraints = new GridBagConstraints();

        cartasJugadas = new JPanel();
        cartasJugadas.setBorder(BorderFactory.createLineBorder(Color.black));
        cartasJugadas.setLayout(new GridBagLayout());
        cartasJugadas.setPreferredSize(new Dimension(0, 300));
        cartasJugadas.setOpaque(false);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 2;
        constraints.gridwidth = 3;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        mesa.add(cartasJugadas, constraints);

        // Respuesta Jugador
        constraints = new GridBagConstraints();

        respuestaJugador = new JLabel(new ImageIcon("src/vistas/imagenes/speechBubble2.png"));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.SOUTH;
        mesa.add(respuestaJugador, constraints);

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
        carta2 = new JLabel();
        carta3 = new JLabel();
        cartaJugada1 = new JLabel();
        cartaJugada2 = new JLabel();

        // Puntos
        constraints = new GridBagConstraints();

        puntos = new JLabel(new ImageIcon("src/vistas/imagenes/puntos.png"));
        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        mesa.add(puntos, constraints);

        // Mostrar Ventana Principal
        ventanaPrincipal.pack();
        ventanaPrincipal.setVisible(true);

    }

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

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public void habilitarBotones(JPanel panelBotones){
        Component[] botones = panelBotones.getComponents();
        for (Component boton : botones){
            boton.setEnabled(true);
        }
    }

    public void deshabilitarBotones(JPanel panelBotones){
        Component[] botones = panelBotones.getComponents();
        for (Component boton : botones){
            boton.setEnabled(false);
        }
    }

    @Override
    public void mostrarMenuPrincipal() {
        mostrarCartas();
        if (controlador.esMiTurno()) {
            mostrarOpcionesRonda();
            // TODO
            // acá es donde se muestra el speechBubble del jugador indicando que es su turno
        } else {
            // TODO
            // acá es donde se muestra el speechBubble del oponente indicando que es su turno
        }
    }

    @Override
    public void mostrarOpcionesRonda() {
        habilitarBotones(opciones);
    }

    public void jugarCarta(int nroCarta){
        // TODO
        if (controlador.esMiTurno()) {
            controlador.jugarCarta(nroCarta);
            mostrarCartas();
        }
    }

    public void cantarTruco(){

    }

    public void cantarEnvido(){

    }

    public void irseAlMazo(){

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
                            jugarCarta(0);
                        }
                    });
                    cartasJugador.add(carta1, constraints);
                }

                case 2 -> {
                    carta2.setIcon(new ImageIcon(path));
                    carta2.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e){
                            jugarCarta(1);
                        }
                    });
                    cartasJugador.add(carta2, constraints);
                }

                case 3 -> {
                    carta3.setIcon(new ImageIcon(path));
                    carta3.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e){
                            jugarCarta(2);
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
        constraints = new GridBagConstraints();
        if (cartaJugada1 != null){
            cartaJugada1 = new JLabel (new ImageIcon(path));
            cartasJugadas.add(cartaJugada1);
        } else {
            cartaJugada2 = new JLabel (new ImageIcon(path));
            constraints.insets = new Insets(0,0,-10,-10);
            cartasJugadas.add(cartaJugada2);
        }
    }

    @Override
    public void mostrarResponderApuesta() {

    }

    @Override
    public void mostrarEsperandoRespuesta(String jugadorActual) {

    }

    @Override
    public void mostrarDijoQuiero(String jugadorActual) {

    }

    @Override
    public void mostrarDijoNoQuiero(String jugadorActual) {

    }

    @Override
    public void mostrarResultadoEnvido(String ganadorEnvido, String tantos) {

    }

    @Override
    public void mostrarIrseAlMazo(String jugadorActual) {

    }

    @Override
    public void mostrarGanadorRonda(String ganadorRonda) {
        String path = "src/vistas/imagenes/cartas";
        String cartaGanadora = controlador.getCartaGanadora();
        ImageIcon icon = new ImageIcon(path + cartaGanadora + "png");
        if (cartaJugada1.getIcon().equals(icon)){
            cartasJugadas.removeAll();
            cartasJugadas.add(cartaJugada2);
            constraints = new GridBagConstraints();
            constraints.insets = new Insets(0,0,-10,-10);
            cartasJugadas.add(cartaJugada1, constraints);
        }
    }

    @Override
    public void mostrarGanadorMano(String ganadorMano) {

    }

    @Override
    public void mostrarTurno(boolean esMiTurno) {

    }

    @Override
    public void mostrarFinPartida(String jugadorGanador) {

    }

    @Override
    public void mostrarApuesta(String jugadorActual, Apuesta apuestaActual) {

    }
}

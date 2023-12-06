package vistas;

import controlador.Controlador;
import modelos.Apuesta;

import javax.swing.*;
import java.awt.*;

public class VistaGrafica implements IVista {

    private final GridBagConstraints constraints;
    private final JFrame ventanaPrincipal;
    private final JLabel fondo;
    private final JPanel opciones;
    private final JButton botonTruco;
    private final JButton botonEnvido;
    private final JButton botonMazo;

    private final JLabel carta1;
    private final JLabel carta2;

    public VistaGrafica(){
        constraints = new GridBagConstraints();
        ventanaPrincipal = new JFrame("Truco");
        ventanaPrincipal.setIconImage(new ImageIcon("src/vistas/imagenes/icono.png").getImage());
        ventanaPrincipal.getContentPane().setLayout(new GridBagLayout());
        ventanaPrincipal.setSize(1200, 600);

        fondo = new JLabel(new ImageIcon("src/vistas/imagenes/fondo.jpg"));
        fondo.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.ABOVE_BASELINE;
        ventanaPrincipal.getContentPane().add(fondo, constraints);

        constraints.weightx = 0.0;
        constraints.weighty = 0.0;

        opciones = new JPanel();
        opciones.setLayout(new GridLayout());

        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        ventanaPrincipal.getContentPane().add(opciones, constraints);

        constraints.weightx = 0.0;
        constraints.weighty = 0.0;

        botonTruco = new JButton("CANTAR TRUCO");
        botonEnvido = new JButton("CANTAR ENVIDO");
        botonMazo = new JButton("IRSE AL MAZO");

        opciones.add(botonTruco);
        opciones.add(botonEnvido);
        opciones.add(botonMazo);

        constraints.weightx = 0.0;
        constraints.weighty = 0.0;

        carta1 = new JLabel(new ImageIcon("src/vistas/imagenes/cartas/1 de ESPADA.png"));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(0, 0, 0, 0);

        fondo.add(carta1, constraints);

        carta2 = new JLabel(new ImageIcon("src/vistas/imagenes/cartas/1 de BASTO.png"));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(100, 100, 0, 0);

        fondo.add(carta2, constraints);

        ventanaPrincipal.setResizable(false);
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

    }

    @Override
    public void mostrarMenuPrincipal() {

    }

    @Override
    public void mostrarOpcionesRonda() {

    }

    @Override
    public void mostrarPuntos() {

    }

    @Override
    public void mostrarCartas() {

    }

    @Override
    public void mostrarCartaJugada(String jugadorActual, String cartaJugada) {

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

package modelos;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import vistas.Observador;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ModeloTrucoI extends IObservableRemoto {
    void setObservers(IControladorRemoto observer) throws RemoteException;

    void notificarObservadoresObservadores(Object arg) throws RemoteException;

    void notificarObservadoresJugarCarta() throws RemoteException;

    void notificarObservadoresApuesta(Apuesta apuesta) throws RemoteException;

    int getNumeroMano() throws RemoteException;

    int getNumeroRonda() throws RemoteException;

    int getJugadorActual() throws RemoteException;

    String getNombreJugadorActual() throws RemoteException;

    ArrayList<String> getCartas(int jugador) throws RemoteException;

    int getGanadorRonda() throws RemoteException;

    String getNombreGanadorRonda() throws RemoteException;

    int getGanadorMano() throws RemoteException;

    String getNombreGanadorMano() throws RemoteException;

    int getJugadorQuieroTruco() throws RemoteException;

    int getGanadorEnvido() throws RemoteException;

    String getNombreGanadorEnvido() throws RemoteException;

    String getCartaJugada(int jugador) throws RemoteException;

    String getCartaGanadora() throws RemoteException;

    String getEstadoPartida() throws RemoteException;

    boolean getTrucoCantado() throws RemoteException;

    Apuesta getTrucoActual() throws RemoteException;

    boolean getEnvidoCantado() throws RemoteException;

    String getTantos() throws RemoteException;

    void ingresarJugador(Jugador jugador) throws RemoteException;

    void iniciarJuego() throws RemoteException;

    void iniciarMano() throws RemoteException;

    void cambiarTurno() throws RemoteException;

    void cambiarJugadorMano() throws RemoteException;

    void jugarCarta(int numeroCarta) throws RemoteException;

    boolean finPartida() throws RemoteException;

    int determinarGanadorRonda() throws RemoteException;

    int determinarGanadorMano() throws RemoteException;

    void irseAlMazo() throws RemoteException;

    void guardarCartasJugadasAlMazo() throws RemoteException;

    void guardarCartasNoJugadasAlMazo() throws RemoteException;

    void limpiarRonda() throws RemoteException;

    void limpiarMano() throws RemoteException;

    void cantarTruco() throws RemoteException;

    // Para el envido inicial
    void cantarEnvido(Apuesta apuesta) throws RemoteException;

    void cantarEnvidoTruco(Apuesta apuesta) throws RemoteException;

    int calcularEnvido() throws RemoteException;

    void quiero(Apuesta apuesta) throws RemoteException;

    void noQuiero(Apuesta apuesta) throws RemoteException;

    // Para el primer envido
    void redoblarEnvido(Apuesta apuesta) throws RemoteException;

    void redoblarApuesta(Apuesta apuesta) throws RemoteException;

    void darPuntos(int jugador, int puntos) throws RemoteException;
}

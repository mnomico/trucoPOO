package modelos;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IJuego extends IObservableRemoto {
    void setObservers(IControladorRemoto observer) throws RemoteException;

    void notificarObservadores(Object arg) throws RemoteException;

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

    public void ingresarJugador(String nombreJugador) throws RemoteException;

    int obtenerJugador() throws RemoteException;

    void iniciarJuego() throws RemoteException;

    void iniciarMano() throws RemoteException;

    int cambiarTurno() throws RemoteException;

    int cambiarJugadorMano() throws RemoteException;

    void jugarCarta(int numeroCarta) throws IOException, InterruptedException;

    boolean finPartida() throws RemoteException;

    void irseAlMazo() throws RemoteException, IOException;

    void guardarCartasJugadasAlMazo() throws RemoteException;

    void guardarCartasNoJugadasAlMazo() throws RemoteException;

    void limpiarRonda() throws RemoteException;

    void limpiarMano() throws RemoteException;

    void cantarTruco() throws RemoteException;

    // Para el envido inicial
    void cantarEnvido(Apuesta apuesta) throws RemoteException;

    void cantarEnvidoTruco(Apuesta apuesta) throws RemoteException;

    void quiero(Apuesta apuesta) throws IOException;

    void noQuiero(Apuesta apuesta) throws IOException;

    // Para el primer envido
    void redoblarEnvido(Apuesta apuesta) throws RemoteException;

    void redoblarApuesta(Apuesta apuesta) throws RemoteException;

    void darPuntos(int jugador, int puntos) throws RemoteException;
}

package controlador;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import modelos.*;
import vistas.IVista;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Controlador implements IControladorRemoto {
    private ModeloTrucoI modelo;
    private IVista vista;
    //private final Jugador jugador;
    private int jugador;
    private String nombreJugador;

    public <T extends IObservableRemoto> Controlador(T modelo) {
        try {
            this.setModeloRemoto(modelo);
            // TODO esto es lo que falta para que funcione con RMI, supuestamente
            this.modelo.setObservers(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Controlador() {

    }

    public void setAsObserver() throws RemoteException {
        this.modelo.setObservers(this);
    }

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public void ingresarJugador(String nombre) throws RemoteException {
        int nroJugador = this.modelo.obtenerJugador();
        if (nroJugador != 0) {
            this.jugador = nroJugador;
            this.nombreJugador = nombre;
            modelo.ingresarJugador(nombreJugador);
        }
    }

    public int getJugador(){
        return jugador;
    }

    public String getEstadoPartida() {
        try {
            return modelo.getEstadoPartida();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getTantos() {
        try {
            return modelo.getTantos();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public int getNumeroMano() {
        try {
            return modelo.getNumeroMano();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return 0;
    }

    public int getNumeroRonda() {
        try {
            return modelo.getNumeroRonda();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return 0;
    }

    public int getJugadorActual() {
        try {
            return modelo.getJugadorActual();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return 0;
    }

    public String getNombreJugadorActual() {
        try {
            return modelo.getNombreJugadorActual();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public int getGanadorRonda() {
        try {
            return modelo.getGanadorRonda();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return 0;
    }

    public String getNombreGanadorRonda() {
        try {
            return modelo.getNombreGanadorRonda();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    /*
    public int getGanadorMano() {
        return modelo.getGanadorMano();
    }
    */

    public String getNombreGanadorMano() {
        try {
            return modelo.getNombreGanadorMano();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public int getJugadorQuieroTruco() {
        try {
            return modelo.getJugadorQuieroTruco();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return 0;
    }

    /*
    public int getGanadorEnvido() throws RemoteException {
        return modelo.getGanadorEnvido();
    }
    */

    public String getNombreGanadorEnvido() {
        try {
            return modelo.getNombreGanadorEnvido();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getCartas() {
        try {
            return modelo.getCartas(jugador);
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getCartaJugada(int jugador) {
        try {
            return modelo.getCartaJugada(jugador);
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getCartaGanadora()  {
        try {
            return modelo.getCartaGanadora();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean getTrucoCantado()  {
        try {
            return modelo.getTrucoCantado();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return false;
    }

    public Apuesta getTrucoActual() {
        try {
            return modelo.getTrucoActual();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean getEnvidoCantado() {
        try {
            return modelo.getEnvidoCantado();
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean esMiTurno()  {
        try {
            return modelo.getJugadorActual() == jugador;
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return false;
    }

    public void jugarCarta(int numeroCarta)  {
        try {
            modelo.jugarCarta(numeroCarta);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void cantarTruco()  {
        try {
            modelo.cantarTruco();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cantarEnvido(Apuesta apuesta)  {
        try {
            modelo.cantarEnvido(apuesta);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cantarEnvidoTruco(Apuesta apuesta)  {
        try {
            modelo.cantarEnvidoTruco(apuesta);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void irseAlMazo()  {
        try {
            modelo.irseAlMazo();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void quiero(Apuesta apuesta)  {
        try {
            modelo.quiero(apuesta);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void noQuiero(Apuesta apuesta)  {
        try {
            modelo.noQuiero(apuesta);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void redoblarEnvido(Apuesta apuesta)  {
        try {
            modelo.redoblarEnvido(apuesta);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void redoblarApuesta(Apuesta apuesta)  {
        try {
            modelo.redoblarApuesta(apuesta);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(IObservableRemoto o, Object arg) throws RemoteException{
        if (arg instanceof Evento){
            switch ((Evento) arg){

                case MOSTRAR_MENU -> vista.mostrarMenuPrincipal();

                case JUGAR_CARTA -> {
                    String nombreJugadorActual = getNombreJugadorActual();
                    String cartaJugada = getCartaJugada(getJugadorActual());
                    vista.mostrarCartaJugada(nombreJugadorActual, cartaJugada);
                }

                case RESPONDER_APUESTA -> {
                    if (esMiTurno()) {
                        vista.mostrarResponderApuesta();
                    } else {
                        String nombreJugadorActual = getNombreJugadorActual();
                        vista.mostrarEsperandoRespuesta(nombreJugadorActual);
                    }
                }

                case DIJO_QUIERO -> {
                    String nombreJugadorActual = getNombreJugadorActual();
                    vista.mostrarDijoQuiero(nombreJugadorActual);
                }

                case DIJO_NO_QUIERO -> {
                    String nombreJugadorActual = getNombreJugadorActual();
                    vista.mostrarDijoNoQuiero(nombreJugadorActual);
                }

                case RESULTADO_ENVIDO -> {
                    String ganadorEnvido = getNombreGanadorEnvido();
                    String tantos = getTantos();
                    vista.mostrarResultadoEnvido(ganadorEnvido, tantos);
                }

                case IRSE_AL_MAZO -> {
                    String nombreJugadorActual = getNombreJugadorActual();
                    vista.mostrarIrseAlMazo(nombreJugadorActual);
                }

                case FIN_RONDA -> {
                    if (getGanadorRonda() == 0){
                        vista.mostrarGanadorRonda(null);
                    } else {
                        String ganadorRonda = getNombreGanadorRonda();
                        vista.mostrarGanadorRonda(ganadorRonda);
                    }
                }

                case FIN_MANO -> {
                    String ganadorMano = getNombreGanadorMano();
                    vista.mostrarGanadorMano(ganadorMano);
                }

                case CAMBIO_TURNO -> vista.mostrarTurno(esMiTurno());

                case FIN_PARTIDA -> {
                    String jugadorGanador = getNombreJugadorActual();
                    vista.mostrarFinPartida(jugadorGanador);
                }

            }

        } else if (arg instanceof Apuesta apuestaActual){
            // Muestra la apuesta y luego muestra para responder
            String jugadorActual = getNombreJugadorActual();
            vista.mostrarApuesta(jugadorActual, apuestaActual);
        }
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException{
        this.modelo = (ModeloTrucoI) modeloRemoto;
    }
}
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.*;

import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;
import controlador.Controlador;
import vistas.*;

public class AppCliente {

	private static int jugador = 1;

	public static void main(String[] args) throws RemoteException {
		ArrayList<String> ips = Util.getIpDisponibles();
		String ip = (String) JOptionPane.showInputDialog(
				null,
				"Seleccione la IP en la que escuchará peticiones el cliente", "IP del cliente",
				JOptionPane.QUESTION_MESSAGE,
				null,
				ips.toArray(),
				null
		);
		String port = (String) JOptionPane.showInputDialog(
				null,
				"Seleccione el puerto en el que escuchará peticiones el cliente", "Puerto del cliente",
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				9999
		);
		String ipServidor = (String) JOptionPane.showInputDialog(
				null,
				"Seleccione la IP en la corre el servidor", "IP del servidor",
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				"127.0.0.1"
		);
		String portServidor = (String) JOptionPane.showInputDialog(
				null,
				"Seleccione el puerto en el que corre el servidor", "Puerto del servidor",
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				8888
		);
		String nombreJugador = (String) JOptionPane.showInputDialog(
				null,
				"Ingrese un nombre de usuario", "Nombre de usuario",
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				""
		);
		String[] opciones = {"Vista gráfica", "Vista consola"};
		int tipoVista = JOptionPane.showOptionDialog(
				null,
				"Elija el tipo de vista que desea usar",
				"Tipo de vista",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				opciones,
				opciones[0]
		);

		Controlador controlador = new Controlador();

		IVista vista;
		if (tipoVista == 0){
			vista = new VistaGrafica(controlador);
		} else {
			vista = new ConsolaGrafica(controlador);
		}
		controlador.setVista(vista);

		Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
		try {
			c.iniciar(controlador);
		} catch (RemoteException | RMIMVCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		controlador.setAsObserver();
		controlador.ingresarJugador(nombreJugador);
		//vista.mostrarMenuPrincipal();
		//vista.setVisible(true);
	}

}
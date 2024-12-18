import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;
import modelos.Juego;

public class AppServidor extends JFrame {

	private JTextField ipField;
	private JTextField portField;

	public AppServidor() {
		setTitle("Servidor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);
		setLayout(new BorderLayout());

		// Panel de entrada de datos
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(2, 2, 5, 5));

		// IP del servidor
		inputPanel.add(new JLabel("IP del servidor:"));
		ArrayList<String> ips = Util.getIpDisponibles();
		JComboBox<String> ipComboBox = new JComboBox<>(ips.toArray(new String[0]));
		ipField = new JTextField(ips.get(0));
		ipComboBox.addActionListener(e -> ipField.setText((String) ipComboBox.getSelectedItem()));
		inputPanel.add(ipComboBox);

		// Puerto del servidor
		inputPanel.add(new JLabel("Puerto del servidor:"));
		portField = new JTextField("8888");
		inputPanel.add(portField);

		add(inputPanel, BorderLayout.CENTER);

		// Botón para iniciar
		JButton iniciarButton = new JButton("Iniciar");
		iniciarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniciarServidor();
			}
		});
		add(iniciarButton, BorderLayout.SOUTH);
	}

	private void iniciarServidor() {
		try {
			String ip = ipField.getText();
			int port = Integer.parseInt(portField.getText());

			Juego modelo = new Juego();
			Servidor servidor = new Servidor(ip, port);
			servidor.iniciar(modelo);

			JOptionPane.showMessageDialog(this, "Servidor iniciado.");
			dispose();
		} catch (RemoteException | RMIMVCException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al iniciar el servidor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "El puerto debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			AppServidor frame = new AppServidor();
			frame.setVisible(true);
		});
	}
}

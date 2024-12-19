import java.util.ArrayList;
import javax.swing.*;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;
import controlador.Controlador;
import vistas.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AppCliente extends JFrame {
	private JTextField ipField;
	private JTextField portField;
	private JTextField ipServidorField;
	private JTextField portServidorField;
	private JTextField nombreJugadorField;
	private JComboBox<String> tipoVistaComboBox;

	public AppCliente() {
		setTitle("Configuración del Cliente");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 350);
		setLayout(new BorderLayout());

		// Panel de entrada de datos
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(6, 2, 5, 5));

		// IP del cliente
		inputPanel.add(new JLabel("IP del cliente:"));
		ArrayList<String> ips = Util.getIpDisponibles();
		JComboBox<String> ipComboBox = new JComboBox<>(ips.toArray(new String[0]));
		ipField = new JTextField(ips.get(0));
		ipComboBox.addActionListener(e -> ipField.setText((String) ipComboBox.getSelectedItem()));
		inputPanel.add(ipComboBox);

		// Puerto del cliente
		inputPanel.add(new JLabel("Puerto del cliente:"));
		portField = new JTextField("9999");
		inputPanel.add(portField);

		// IP del servidor
		inputPanel.add(new JLabel("IP del servidor:"));
		ipServidorField = new JTextField("127.0.0.1");
		inputPanel.add(ipServidorField);

		// Puerto del servidor
		inputPanel.add(new JLabel("Puerto del servidor:"));
		portServidorField = new JTextField("8888");
		inputPanel.add(portServidorField);

		// Nombre del jugador
		inputPanel.add(new JLabel("Nombre del jugador:"));
		nombreJugadorField = new JTextField("");
		inputPanel.add(nombreJugadorField);

		// Tipo de vista
		inputPanel.add(new JLabel("Tipo de vista:"));
		String[] opciones = {"Vista gráfica", "Vista consola"};
		tipoVistaComboBox = new JComboBox<>(opciones);
		inputPanel.add(tipoVistaComboBox);

		add(inputPanel, BorderLayout.CENTER);

		// Panel de botones
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		// Botón para iniciar
		JButton iniciarButton = new JButton("Iniciar");
		iniciarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniciarCliente();
				dispose();
			}
		});
		buttonPanel.add(iniciarButton);

		// Botón para ver el leaderboard
		JButton leaderboardButton = new JButton("Ver Leaderboard");
		leaderboardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarLeaderboard();
			}
		});
		buttonPanel.add(leaderboardButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void iniciarCliente() {
		try {
			String ip = ipField.getText();
			int port = Integer.parseInt(portField.getText());
			String ipServidor = ipServidorField.getText();
			int portServidor = Integer.parseInt(portServidorField.getText());
			String nombreJugador = nombreJugadorField.getText();
			int tipoVista = tipoVistaComboBox.getSelectedIndex();

			Controlador controlador = new Controlador();
			IVista vista;
			if (tipoVista == 0) {
				vista = new VistaGrafica(controlador);
			} else {
				vista = new ConsolaGrafica(controlador);
			}
			controlador.setVista(vista);

			Cliente cliente = new Cliente(ip, port, ipServidor, portServidor);
			cliente.iniciar(controlador);

			controlador.setAsObserver();
			controlador.ingresarJugador(nombreJugador);

			JOptionPane.showMessageDialog(this, "Cliente iniciado correctamente. Espere a que se conecten todos los jugadores.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void mostrarLeaderboard() {
        File file = new File("scoreboard.dat");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Todavía no se han jugado partidas.", "Leaderboard", JOptionPane.WARNING_MESSAGE);
            return;
        }
		new Leaderboard("scoreboard.dat");
    }

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			AppCliente frame = new AppCliente();
			frame.setVisible(true);
		});
	}
}
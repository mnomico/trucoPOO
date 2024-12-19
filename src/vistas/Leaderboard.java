package vistas;

import services.Serializador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Leaderboard extends JFrame {

    public Leaderboard(String filePath) {
        setTitle("Leaderboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Leer datos del archivo
        JTable table = getJTable(filePath);
        JScrollPane scrollPane = new JScrollPane(table);

        // Configurar el diseño de la ventana
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // Hacer visible la ventana
        setVisible(true);
    }

    private static JTable getJTable(String filePath) {
        Serializador scoreboard = new Serializador(filePath);
        Object[] leaderboardData = scoreboard.readObjects();

        // Crear la tabla
        String[] columnNames = {"Jugador", "Partidas ganadas"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Rellenar la tabla con los datos del archivo
        for (Object o : leaderboardData) {
            Object[] row = (Object[]) o;
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        table.setEnabled(false);
        return table;
    }
}

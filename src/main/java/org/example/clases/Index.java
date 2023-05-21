package org.example.clases;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class Index extends JFrame implements ActionListener {
    private JLabel usernameLabel, passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private static String dbUrl = "jdbc:mysql://localhost:3306/usuarios";
    private static String username = "root";
    private static String password = "12345";

    public Index() {
        super("Pantalla de inicio de sesión");

        usernameLabel = new JLabel("Usuario:");
        passwordLabel = new JLabel("Contraseña:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Continuar");

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.setBackground(Color.CYAN);

        add(panel);

        loginButton.addActionListener(this);

        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            System.out.println("Conectado a la base de datos");

            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user);
            statement.setString(2, pass);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                System.out.println("Ingreso correctamente con rol: " + role);

                if (role.equals("admin")) {
                    AdminMain.show();
                } else if (role.equals("invitado")) {
                    InvitadoMain.shows();
                }
                setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No es válida su información, intente de nuevo", "Ingreso fallido", JOptionPane.ERROR_MESSAGE);
            }
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos " + ex.getMessage(), "Ingreso fallido", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Index();
    }
}

class InvitadoMain {

    public static void shows() {
        JOptionPane.showMessageDialog(null, "Ingresaste correctamente como invitado", "Ingreso correcto", JOptionPane.INFORMATION_MESSAGE);
        Formato objectForm1 = new Formato(true);
        objectForm1.setVisible(true);


    }}
class AdminMain {
    public static void show() {
        JOptionPane.showMessageDialog(null, "Ingresaste correctamente como administrador", "Ingreso correcto", JOptionPane.INFORMATION_MESSAGE);
        Formato objectForm = new Formato(false);
        objectForm.setVisible(true);

    }
}
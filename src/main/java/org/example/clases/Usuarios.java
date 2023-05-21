package org.example.clases;


import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class Usuarios {
    private int id;
    private String name;
    private String password;
    private String role;
    private String authenticatedRole;

    //private boolean isAdmin = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public boolean autenticarUsuario(String username, String password) {
        Coneccion objectConnection = new Coneccion();

        String sqlQuery = "SELECT role FROM Users WHERE username = ? AND password = ?";

        try {
            CallableStatement cs = objectConnection.establishConnection().prepareCall(sqlQuery);
            cs.setString(1, username);
            cs.setString(2, password);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                authenticatedRole = rs.getString("role");
                return true;  // Autenticación exitosa
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al autenticar: " + e.toString());
        }

        return false;  // Autenticación fallida
    }
    public void InsertUsers(JTextField username, JTextField password, JComboBox<String> cmbRole) {
        setName(username.getText());
        setPassword(password.getText());
        setRole((String) cmbRole.getSelectedItem());

        Coneccion objectConnection = new Coneccion();

        String sqlQuery = "INSERT INTO Users (username, password, role) VALUES (?,?,?)";

        try {
            CallableStatement cs = objectConnection.establishConnection().prepareCall(sqlQuery);

            cs.setString(1, getName());
            cs.setString(2, getPassword());
            cs.setString(3, getRole());

            cs.execute();
            JOptionPane.showMessageDialog(null, "Se insertó correctamente");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e.toString());
        }
    }

    public void ModifyUsuario(JTextField id, JTextField name, JTextField password, JComboBox<String> cmbRole) {
        setId(Integer.parseInt(id.getText()));
        setName(name.getText());
        setPassword(password.getText());
        setRole((String) cmbRole.getSelectedItem());

        Coneccion objectConnection = new Coneccion();

        String sqlQuery = "UPDATE Users SET username = ?, password = ?, role = ? WHERE id = ?";

        try {
            CallableStatement cs = objectConnection.establishConnection().prepareCall(sqlQuery);

            cs.setString(1, getName());
            cs.setString(2, getPassword());
            cs.setString(3, getRole());
            cs.setInt(4, getId());

            cs.execute();

            JOptionPane.showMessageDialog(null, "Se modificó el usuario");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.toString());
        }
    }

    public void ShowUsers(JTable tableUsuarios) {
        Coneccion objectConnection = new Coneccion();
        DefaultTableModel tModel = new DefaultTableModel();
        TableRowSorter<TableModel> sortTable = new TableRowSorter<TableModel>(tModel);
        tableUsuarios.setRowSorter(sortTable);

        String sqlQuery = "";
        tModel.addColumn("Id");
        tModel.addColumn("Nombre de usuario");
        tModel.addColumn("Contraseña");
        tModel.addColumn("Rol");
        tableUsuarios.setModel(tModel);

        sqlQuery = "SELECT * FROM Users";

        String[] data = new String[4];
        Statement st;

        try {
            st = objectConnection.establishConnection().createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);

            while (rs.next()) {
                data[0] = rs.getString(1);
                data[1] = rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getString(4);

                tModel.addRow(data);
            }

            tableUsuarios.setModel(tModel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se puede mostrar esta base de datos: " + e.toString());
        }
    }

    public void SelectUsuario(JTable tableUsuarios, JTextField id, JTextField username, JTextField password, JComboBox<String> role) {
        try {
            int rowCounter = tableUsuarios.getSelectedRow();
            if (rowCounter >= 0) {
                id.setText((String) tableUsuarios.getValueAt(rowCounter, 0));
                username.setText((String) tableUsuarios.getValueAt(rowCounter, 1));
                password.setText((String) tableUsuarios.getValueAt(rowCounter, 2));
                role.setSelectedItem((String) tableUsuarios.getValueAt(rowCounter, 3));
            } else {
                JOptionPane.showMessageDialog(null, "Deje de seleccionar");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la selección: " + e.toString());
        }
    }

    public void DeleteReg(String id, JComboBox<String> cmbRole) {
        setId(Integer.parseInt(id));
        setRole((String) cmbRole.getSelectedItem());

        Coneccion objectConnection = new Coneccion();

        String sqlQuery = "DELETE FROM Users WHERE id = ?";

        try {
            CallableStatement cs = objectConnection.establishConnection().prepareCall(sqlQuery);

            cs.setInt(1, getId());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Se eliminó el usuario correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.toString());
        }
    }
}

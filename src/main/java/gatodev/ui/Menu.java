package gatodev.ui;

import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

@Getter
public class Menu {
    private JPanel menu;
    private JTable tblContacts;
    private JTextField txtId;
    private JLabel lblTitle;
    private JButton btnSearch;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnExit;

    public Menu() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Nombres");
        model.addColumn("Apellidos");
        model.addColumn("Compañía");
        model.addColumn("Teléfono");
        model.addColumn("Email");
        model.addColumn("Fecha de nacimiento");
        model.addColumn("Dirección");
        tblContacts.setModel(model);
        model.addRow(new Object[]{
                "1", "Juan", "Pérez", "EmpresaX", "123456789", "juan@email.com", "01/01/1990", "Calle Falsa 123"
        });

    }
}

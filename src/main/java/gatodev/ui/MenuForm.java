package gatodev.ui;

import gatodev.dao.ContactDAO;
import gatodev.models.Contact;
import gatodev.ui.components.BodyForm;
import gatodev.ui.components.FieldForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MenuForm {
    private final ContactDAO contactDAO = ContactDAO.instance;
    private final JTable tblContact = new JTable();
    private final FieldForm ffId = new FieldForm("ID", "Escriba un ID");
    private final BodyForm bfSearch = new BodyForm(new JButton("Buscar"), ffId);
    private final BodyForm bfDelete = new BodyForm(new JButton("Eliminar"), ffId);

    public MenuForm() {
        JFrame frm = new JFrame("Menu");
        JPanel pnlMain = new JPanel();
        JPanel pnlButtons = new JPanel();
        JScrollPane src = new JScrollPane();
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(800, 600);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        frm.setContentPane(pnlMain);

        pnlMain.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> System.exit(0));

        pnlButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Asignar la tabla al JScrollPane CORRECTAMENTE
        src.setViewportView(tblContact);

        // Agregar pnlButtons
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        pnlMain.add(pnlButtons);

        // Agrega la tabla y la expande
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.BOTH;
        pnlMain.add(src, gbc);

        // Agregar botón Salir
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        pnlMain.add(btnSalir, gbc);

        //Agrega botones funcionales
        bfSearch.setOnSuccess(() -> {
            try {
                Long id = Long.parseLong(bfSearch.getFields().get(0).getText());
                Contact contact = contactDAO.findById(id);
                System.out.println(contact);
                if (contact != null) {
                    fillTable(List.of(contact));
                } else {
                    fillTable(contactDAO.findAll());
                    JOptionPane.showMessageDialog(null, "Contacto no encontrado.");
                }
                bfSearch.getFields().get(0).getTxtField().setText("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ID inválido.");
            }
        });
        bfDelete.setOnSuccess(() -> {
            try {
                Long id = Long.parseLong(bfDelete.getFields().get(0).getText());
                contactDAO.delete(id);
                JOptionPane.showMessageDialog(null, "Contacto eliminado.");
                fillTable(contactDAO.findAll());
                bfSearch.getFields().get(0).getTxtField().setText("");
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ID inválido.");
            }
        });

        pnlButtons.add(bfSearch.getFields().get(0).getTxtField());
        pnlButtons.add(bfSearch.getBtnSubmit());

        JButton btnList = new JButton("Listar");
        btnList.addActionListener(e -> fillTable(contactDAO.findAll()));

        JButton btnCreate = getBtnCreate();
        JButton btnUpdate = getBtnUpdate();

        pnlButtons.add(btnList);
        pnlButtons.add(btnCreate);
        pnlButtons.add(btnUpdate);
        pnlButtons.add(bfDelete.getBtnSubmit());

        // Configurar modelo de la tabla
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Nombres");
        model.addColumn("Apellidos");
        model.addColumn("Compañía");
        model.addColumn("Teléfono");
        model.addColumn("Email");
        model.addColumn("Fecha de nacimiento");
        model.addColumn("Dirección");
        tblContact.setModel(model);
        fillTable(contactDAO.findAll());
    }

    private JButton getBtnUpdate() {
        JButton btnUpdate = new JButton("Actualizar");

        btnUpdate.addActionListener(e -> {
            int selectedRow = tblContact.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null,
                        "Debe seleccionar un contacto.");
                return;
            }

            // Crear un nuevo formulario cada vez
            ContactForm contactFormUpdate = new ContactForm(btnUpdate.getText());

            // Rellenar campos
            contactFormUpdate.getFfFirstname().getTxtField()
                    .setText(tblContact.getValueAt(selectedRow, 1).toString());
            contactFormUpdate.getFfLastname().getTxtField()
                    .setText(tblContact.getValueAt(selectedRow, 2).toString());
            contactFormUpdate.getFfCompany().getTxtField()
                    .setText(tblContact.getValueAt(selectedRow, 3).toString());
            contactFormUpdate.getFfPhoneNumber().getTxtField()
                    .setText(tblContact.getValueAt(selectedRow, 4).toString());
            contactFormUpdate.getFfEmail().getTxtField()
                    .setText(tblContact.getValueAt(selectedRow, 5).toString());
            contactFormUpdate.getFfBirthDate().getTxtField()
                    .setText(tblContact.getValueAt(selectedRow, 6).toString());
            contactFormUpdate.getFfAddress().getTxtField()
                    .setText(tblContact.getValueAt(selectedRow, 7).toString());

            // Obtener ID (columna 0)
            Long id = Long.parseLong(tblContact.getValueAt(selectedRow, 0).toString());

            // Acción al enviar
            contactFormUpdate.setOnSubmit(() -> {
                Contact updatedContact = contactFormUpdate.getContact();
                if (updatedContact != null) {
                    updatedContact.setId(id); // Asegurar que conserve su ID
                    contactDAO.update(updatedContact); // Método update en tu DAO
                    JOptionPane.showMessageDialog(null,
                            "Contacto actualizado exitosamente.");
                    fillTable(contactDAO.findAll());
                }
            });

            // Mostrar diálogo
            JDialog dialog = new JDialog();
            dialog.setTitle("Actualizar contacto");
            dialog.setContentPane(contactFormUpdate.buildForm());
            dialog.setSize(400, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.setVisible(true);
        });
        return btnUpdate;
    }

    private JButton getBtnCreate() {
        JButton btnCreate = new JButton("Crear");

        btnCreate.addActionListener(e -> {
            ContactForm contactFormCreate = new ContactForm(btnCreate.getText());
            contactFormCreate.setOnSubmit(() -> {
                Contact newContact = contactFormCreate.getContact();
                if (newContact != null) {
                    contactDAO.save(newContact);
                    JOptionPane.showMessageDialog(null,
                            "Contacto creado exitosamente.");
                    fillTable(contactDAO.findAll());
                }
            });
            JDialog dialog = new JDialog();
            dialog.setTitle("Crear contacto");
            dialog.setContentPane(contactFormCreate.buildForm());
            dialog.setSize(400, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.setVisible(true);
        });
        return btnCreate;
    }

    public void fillTable(List<Contact> contacts) {
        DefaultTableModel model = (DefaultTableModel) tblContact.getModel();
        model.setRowCount(0);
        (contacts).forEach(c -> model.addRow(new Object[]{
                c.getId(),
                c.getFirstname(),
                c.getLastname(),
                c.getCompany(),
                c.getPhoneNumber(),
                c.getEmail(),
                c.getBirthDate(),
                c.getAddress(),
        }));
    }
}

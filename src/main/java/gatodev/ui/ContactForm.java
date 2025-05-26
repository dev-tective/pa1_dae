package gatodev.ui;

import gatodev.models.Contact;
import gatodev.ui.components.BodyForm;
import gatodev.ui.components.FieldForm;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Getter
@Setter
public class ContactForm {
    private final FieldForm ffFirstname;
    private final FieldForm ffLastname;
    private final FieldForm ffCompany;
    private final FieldForm ffPhoneNumber;
    private final FieldForm ffEmail;
    private final FieldForm ffBirthDate;
    private final FieldForm ffAddress;
    private final BodyForm bdContact;
    private Runnable onSubmit;

    public ContactForm(String submit) {
        ffFirstname = new FieldForm("nombre", "Ingrese el nombre");
        ffLastname = new FieldForm("apellido", "Ingrese el apellido");
        ffCompany = new FieldForm("compañía", "Ingrese la compañía");
        ffPhoneNumber = new FieldForm("numero telefónico", "Ingrese el numero telefónico");
        ffEmail = new FieldForm("email", "Ingrese el email");
        ffBirthDate = new FieldForm("fecha de nacimiento", "yyyy-MM-dd");
        ffAddress = new FieldForm("dirección", "Ingrese la dirección");
        bdContact = new BodyForm(new JButton(submit),
                ffFirstname,
                ffLastname,
                ffCompany,
                ffPhoneNumber,
                ffEmail,
                ffBirthDate,
                ffAddress
        );
    }

    public JPanel buildForm() {
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(0, 2, 10, 10));

        innerPanel.add(new JLabel("Nombre:"));
        innerPanel.add(ffFirstname.getTxtField());
        innerPanel.add(new JLabel("Apellido:"));
        innerPanel.add(ffLastname.getTxtField());
        innerPanel.add(new JLabel("Compañía:"));
        innerPanel.add(ffCompany.getTxtField());
        innerPanel.add(new JLabel("Teléfono:"));
        innerPanel.add(ffPhoneNumber.getTxtField());
        innerPanel.add(new JLabel("Email:"));
        innerPanel.add(ffEmail.getTxtField());
        innerPanel.add(new JLabel("Nacimiento:"));
        innerPanel.add(ffBirthDate.getTxtField());
        innerPanel.add(new JLabel("Dirección:"));
        innerPanel.add(ffAddress.getTxtField());

        innerPanel.add(new JLabel());
        innerPanel.add(bdContact.getBtnSubmit());

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // top, left, bottom, right
        paddedPanel.add(innerPanel, BorderLayout.CENTER);

        return paddedPanel;
    }

    // Getters opcionales para obtener los datos
    public Contact getContact() {
        try {
            return Contact.builder()
                    .firstname(ffFirstname.getText())
                    .lastname(ffLastname.getText())
                    .company(ffCompany.getText())
                    .phoneNumber(ffPhoneNumber.getText())
                    .email(ffEmail.getText())
                    .birthDate(LocalDate.parse(ffBirthDate.getText()))
                    .address(ffAddress.getText())
                    .build();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null,
                    "Fecha de nacimiento inválida. Use el formato yyyy/MM/dd.");
            return null;
        }
    }

    public void setOnSubmit(Runnable onSubmit) {
        this.onSubmit = onSubmit;
        bdContact.setOnSuccess(onSubmit);
    }
}

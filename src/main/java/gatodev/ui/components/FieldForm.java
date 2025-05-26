package gatodev.ui.components;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

@Getter
public class FieldForm {
    private final JTextField txtField = new JTextField();
    private final String fieldName;
    private final String placeholder;

    public FieldForm(String fieldName, String placeholder) {
        this.fieldName = fieldName;
        this.placeholder = placeholder;

        txtField.setText(this.placeholder);
        txtField.setForeground(Color.GRAY);

        txtField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txtField.getText().equals(placeholder)) {
                    txtField.setText("");
                    txtField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (txtField.getText().isBlank()) {
                    txtField.setForeground(Color.GRAY);
                    txtField.setText(placeholder);
                }
            }
        });
    }

    public String isValid() {
        return getText().isBlank() ? String.format("El campo %s esta vacío.", fieldName) : null;
    }

    public String getText() {
        String text = txtField.getText();
        return text.equals(placeholder) ? "" : text;
    }
}

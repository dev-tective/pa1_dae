package gatodev.ui.components;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Setter
@Getter
public class BodyForm {
    private final List<FieldForm> fields;
    private final JButton btnSubmit;
    private Runnable onSuccess;

    public BodyForm(JButton btnSubmit, FieldForm... fields) {
        this.fields = Arrays.stream(fields).toList();
        this.btnSubmit = btnSubmit;

        this.btnSubmit.addActionListener(e -> {
            if (validateFields() && onSuccess != null) {
                onSuccess.run();
            }
        });
    }

    private boolean validateFields() {
        String message = fields.stream().map(FieldForm::isValid)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));

        if (!message.isBlank()) {
            JOptionPane.showMessageDialog(null, message,
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
}

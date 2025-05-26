package gatodev;

import gatodev.ui.MenuForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuForm::new);
    }
}

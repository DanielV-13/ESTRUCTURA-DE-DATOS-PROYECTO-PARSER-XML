import javax.swing.SwingUtilities;

public class MainGUI {
    public static void main(String[] args) {

        // Esto asegura que Swing se ejecute correctamente
        SwingUtilities.invokeLater(() -> {
            VentanaXML v = new VentanaXML();
            v.setVisible(true);
        });
    }
}

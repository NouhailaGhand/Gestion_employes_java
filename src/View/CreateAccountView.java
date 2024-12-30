package View;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class CreateAccountView{
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    int id = 0;
    private JLabel roleLabel;
    private JComboBox<String> roleComboBox;

    public CreateAccountView() {
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("create a conte");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        panel.add(usernameField);

        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(loginButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return  new String(passwordField.getPassword());
    }

    public void afficherMessageErreur(String message) {
        JOptionPane.showMessageDialog(frame, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public void afficherMessageSucces(String message) {
        JOptionPane.showMessageDialog(frame, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setID(int id){
        this.id = id;
    }

    public int getID(){
        return id;
    }
}

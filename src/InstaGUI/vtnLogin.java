/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Insta.GestorInsta;
import Insta.Usuario;
import Insta.CredencialesInvalidas;
import Insta.SesionManager;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author HP
 */
public class vtnLogin extends JDialog {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public vtnLogin() {
        //super(parent, "INSTA - Iniciar Sesion", true);
        setTitle("INSTA - Inicar Sesion");
        setSize(350, 250);
        setLayout(new GridLayout(4, 1, 10, 10));
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 5, 5));

        panelCampos.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        panelCampos.add(txtUsername);

        panelCampos.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panelCampos.add(txtPassword);

        JButton btnLogin = new JButton("Iniciar Sesion");
        JButton btnRegistrar = new JButton("Crear Cuenta");

        btnLogin.addActionListener(e -> iniciarSesion());
        btnRegistrar.addActionListener(e -> abrirRegistro());

        add(panelCampos);
        add(new JLabel(""));
        add(btnLogin);
        add(btnRegistrar);
    }

    private void iniciarSesion() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar usuario y contraseña");
            return;
        }

        try {
            Usuario usuarioLogueado = GestorInsta.logIn(username, password);

            SesionManager.setUsuarioActual(usuarioLogueado);

            JOptionPane.showMessageDialog(this, "¡Sesion inciado como " + username + "!");

            /*Aquí debe abrirse la ventana principal de INSTA (ej. el TimeLine)
        la ventana principal de INSTA (donde se muestra el TimeLine) debería abrirse aquí.
        pr ej, clase llamada 'vtnInstaPrincipal' o algo asi, nose
        vtnInstaPrincipal p = new vtnInstaPrincipal(usuarioLogueado); 
        p.setVisible(true);  despues miro qpdo*/
            
            this.dispose();

        } catch (CredencialesInvalidas e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar usuarios: " + e.getMessage());
        }
    }

    private void abrirRegistro() {
        vtnRegistro r = new vtnRegistro(null);
        r.setVisible(true);
    }

    /*public static void main(String[] args) {
        vtnLogin l = new vtnLogin();
        l.setVisible(true);
    }*/
}

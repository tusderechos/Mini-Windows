/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Insta.GestorInsta;
import Compartidas.Usuario;
import Insta.CredencialesInvalidas;
import Insta.SesionManager;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author HP
 */

public class vtnLogin extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private final Color COLOR_FONDO = new Color(18, 18, 18);
    private final Color COLOR_CAMPO_FONDO = new Color(38, 38, 38);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_LINK = new Color(50, 150, 255); 
    private final Color COLOR_BOTON_DOMINANTE = new Color(193, 53, 132); 
    private final Font FONT_LOGO = new Font("Arial", Font.BOLD, 36);

    public vtnLogin() {
        setTitle("INSTA - Inicar Sesion");
        setSize(600, 800);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        inicializarComponentes();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void inicializarComponentes() {
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(COLOR_FONDO);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridwidth = 2;
        
        JLabel labelLogo = new JLabel("Instagram");
        labelLogo.setFont(FONT_LOGO);
        labelLogo.setForeground(new Color(255, 100, 180)); 
        labelLogo.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        panelCentral.add(labelLogo, gbc);
        
        gbc.gridy = 1;
        panelCentral.add(Box.createVerticalStrut(30), gbc);

        txtUsername = crearCampoTexto("Nombre de usuario");
        gbc.gridy = 2;
        gbc.ipady = 10; 
        panelCentral.add(txtUsername, gbc);

        txtPassword = crearCampoPassword("Contraseña");
        gbc.gridy = 3;
        panelCentral.add(txtPassword, gbc);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 18));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setOpaque(true);
        
        btnLogin.setBackground(COLOR_BOTON_DOMINANTE); 

        Dimension loginSize = new Dimension(350, 50);
        btnLogin.setPreferredSize(loginSize);
        btnLogin.setMaximumSize(loginSize);

        btnLogin.addActionListener(e -> iniciarSesion());
        
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 10, 0);
        panelCentral.add(btnLogin, gbc);
        
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelFooter.setBackground(COLOR_FONDO);
        panelFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(38, 38, 38))); 

        JLabel labelNoCuenta = new JLabel("¿No tienes una cuenta?");
        labelNoCuenta.setForeground(new Color(150, 150, 150));
        
        JButton btnRegistrar = new JButton("Regístrate");
        btnRegistrar.setForeground(COLOR_LINK); 
        btnRegistrar.setBackground(COLOR_FONDO);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 12));
        
        btnRegistrar.addActionListener(e -> abrirRegistro());

        panelFooter.add(labelNoCuenta);
        panelFooter.add(btnRegistrar);
        
        add(panelCentral, BorderLayout.CENTER);
        add(panelFooter, BorderLayout.SOUTH);
    }
       
    private JTextField crearCampoTexto(String placeholder) {
        JTextField campo = new JTextField();
        campo.setPreferredSize(new Dimension(350, 40));
        campo.setBackground(COLOR_CAMPO_FONDO);
        campo.setForeground(COLOR_TEXTO);
        campo.setCaretColor(COLOR_TEXTO); 
        
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        campo.setText(placeholder);
        campo.setForeground(new Color(150, 150, 150)); 
        
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(COLOR_TEXTO);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(new Color(150, 150, 150));
                }
            }
        });
        return campo;
    }

    private JPasswordField crearCampoPassword(String placeholder) {
        JPasswordField campo = new JPasswordField();
        campo.setPreferredSize(new Dimension(350, 40));
        campo.setBackground(COLOR_CAMPO_FONDO);
        campo.setForeground(COLOR_TEXTO);
        campo.setCaretColor(COLOR_TEXTO);
        
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        campo.setEchoChar((char) 0); 
        campo.setText(placeholder);
        campo.setForeground(new Color(150, 150, 150));
        
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                String currentText = new String(campo.getPassword());
                if (currentText.equals(placeholder)) {
                    campo.setText("");
                    campo.setEchoChar('*'); 
                    campo.setForeground(COLOR_TEXTO);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                String currentText = new String(campo.getPassword());
                if (currentText.isEmpty()) {
                    campo.setEchoChar((char) 0); 
                    campo.setText(placeholder);
                    campo.setForeground(new Color(150, 150, 150));
                }
            }
        });
        return campo;
    }

    private void iniciarSesion() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        if (username.equals("Nombre de usuario")) {
             username = "";
        }
        if (password.equals("Contraseña")) {
             password = "";
        }

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar usuario y contraseña", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Usuario usuarioLogueado = GestorInsta.logIn(username, password);

            SesionManager.setUsuarioActual(usuarioLogueado);

            JOptionPane.showMessageDialog(this, "¡Sesion iniciada como " + username + "!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            vtnInstaPrincipal p = new vtnInstaPrincipal(usuarioLogueado);
            p.setVisible(true);
            this.dispose();

        } catch (CredencialesInvalidas e) {
            JOptionPane.showMessageDialog(this, "Credenciales inválidas: Usuario no encontrado o contraseña incorrecta.", "Error de Inicio de Sesión", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar usuarios: " + e.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirRegistro() {
        vtnRegistro r = new vtnRegistro();
        r.setVisible(true);
    }
}

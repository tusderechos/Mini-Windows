/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI;

/**
 *
 * @author Hp
 */

import javax.swing.*;
import java.awt.*;

import OS.Core.SistemaOperativo;
import OS.Core.SesionActual;

public class Login extends JFrame {
    
    private final SistemaOperativo SO;
    
    private final JTextField TxtUser = new JTextField(20);
    private final JPasswordField TxtPass = new JPasswordField(20);
    private final JButton BtnLogin = new JButton("Iniciar Sesion");
    private final JLabel LblEstado = new JLabel(" ");
    
    public Login(SistemaOperativo SO) {
        this.SO = SO;
        
        setTitle("Mini-Windows - Iniciar Sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 260);
        setLocationRelativeTo(null);
        setResizable(false);
        AplicarLook();
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titulo = new JLabel("Bienvenido");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(TxtUser, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(TxtPass, gbc);
        
        JPanel filabotones = new JPanel();
        filabotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filabotones.add(BtnLogin);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(filabotones, gbc);
        
        gbc.gridy = 4;
        LblEstado.setForeground(new Color(120, 120, 120));
        panel.add(LblEstado, gbc);
        
        setContentPane(panel);
        Wiring();
    }
    
    private void Wiring() {
        BtnLogin.addActionListener(e -> IntentarLogin());
        
        getRootPane().setDefaultButton(BtnLogin);
    }
    
    private void IntentarLogin() {
        String usuario = TxtUser.getText().trim();
        String contra = new String(TxtPass.getPassword());
        
        if (usuario.isBlank() || contra.isBlank()) {
            LblEstado.setText("Complete usuario y contraseña");
            return;
        }
        
        if (SO.IniciarSesion(usuario, contra)) {
            SesionActual.IniciarSesion(SO.getUsuarioActual());
            new Escritorio(SO).setVisible(true);
            dispose();
        } else {
            LblEstado.setText("Credenciales invalidas o cuenta inactiva");
            TxtPass.setText("");
            TxtUser.requestFocusInWindow();
        }
    }
    
    private void AplicarLook() {
        getContentPane().setBackground(Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 13));
    }
}

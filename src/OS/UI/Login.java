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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import OS.Core.SistemaOperativo;
import OS.Core.SesionActual;
import OS.UI.util.TemaOscuro;

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
        
        getContentPane().setBackground(TemaOscuro.BG);
                
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(TemaOscuro.BG);
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titulo = new JLabel("Bienvenido");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        titulo.setForeground(TemaOscuro.TEXTO);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);
        
        JLabel LblUser = new JLabel("Usuario:");
        LblUser.setForeground(TemaOscuro.TEXTO);
        
        JLabel LblPass = new JLabel("Contraseña:");
        LblPass.setForeground(TemaOscuro.TEXTO);
        
        TxtUser.setBackground(TemaOscuro.CARD);
        TxtUser.setForeground(TemaOscuro.TEXTO);
        TxtUser.setPreferredSize(new Dimension(180, 28));
        TxtUser.setBorder(BorderFactory.createCompoundBorder(new LineBorder(TemaOscuro.LINEA), new EmptyBorder(6, 8, 6, 8)));
        
        TxtPass.setBackground(TemaOscuro.CARD);
        TxtPass.setForeground(TemaOscuro.TEXTO);
        TxtPass.setPreferredSize(new Dimension(180, 28));
        TxtPass.setBorder(BorderFactory.createCompoundBorder(new LineBorder(TemaOscuro.LINEA), new EmptyBorder(6, 8, 6, 8)));
        
        BtnLogin.setBackground(TemaOscuro.CARD);
        BtnLogin.setForeground(TemaOscuro.TEXTO);
        BtnLogin.setFocusPainted(false);
        BtnLogin.setPreferredSize(new Dimension(80, 28));
        BtnLogin.setBorder(BorderFactory.createCompoundBorder(new LineBorder(TemaOscuro.LINEA), new EmptyBorder(6, 12, 6, 12)));
        
        LblEstado.setForeground(TemaOscuro.SUTIL);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(LblUser, gbc);
        gbc.gridx = 1;
        panel.add(TxtUser, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(LblPass, gbc);
        gbc.gridx = 1;
        panel.add(TxtPass, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(BtnLogin, gbc);
        
        gbc.gridy = 4;
        panel.add(LblEstado, gbc);
        
        setContentPane(panel);
        
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
}

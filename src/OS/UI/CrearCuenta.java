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

import OS.Core.GestorCarpetasUsuario;
import OS.Core.SistemaOperativo;
import Compartidas.Usuario;
import OS.Core.SesionActual;

public class CrearCuenta extends JFrame {
    
    private final SistemaOperativo SO;
    private final JFrame VentanaLogin;
    
    private final JTextField TxtNombre = new JTextField(22);
    private final JComboBox<String> CBGenero = new JComboBox<>(new String[]{"M", "F"});
    private final JTextField TxtUser = new JTextField(18);
    private final JPasswordField TxtPass = new JPasswordField(18);
    private final JSpinner SPEdad = new JSpinner(new SpinnerNumberModel(18, 1, 120, 1));
    private final JButton BtnCrear = new JButton("Crear Cuenta");
    private final JButton BtnVolver = new JButton("Volver");
    private final JLabel LblEstado = new JLabel(" ");
    
    public CrearCuenta(SistemaOperativo SO, JFrame VentanaLogin) {
        this.VentanaLogin = VentanaLogin;
        this.SO = SO;
        
        if (SesionActual.getUsuario() == null || !SesionActual.getUsuario().isAdministrador()) {
            JOptionPane.showMessageDialog(this, "Acceso denegado.\nNo eres admin", "Mini-Windows", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        setTitle("Mini-Windows - Crear Cuenta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        AplicarLook();
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titulo = new JLabel("Nueva Cuenta");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1;
        panel.add(TxtNombre, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Genero (M/F):"), gbc);
        gbc.gridx = 1;
        panel.add(CBGenero, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(TxtUser, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(TxtPass, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1;
        panel.add(SPEdad, gbc);
        
        JPanel fila = new JPanel();
        fila.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        fila.add(BtnVolver);
        fila.add(BtnCrear);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(fila, gbc);
        
        gbc.gridy = 7;
        LblEstado.setForeground(new Color(120, 120, 120));
        panel.add(LblEstado, gbc);
        
        setContentPane(panel);
        Wiring();
    }
    
    private void Wiring() {
        BtnCrear.addActionListener(e -> CrearCuenta());
        BtnVolver.addActionListener(e -> {
            VentanaLogin.setVisible(true);
            dispose();
        });
        
        getRootPane().setDefaultButton(BtnCrear);
    }
    
    private void CrearCuenta() {
        String nombre = TxtNombre.getText().trim();
        String generostr = (String) CBGenero.getSelectedItem();
        char genero = (generostr == null || generostr.isBlank()) ? 'M' : generostr.charAt(0);
        String usuario = TxtUser.getText().trim();
        String contra = new String(TxtPass.getPassword());
        int edad = (Integer) SPEdad.getValue();
        
        if (nombre.isBlank() || usuario.isBlank() || contra.isBlank()) {
            LblEstado.setText("Campos Obligatorios: nombre, usuario y contraseña");
            return;
        }
        
        Usuario usu = SO.RegistrarUsuario(nombre, genero, usuario, contra, edad, null);
        
        if (usu == null) {
            LblEstado.setText("No se pudo crear el usuario.\nPuede ser que ya exista el usuario");
            return;
        }
        
        GestorCarpetasUsuario.CrearEstructuraUsuario(usuario);
        
        JOptionPane.showMessageDialog(this, "Cuenta creada con exito", "Mini-Windows", JOptionPane.INFORMATION_MESSAGE);
        VentanaLogin.setVisible(true);
        dispose();
    }
    
    private void AplicarLook() {
        getContentPane().setBackground(Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Spinner.font", new Font("Segoe UI", Font.PLAIN, 13));
    }
}

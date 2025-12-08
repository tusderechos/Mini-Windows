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
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.EmptyBorder;

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
    private final JButton BtnCrear = CrearBoton("Crear Cuenta", true, 18);
    private final JButton BtnVolver = CrearBoton("Volver", false, 18);
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
        
        Usuario usu = SO.RegistrarUsuario(nombre, genero, usuario, contra, edad);
        
        if (usu == null) {
            LblEstado.setText("No se pudo crear el usuario.\nPuede ser que ya exista el usuario");
            return;
        }
        
        GestorCarpetasUsuario.CrearEstructuraUsuario(usuario);
        
        JOptionPane.showMessageDialog(this, "Cuenta creada con exito", "Mini-Windows", JOptionPane.INFORMATION_MESSAGE);
        VentanaLogin.setVisible(true);
        dispose();
    }
    
    private JButton CrearBoton(String texto, boolean primario, int radio) {
        //Colores base segun el tipo
        Color BG = primario ? new Color(84, 36, 122) : new Color(44, 44, 50);
        Color hover = primario ? new Color(110, 50, 150) : new Color(60, 60, 68);
        Color presionado = primario ? new Color(60, 20, 95) : new Color(24, 24, 28);
        Color textoC = primario ? Color.WHITE : new Color(230, 230, 230);
        
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                int w = getWidth();
                int h = getHeight();
                
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                //Estado actual
                ButtonModel modelo = getModel();
                Color fill = !isEnabled() ? BG.darker().darker() : modelo.isPressed() ? presionado : modelo.isRollover() ? hover : BG;
                
                //Fondo redondeado
                Shape rr = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, radio, radio);
                
                //Sombra sutil
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.fillRoundRect(2, 3, w - 4, h - 5, radio + 2, radio + 2);
                
                //Relleno
                g2d.setColor(fill);
                g2d.fill(rr);
                
                //Borde
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.draw(new RoundRectangle2D.Float(2, 3, w - 1, h - 1, radio, radio));
                
                g2d.setClip(rr);
                super.paintComponent(g);
                g2d.dispose();
            }
            
            @Override
            public boolean contains(int x, int y) {
                Shape rr = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
                return rr.contains(x, y);
            }
        };
        
        //Baseline de estilo
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setForeground(textoC);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setRolloverEnabled(true);
        boton.setFont(boton.getFont().deriveFont(Font.PLAIN, 12f));
        boton.setBorder(new EmptyBorder(6, 16, 6, 16));
        
        return boton;
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

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
import java.awt.event.KeyEvent;

import Compartidas.Usuario;
import OS.Core.SistemaOperativo;
import OS.Core.SesionActual;
import OS.UI.util.FullscreenHelper;

public class Escritorio extends JFrame {
    
    private final SistemaOperativo SO;
    private final JLabel LblUsuario = new JLabel(" ");
    
    public Escritorio(SistemaOperativo SO) {
        this.SO = SO;
        
        setTitle("Mini-Windows - Escritorio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));
        AplicarLook();
        
        JPanel barra = new JPanel();
        barra.setLayout(new BorderLayout());
        barra.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        barra.setBackground(new Color(245, 247, 250));
        
        JPanel izquierda = new JPanel();
        izquierda.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
        izquierda.setOpaque(false);
        
        JButton btnarchivos = new JButton("Archivos");
        JButton btnconsola = new JButton("Consola");
        JButton btneditor = new JButton("Editor");
        JButton btnusuarios = new JButton("Usuarios");
        JButton btnfull = new JButton("Pantalla Completa");
        
        izquierda.add(btnarchivos);
        izquierda.add(btnconsola);
        izquierda.add(btneditor);
        izquierda.add(btnusuarios);
        izquierda.add(btnfull);
        
        JPanel derecha = new JPanel();
        derecha.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        derecha.setOpaque(false);
        
        LblUsuario.setForeground(new Color(80, 80, 80));
        
        derecha.add(LblUsuario);
        
        barra.add(izquierda, BorderLayout.WEST);
        barra.add(derecha, BorderLayout.EAST);
        
        JPanel centro = new JPanel();
        centro.setLayout(new BorderLayout());
        centro.setBackground(Color.WHITE);
        
        setLayout(new BorderLayout());
        add(barra, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        
        Usuario usu = SesionActual.getUsuario();
        boolean esadmin = (usu != null && usu.isAdministrador());
        
        LblUsuario.setText(usu != null ? ("Usuario: " + usu.getUsuario() + (esadmin ? " (admin)" : "")) : "Sin sesion");
        btnusuarios.setVisible(esadmin);
        
//        btnconsola.addActionListener(e -> );
//        btnarchivos.addActionListener(e -> );
//        btneditor.addActionListener(e -> );
        btnusuarios.addActionListener(e -> {
            if (!esadmin) {
                JOptionPane.showMessageDialog(this, "Solo el administrador puede gestionar usuarios");
                return;
            }
            
            new CrearCuenta(SO, this).setVisible(true);
        });
        
        btnfull.addActionListener(e -> FullscreenHelper.toggle(this));
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(key -> {
            if (key.getID() == KeyEvent.KEY_PRESSED && key.getKeyCode() == KeyEvent.VK_F11) {
                FullscreenHelper.toggle(this);
                return true;
            }
            
            return false;
        });
    }
    
    private void AplicarLook() {
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 13));
        
        getContentPane().setBackground(Color.WHITE);
    }
}

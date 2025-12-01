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
import OS.Archivos.SistemaArchivo;
import OS.Core.SistemaOperativo;
import OS.Core.SesionActual;
import OS.UI.util.FullscreenHelper;
import OS.UI.util.TemaOscuro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Escritorio extends JFrame {
    
    private final SistemaOperativo SO;
    private final JLabel LblUsuario = LabelStyle(" ", true);
    
    public Escritorio(SistemaOperativo SO) {
        this.SO = SO;
        
        setTitle("Mini-Windows - Escritorio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));
        
        AplicarLook();
        WireActions();
        
        JPanel barra = new JPanel();
        barra.setLayout(new BorderLayout());
        barra.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        barra.setBackground(new Color(245, 247, 250));
        
        JPanel izquierda = new JPanel();
        izquierda.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
        izquierda.setOpaque(false);
        
        JButton btnarchivos = BotonStyle("Archivos");
        JButton btnconsola = BotonStyle("Consola");
        JButton btneditor = BotonStyle("Editor");
        JButton btnusuarios = BotonStyle("Usuarios");
        JButton btnfull = BotonStyle("Pantalla Completa");
        
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
        
        JPanel centro = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(TemaOscuro.BG);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        centro.setBackground(TemaOscuro.BG);
        
        JPanel tiles = card();
        tiles.setLayout(new GridLayout(2, 3, 14, 14));
        tiles.add(tile("Archivos", e -> new NavegadorArchivos().setVisible(true)));
        tiles.add(tile("Consola", e -> new Consola().setVisible(true)));
        tiles.add(tile("Editor de Texto", e -> new EditorTexto().setVisible(true)));
        tiles.add(tile("Fotos", e -> AbrirFoto()));
        tiles.add(tile("Pantalla Completa", e -> FullscreenHelper.toggle(this)));
        tiles.add(tile("Cerrar Sesion", e -> CerrarSesion()));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        centro.add(tiles, gbc);
        
        getContentPane().setBackground(TemaOscuro.BG);
        setLayout(new BorderLayout());
        add(barra, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        
        Usuario usu = SesionActual.getUsuario();
        boolean esadmin = (usu != null && usu.isAdministrador());
        
        LblUsuario.setText(usu != null ? ("Usuario: " + usu.getUsuario() + (esadmin ? " (admin)" : "")) : "Sin sesion");
        btnusuarios.setVisible(esadmin);
        
        btnconsola.addActionListener(e -> new Consola().setVisible(true));
        btnarchivos.addActionListener(e -> new NavegadorArchivos().setVisible(true));
        btneditor.addActionListener(e -> new EditorTexto().setVisible(true));
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
    
    private void WireActions() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F11"), "toggleF5");
        getRootPane().getActionMap().put("toggleF5", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FullscreenHelper.toggle(Escritorio.this);
            }
        });
    }
    
    private JButton BotonStyle(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(true);
        boton.setBackground(TemaOscuro.CARD);
        boton.setForeground(TemaOscuro.TEXTO);
        boton.setBorder(BorderFactory.createCompoundBorder(new LineBorder(TemaOscuro.LINEA), new EmptyBorder(8, 12, 8, 12)));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        return boton;
    }
    
    private JLabel LabelStyle(String texto, boolean sutil) {
        JLabel label = new JLabel(texto);
        label.setForeground(sutil ? TemaOscuro.SUTIL : TemaOscuro.TEXTO);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        return label;
    }
    
    private JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(TemaOscuro.CARD);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        
        return panel;
    }
    
    private JButton tile(String texto, ActionListener e) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(TemaOscuro.CARD);
        boton.setForeground(TemaOscuro.TEXTO);
        boton.setBorder(BorderFactory.createCompoundBorder(new LineBorder(TemaOscuro.LINEA), new EmptyBorder(18, 18, 18, 18)));
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.addActionListener(e);
        
        return boton;        
    }
    
    private void AbrirFoto() {
        JFileChooser fc = new JFileChooser(SistemaArchivo.getRutaUsuario());
        int r = fc.showOpenDialog(this);
        
        if (r == JFileChooser.APPROVE_OPTION) {
            new VisorImagenes(fc.getSelectedFile()).setVisible(true);
        }
    }
    
    private void CerrarSesion() {
        SesionActual.CerrarSesion();
        dispose();
        new Login(SO).setVisible(true);
    }
    
    private void AplicarLook() {
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 13));
        
        getContentPane().setBackground(Color.WHITE);
    }
}

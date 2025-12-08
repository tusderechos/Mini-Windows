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
import OS.UI.util.GradientWallpaper;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

public class Login extends JFrame {
    
    private final SistemaOperativo SO;
    
    private final JTextField TxtUser = new JTextField(20);
    private final JPasswordField TxtPass = new JPasswordField(20);
    private JButton BtnLogin;
    private final JLabel LblEstado = new JLabel(" ");
        
    public Login(SistemaOperativo SO) {
        this.SO = SO;
        
        setTitle("Mini-Windows - Iniciar Sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        
        GradientWallpaper fondo = new GradientWallpaper();
        fondo.setGradient(Color.MAGENTA.darker().darker(), new Color(18, 18, 22));
        fondo.setLayout(new GridBagLayout());
        setContentPane(fondo);
                
        JPanel card = CrearCardCristal();
        
        GridBagConstraints root = new GridBagConstraints();
        root.gridx = 0;
        root.gridy = 0;
        root.anchor = GridBagConstraints.CENTER;
        
        fondo.add(card, root);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        JLabel titulo = new JLabel("Bienvenido");
        titulo.setForeground(TemaOscuro.TEXTO);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 28f));
        
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        
        card.add(titulo, gbc);
        
        //Usuario
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        
        JLabel LblUser = new JLabel("Usuario:");
        LblUser.setForeground(TemaOscuro.TEXTO);
        LblUser.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        
        card.add(LblUser, gbc);
        
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        TxtUser.setColumns(22);
        Icon iconouser = UIManager.getIcon("FileView.computerIcon");
        card.add(CrearCampoConIcono(TxtUser, iconouser), gbc);
        
        //Contrasena
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        
        JLabel LblPass = new JLabel("Contraseña:");
        LblPass.setForeground(TemaOscuro.TEXTO);
        LblPass.setFont(LblPass.getFont().deriveFont(Font.BOLD, 16f));
        
        card.add(LblPass, gbc);
        
        //Fila de contrasena
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        TxtPass.setColumns(24);
        TxtPass.setOpaque(true);
        card.add(CrearPasswordConOjo(TxtPass), gbc);
        
        //Boton        
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        BtnLogin = CrearBoton("Iniciar Sesion", true, 22);
        
        card.add(BtnLogin, gbc);
        
        //Estado/errores
        gbc.gridy++;
        
        LblEstado.setHorizontalAlignment(SwingConstants.CENTER);
        LblEstado.setForeground(new Color(220, 80, 80));
        
        card.add(LblEstado, gbc);
        
        getRootPane().setDefaultButton(BtnLogin);
        fondo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "salir");
        
        fondo.getActionMap().put("salir", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        BtnLogin.addActionListener(e -> IntentarLogin());
        
        SwingUtilities.invokeLater(() -> TxtUser.requestFocusInWindow());
    }
    
    private void IntentarLogin() {
        String usuario = TxtUser.getText().trim();
        String contra = new String(TxtPass.getPassword());
        
        if (usuario.isBlank() || contra.isBlank()) {
            LblEstado.setText("Complete usuario y contraseña");
            return;
        }
        
        if (contra.length() < 5) {
            LblEstado.setText("La contraseña tiene que tener minimo 5 caracteres");
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
    
    private JPanel CrearCampo(JTextField field) {
        GradientWallpaper contenedor = new GradientWallpaper();
        contenedor.setLayout(new BorderLayout());
        contenedor.setOpaque(true);
        contenedor.setBorder(new EmptyBorder(4, 6, 4, 6));
        
        //Para que se vea el gradiente
        field.setOpaque(true);
        field.setBorder(null);
        field.setBackground(TemaOscuro.CARD.brighter().brighter());
        field.setForeground(TemaOscuro.TEXTO);
        field.setCaretColor(TemaOscuro.TEXTO);
        field.setFont(field.getFont().deriveFont(Font.BOLD, 14f));
        
        contenedor.setBorder(BorderFactory.createCompoundBorder(new LineBorder(TemaOscuro.LINEA, 1, true), new EmptyBorder(6, 10, 6, 10)));
        
        contenedor.add(field, BorderLayout.CENTER);
        
        return contenedor;
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
        boton.setFont(boton.getFont().deriveFont(Font.BOLD, 15f));
        boton.setBorder(new EmptyBorder(6, 16, 6, 16));
        
        return boton;
    }
    
    private JPanel CrearCardCristal() {
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                int radio = 18;
                
                //Fondo translucido
                g2d.setColor(new Color(25, 10, 30, 150));
                g2d.fillRoundRect(0, 0, w - 1, h - 1, radio, radio);
                
                //Borde doble (tipo cristal)
                g2d.setColor(new Color(255, 255, 255, 35));
                g2d.drawRoundRect(0, 0, w - 1, h - 1, radio, radio);
                g2d.setColor(new Color(0, 0, 0, 80));
                g2d.drawRoundRect(1, 1, w - 3, h - 3, radio - 2, radio - 2);
                
                g2d.dispose();
            }
        };
        
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(22, 28, 24, 28));
        
        return card;
    }
    
    private JPanel CrearCampoConIcono(JTextField field, Icon icono) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BorderLayout(8, 0));
        wrap.setOpaque(false);
        
        JLabel ico = new JLabel(icono);
        ico.setOpaque(false);
        
        JPanel campo = CrearCampo(field);
        campo.setOpaque(false);
        
        //Borde moradito cuando esta en focus
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(130, 80, 200), 2, true), new EmptyBorder(6, 10, 6, 10)));
            }
            @Override
            public void focusLost(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(new LineBorder(TemaOscuro.LINEA, 1, true), new EmptyBorder(6, 10, 6, 10)));
            }
        });
        
        wrap.add(ico, BorderLayout.EAST);
        wrap.add(campo, BorderLayout.CENTER);
        
        return wrap;
    }
    
    private JPanel CrearPasswordConOjo(JPasswordField pass) {
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BorderLayout());
        contenedor.setOpaque(false);
        
        JPanel campo = CrearCampo(pass);
        
        JButton ojo = new JButton("\uD83D\uDC41");
        ojo.setBorder(new EmptyBorder(0, 8, 0, 8));
        ojo.setContentAreaFilled(false);
        ojo.setFocusPainted(false);
        ojo.setForeground(TemaOscuro.TEXTO);
        ojo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        ojo.addActionListener(e -> {
            pass.setEchoChar(pass.getEchoChar() == 0 ? '\u2022' : (char)0);
        });
        
        JPanel derecha = new JPanel();
        derecha.setLayout(new BorderLayout());
        derecha.setOpaque(false);
        
        derecha.add(ojo, BorderLayout.CENTER);
        
        contenedor.add(campo, BorderLayout.CENTER);
        contenedor.add(derecha, BorderLayout.EAST);
        
        //Borde moradito cuando esta en focus
        pass.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(130, 80, 200), 2, true), new EmptyBorder(6, 10, 6, 10)));
            }
            @Override
            public void focusLost(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(new LineBorder(TemaOscuro.LINEA, 1, true), new EmptyBorder(6, 10, 6, 10)));
            }
        });
        
        pass.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean caps = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
                contenedor.setToolTipText(caps ? "Mayusculas activadas" : null);
            }
        });
        
        return contenedor;
    }
}

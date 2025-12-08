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
import java.awt.geom.RoundRectangle2D;
import javax.swing.plaf.basic.BasicTextFieldUI;

public class Login extends JFrame {
    
    private final SistemaOperativo SO;
    
    private final JTextField TxtUser = new JTextField(20);
    private final JPasswordField TxtPass = new JPasswordField(20);
    private JButton BtnLogin;
    private final JLabel LblEstado = new JLabel(" ");
    
    private char PassEcho;
    
    public Login(SistemaOperativo SO) {
        this.SO = SO;
        
        setTitle("Mini-Windows - Iniciar Sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        
        GradientWallpaper fondo = new GradientWallpaper();
        fondo.setGradient(Color.MAGENTA.darker().darker().darker(), new Color(18, 18, 22));
        fondo.setLayout(new GridBagLayout());
        setContentPane(fondo);
                
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(TemaOscuro.LINEA), new EmptyBorder(16, 18, 16, 18)));
        card.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        //Titulo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
      
        JLabel titulo = new JLabel("Bienvenido");
        titulo.setForeground(TemaOscuro.TEXTO);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 28f));
        
        card.add(titulo, gbc);
        
        //Usuario
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;
        
        JLabel LblUser = new JLabel("Usuario:");
        LblUser.setForeground(TemaOscuro.TEXTO);
        LblUser.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        
        card.add(LblUser, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        CrearCampo(TxtUser);
        TxtUser.setColumns(22);
        card.add(TxtUser, gbc);
        
        //Contrasena
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        
        JLabel LblPass = new JLabel("Contraseña:");
        LblPass.setForeground(TemaOscuro.TEXTO);
        
        card.add(LblPass, gbc);
        
        //Fila de contrasena
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        CrearCampo(TxtPass);
        TxtPass.setColumns(24);
        card.add(TxtPass, gbc);
        
        //Fila de checkbox
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        
        JCheckBox mostrar = new JCheckBox("Mostrar");
        mostrar.setOpaque(false);
        mostrar.setForeground(TemaOscuro.SUTIL);
        
        PassEcho = TxtPass.getEchoChar();
        
        mostrar.addActionListener(e -> TxtPass.setEchoChar(mostrar.isSelected() ? (char) 0 : PassEcho));
        
        //Margen superior para separarlo del campo
        ((GridBagLayout)card.getLayout()).setConstraints(mostrar, gbc);
        
        card.add(mostrar, gbc);
        
        //Boton        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        BtnLogin = CrearBoton("Iniciar Sesion", true, 22);
        
        card.add(BtnLogin, gbc);
        
        //Estado/errores
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        LblEstado.setHorizontalAlignment(SwingConstants.CENTER);
        LblEstado.setForeground(new Color(220, 80, 80));
        
        card.add(LblEstado, gbc);
        
        //Colocar tarjeta al centro en el fondo
        GridBagConstraints centro = new GridBagConstraints();
        centro.gridx = 0;
        centro.gridy = 0;
        centro.anchor = GridBagConstraints.CENTER;
                
        fondo.add(card, centro);
        
        BtnLogin.addActionListener(e -> IntentarLogin());
        getRootPane().setDefaultButton(BtnLogin);
        
        SwingUtilities.invokeLater(() -> TxtUser.requestFocusInWindow());
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
}

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
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import java.io.File;

import Compartidas.Usuario;
import OS.Archivos.SistemaArchivo;
import OS.Core.SistemaOperativo;
import OS.Core.SesionActual;
import OS.UI.util.FullscreenHelper;
import OS.UI.util.IconTile;
import OS.UI.util.TemaOscuro;
import OS.UI.util.BarraTareas;
import OS.UI.util.BotonesBarra;
import OS.UI.util.GradientWallpaper;

public class Escritorio extends JFrame {
    
    private final SistemaOperativo SO;
    private final JPanel Grid;
    
    public Escritorio(SistemaOperativo SO) {
        this.SO = SO;
        
        setTitle("Mini-Windows - Escritorio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));
        getContentPane().setBackground(TemaOscuro.BG);
        
        AplicarLook();
        WireF11();
        
        GradientWallpaper fondo = new GradientWallpaper();
        fondo.setGradient(Color.MAGENTA.darker().darker().darker(), Color.BLACK);
        fondo.setLayout(new BorderLayout());
        
        JPanel centro = new JPanel();
        centro.setLayout(new GridBagLayout());
        centro.setOpaque(false);
        
        Grid = new JPanel();
        Grid.setLayout(new GridLayout(2, 3, 18, 18));
        Grid.setOpaque(false);
        Grid.setBorder(new EmptyBorder(28, 28, 28, 28));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        centro.add(Grid, gbc);
        fondo.add(centro, BorderLayout.CENTER);
        
        //2 filas x 3 columnas
        addTile("Archivos", "icons/folder.png", () -> new NavegadorArchivos().setVisible(true));
        addTile("Consola", "icons/terminal.png", () -> new Consola().setVisible(true));
        addTile("Editor de Texto", "icons/notepad.png", () -> new EditorTexto().setVisible(true));
        
        addTile("Fotos", "icons/photos.png", this::AbrirFoto);
        addTile("Intagran", "icons/instagram.png", () -> JOptionPane.showMessageDialog(this, "No he conectado el insta aun :/"));
        addTile("Musica", "icons/music.png", () -> new ReproductorMusica().setVisible(true));
        
        BarraTareas taskbar = new BarraTareas();
        taskbar.AnadirApp(new BotonesBarra("Archivos", "icons/folder.png", () -> new NavegadorArchivos().setVisible(true)));
        taskbar.AnadirApp(new BotonesBarra("Consola", "icons/terminal.png", () -> new Consola().setVisible(true)));
        taskbar.AnadirApp(new BotonesBarra("Editor de Texto", "icons/notepad.png", () -> new EditorTexto().setVisible(true)));
        taskbar.AnadirApp(new BotonesBarra("Fotos", "icons/photos.png", () -> AbrirFoto()));
        taskbar.AnadirApp(new BotonesBarra("Intagran", "icons/instagram.png", () -> JOptionPane.showMessageDialog(this, "No he conectado el insta aun :/")));
        taskbar.AnadirApp(new BotonesBarra("Musica", "icons/music.png", () -> new ReproductorMusica().setVisible(true)));
        
        Usuario usu = SesionActual.getUsuario();        
        boolean esadmin = (usu != null && usu.isAdministrador());
        
        if (esadmin) {
            taskbar.AnadirApp(new BotonesBarra("Usuarios", "icons/users.png", () -> new UsuariosUI(SesionActual.getUsuario()).setVisible(true)));
        }
        
        taskbar.setUsuario("Usuario: " + (usu != null ? usu.getUsuario() : "-") + (esadmin ? " (admin)" : ""));
        
        taskbar.setBotonCentro(new BotonesBarra("Cerrar Sesion", "icons/logout.png", this::CerrarSesion));
        
        fondo.add(taskbar, BorderLayout.SOUTH);
        add(fondo, BorderLayout.CENTER);
    }
    
    private void addTile(String titulo, String iconpath, Runnable action) {
        IconTile tile = new IconTile(titulo, iconpath, action);
        Grid.add(tile);
    }
    
    private void WireF11() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleFullscreen");
        getRootPane().getActionMap().put("toggleFullscreen", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FullscreenHelper.toggle(Escritorio.this);
            }
        });
    }
    
    private void AbrirFoto() {
        JFileChooser fc = new JFileChooser(SistemaArchivo.getRutaUsuario());
        fc.setDialogTitle("Abrir Imagen");
        int r = fc.showOpenDialog(this);
        
        if (r != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        File file = fc.getSelectedFile();
        String nombre = file.getName().toLowerCase();
        
        if (!(nombre.endsWith(".png") || nombre.endsWith(".jpg") || nombre.endsWith(".jpeg"))) {
            JOptionPane.showMessageDialog(this, "Seleciona una imagen (.png/.jpg/.jpeg)");
            return;
        }
        
        new VisorImagenes(file).setVisible(true);
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

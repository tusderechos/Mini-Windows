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
import java.io.File;

import OS.apps.editor.ArchivoTexto;
import OS.apps.editor.EditorTextoCore;
import OS.Archivos.SistemaArchivo;
import OS.UI.util.TemaOscuro;

public class EditorTexto extends JFrame {
    
    private final JTextArea Texto = new JTextArea();
    private File ArchivoActual = null;
    
    public EditorTexto() {
        setTitle("Mini-Windows - Editor de Texto");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 540);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(640, 420));
        
        getContentPane().setBackground(TemaOscuro.BG);
        
        Texto.setFont(new Font("Consolas", Font.PLAIN, 14));
        Texto.setBackground(TemaOscuro.CARD);
        Texto.setForeground(TemaOscuro.TEXTO);
        Texto.setCaretColor(TemaOscuro.TEXTO);
        
        JScrollPane scroll = new JScrollPane(Texto);
        scroll.getViewport().setBackground(TemaOscuro.BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        add(scroll, BorderLayout.CENTER);
        
        JMenuBar MB = new JMenuBar();
        MB.setBackground(TemaOscuro.BAR);
        MB.setBorder(BorderFactory.createEmptyBorder());
        MB.setOpaque(false);
        
        JMenu MArchivo = new JMenu("Archivo");
        MArchivo.setForeground(TemaOscuro.TEXTO);
        MArchivo.setBackground(TemaOscuro.BAR);
        
        JMenuItem MiNuevo = MenuItemStyle("Nuevo");
        JMenuItem MiAbrir = MenuItemStyle("Abrir .txt");
        JMenuItem MiGuardar = MenuItemStyle("Guardar");
        JMenuItem MiGuardarComo = MenuItemStyle("Guardar como...");
        
        MArchivo.add(MiNuevo);
        MArchivo.add(MiAbrir);
        
        MArchivo.addSeparator();
        
        MArchivo.add(MiGuardar);
        MArchivo.add(MiGuardarComo);
        
        MB.add(MArchivo);
        setJMenuBar(MB);
        
        MiNuevo.addActionListener(e -> Nuevo());
        MiAbrir.addActionListener(e -> Abrir());
        MiGuardar.addActionListener(e -> Guardar());
        MiGuardarComo.addActionListener(e -> GuardarComo());
    }
    
    private void Nuevo() {
        Texto.setText("");
        ArchivoActual = null;
        setTitle("Mini-Windows - Editor de Texto");
    }
    
    private void Abrir() {
        JFileChooser fc = new JFileChooser(SistemaArchivo.getRutaUsuario());
        int r = fc.showOpenDialog(this);
        
        if (r != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        File file = fc.getSelectedFile();
        ArchivoTexto at = EditorTextoCore.AbrirTxt(file.getAbsolutePath(), file.getName());
        
        if (at == null) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir");
            return;
        }
        
        Texto.setText(at.getContenido());
        ArchivoActual = file;
        setTitle("Editor - " + file.getName());
    }
    
    private void Guardar() {
        if (ArchivoActual == null) {
            GuardarComo();
            return;
        }
        
        ArchivoTexto at = new ArchivoTexto(ArchivoActual.getName(), ArchivoActual.getAbsolutePath());
        at.setContenido(Texto.getText());
        
        if (!EditorTextoCore.GuardarComoTxt(at)) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar");
        }
    }
    
    private void GuardarComo() {
        JFileChooser fc = new JFileChooser(SistemaArchivo.getRutaUsuario());
        int r = fc.showSaveDialog(this);
        
        if (r != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        File file = fc.getSelectedFile();
        
        if (!file.getName().toLowerCase().endsWith(".txt")) {
            file = new File(file.getParentFile(), file.getName() + ".txt");
        }
        
        ArchivoTexto at = new ArchivoTexto(file.getName(), file.getAbsolutePath());
        at.setContenido(Texto.getText());
        
        if (EditorTextoCore.GuardarComoTxt(at)) {
            ArchivoActual = file;
            setTitle("Editor = " + file.getName());
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar");
        }
    }
    
    private JMenuItem MenuItemStyle(String texto) {
        JMenuItem item = new JMenuItem(texto);
        item.setForeground(TemaOscuro.TEXTO);
        item.setBackground(TemaOscuro.BAR);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        return item;
    }
}

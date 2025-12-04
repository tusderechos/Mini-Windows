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
import java.util.function.Consumer;
import javax.swing.text.SimpleAttributeSet;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import javax.swing.text.rtf.RTFEditorKit;

import OS.apps.editor.ArchivoTexto;
import OS.apps.editor.EditorTextoCore;
import OS.Archivos.SistemaArchivo;
import OS.UI.util.TemaOscuro;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



public class EditorTexto extends JFrame {
    
    private final JTextPane Texto = new JTextPane();
    private File ArchivoActual = null;
    private boolean Sucio = false;
    
    public EditorTexto() {
        setTitle("Mini-Windows - Editor de Texto");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 540);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(640, 420));
        
        getContentPane().setBackground(TemaOscuro.BG);
        
        Texto.setFont(new Font("Consolas", Font.PLAIN, 14));
        Texto.setBackground(Color.WHITE);
        Texto.setForeground(Color.BLACK);
        Texto.setCaretColor(TemaOscuro.TEXTO);
        Texto.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        JScrollPane scroll = new JScrollPane(Texto);
        scroll.getViewport().setBackground(TemaOscuro.BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        add(scroll, BorderLayout.CENTER);
        
        JPanel barra = new JPanel();
        barra.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        barra.setBackground(TemaOscuro.BAR);
        
        String[] fuentes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        JComboBox<String> CBFuente = new JComboBox<>(fuentes);
        CBFuente.setSelectedItem("Segoe UI");
        stydark(CBFuente);
        
        Integer[] tamanos = {10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 32};
        JComboBox<Integer> CBTamano = new JComboBox<>(tamanos);
        CBTamano.setSelectedItem(14);
        stydark(CBTamano);
        
        JButton BtnColor = BotonStyle("Color");
        JToggleButton BtnBold = ToggleStyle("B");
        JToggleButton BtnItalica = ToggleStyle("I");
        
        barra.add(new JLabel("  Fuente:")).setForeground(TemaOscuro.TEXTO);
        barra.add(CBFuente);
        barra.add(new JLabel("  Tamaño:")).setForeground(TemaOscuro.TEXTO);
        barra.add(CBTamano);
        barra.add(BtnColor);
        barra.add(BtnBold);
        barra.add(BtnItalica);
        
        add(barra, BorderLayout.NORTH);
        
        CBFuente.addActionListener(e -> AplicarFuente((String) CBFuente.getSelectedItem()));
        CBTamano.addActionListener(e -> AplicarTamano((Integer) CBTamano.getSelectedItem()));
        BtnColor.addActionListener(e -> ElegirColor());
        BtnBold.addActionListener(e -> AplicarBold(BtnBold.isSelected()));
        BtnItalica.addActionListener(e -> AplicarItalica(BtnItalica.isSelected()));
        
        JMenuBar MB = new JMenuBar();
        MB.setBackground(TemaOscuro.BAR);
        MB.setBorder(BorderFactory.createEmptyBorder());
        MB.setOpaque(true);
        
        JMenu MArchivo = new JMenu("Archivo");
        MArchivo.setForeground(TemaOscuro.TEXTO);
        MArchivo.setBackground(TemaOscuro.BAR);
        
        JMenuItem MiNuevo = MenuItemStyle("Nuevo");
        JMenuItem MiAbrir = MenuItemStyle("Abrir .txt");
        JMenuItem MiGuardar = MenuItemStyle("Guardar");
        JMenuItem MiGuardarComo = MenuItemStyle("Guardar como...");
        JMenuItem MiGuardarRTF = MenuItemStyle("Guardar como RTF...");
                
        MArchivo.add(MiNuevo);
        MArchivo.add(MiAbrir);
        
        MArchivo.addSeparator();
        
        MArchivo.add(MiGuardar);
        MArchivo.add(MiGuardarComo);
        MArchivo.add(MiGuardarRTF);
        
        MB.add(MArchivo);
        setJMenuBar(MB);
        
        MiNuevo.addActionListener(e -> Nuevo());
        MiAbrir.addActionListener(e -> Abrir());
        
        MiGuardar.addActionListener(e -> Guardar());
        MiGuardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        
        MiGuardarComo.addActionListener(e -> GuardarComo());
        MiGuardarComo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        
        MiGuardarRTF.addActionListener(e -> GuardarComoRTF());
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (Sucio) {
                    int r = JOptionPane.showConfirmDialog(EditorTexto.this, "Hay cambios sin guardar.\nDeseas guardar?", "Salir", JOptionPane.YES_NO_CANCEL_OPTION);
                    
                    if (r == JOptionPane.CANCEL_OPTION) {
                        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    }
                    
                    if (r == JOptionPane.YES_OPTION) {
                        Guardar();
                    }
                }
            }
        });
        
        Texto.getDocument().addDocumentListener(new DocumentListener() {
            private void mark() {
                if (!Sucio) {
                    Sucio = true;
                    setTitle("* " + "Mini-Windows - Editor de Texto");
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                mark();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mark();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                mark();
            }
        });
    }
    
    public EditorTexto(File ArchivoActual) {
        this();
        CargarDesdeArchivo(ArchivoActual);
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
        try {
            if (ArchivoActual == null) {
                GuardarComo();
                return;
            }
            
            Files.writeString(ArchivoActual.toPath(), Texto.getText(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Sucio = false;
            setTitle("Mini-Windows - Editor de Texto - " + ArchivoActual.getName());
            System.out.println("[Editor] Guardado: " + ArchivoActual.getAbsolutePath() + " (" + Files.size(ArchivoActual.toPath()) + " bytes)");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void GuardarComo() {
        try {
            JFileChooser fc = new JFileChooser(SistemaArchivo.getRutaUsuario());
            fc.setDialogTitle("Guardar como .txt");

            if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = fc.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getParentFile(), file.getName() + ".txt");
            }
            
            if (!ConfirmarOverwrite(file)) {
                return;
            }

            //Escribir contenido en UTF-8
            Files.writeString(file.toPath(), Texto.getText(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            ArchivoActual = file;
            Sucio = false;
            setTitle("Mini-Windows - Editor de Texto - " + file.getName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
   
    private void GuardarComoRTF() {
        try {
            JFileChooser fc = new JFileChooser(SistemaArchivo.getRutaUsuario());
            fc.setDialogTitle("Guardar como RTF");

            if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = fc.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".rtf")) {
                file = new File(file.getParentFile(), file.getName() + ".rtf");
            }
            
            if (!ConfirmarOverwrite(file)) {
                return;
            }
            
            RTFEditorKit kit = new RTFEditorKit();
            
            try (FileOutputStream salida = new FileOutputStream(file)) {
                kit.write(salida, Texto.getDocument(), 0, Texto.getDocument().getLength());
            }
            
            ArchivoActual = file;
            Sucio = false;
            setTitle("Mini-Windows - Editor de Texto - " + file.getName());
        } catch (Exception e) {
        }
    }
    
    private boolean ConfirmarOverwrite(File file) {
        return !file.exists() || JOptionPane.showConfirmDialog(this, "El archivo ya existe.\nDeseas sobrescribirlo?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    private void AplicarFuente(String nombre) {
        AplicarEstilos(a -> StyleConstants.setFontFamily(a, nombre));
    }
    
    private void AplicarTamano(int tamano) {
        AplicarEstilos(a -> StyleConstants.setFontSize(a, tamano));
    }
    
    private void AplicarBold(boolean on) {
        AplicarEstilos(a -> StyleConstants.setBold(a, on));
    }
    
    private void AplicarItalica(boolean on) {
        AplicarEstilos(a -> StyleConstants.setItalic(a, on));
    }
    
    private void ElegirColor() {
        Color color = JColorChooser.showDialog(this, "Color del texto", Texto.getForeground());
        
        if (color != null) {
            AplicarEstilos(a -> StyleConstants.setForeground(a, color));
        }
    }
    
    public static String getExtension(String nombre) {
        int i = nombre.lastIndexOf('.');
        return (i > 0 && i < nombre.length() - 1) ? nombre.substring(i + 1).toLowerCase() : "";
    }
    
    private JMenuItem MenuItemStyle(String texto) {
        JMenuItem item = new JMenuItem(texto);
        item.setForeground(TemaOscuro.TEXTO);
        item.setBackground(TemaOscuro.BAR);
        item.setOpaque(true);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        return item;
    }
    
    private void stydark(JComponent component) {
        component.setBackground(TemaOscuro.CARD);
        component.setForeground(TemaOscuro.TEXTO);
        
        if (component instanceof JComponent jc) {
            jc.setBorder(BorderFactory.createLineBorder(TemaOscuro.LINEA));
        }
    }
    
    private JButton BotonStyle(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(TemaOscuro.CARD);
        boton.setForeground(TemaOscuro.TEXTO);
        boton.setBorder(BorderFactory.createLineBorder(TemaOscuro.LINEA));
        boton.setFocusPainted(false);
        
        return boton;
    }
    
    private JToggleButton ToggleStyle(String texto) {
        JToggleButton tb = new JToggleButton(texto);
        tb.setBackground(TemaOscuro.CARD);
        tb.setForeground(TemaOscuro.TEXTO);
        tb.setBorder(BorderFactory.createLineBorder(TemaOscuro.LINEA));
        tb.setFocusPainted(false);
        
        return tb;
    }
    
    private void AplicarEstilos(Consumer<SimpleAttributeSet> edit) {
        int inicio = Texto.getSelectionStart();
        int fin = Texto.getSelectionEnd();
        
        //Si no hay seleccion, aplica como estilo de entrada
        if (inicio == fin) {
            SimpleAttributeSet atributos = new SimpleAttributeSet(Texto.getInputAttributes());
            edit.accept(atributos);
            Texto.setCharacterAttributes(atributos, true);
            
            return;
        }
        
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        edit.accept(atributos);
        Texto.getStyledDocument().setCharacterAttributes(inicio, fin - inicio, atributos, false);
    }
    
    private void CargarDesdeArchivo(File file) {
        try {
            if (file == null || !file.exists() || !file.isFile()) {
                throw new IllegalArgumentException("Ruta invalida: " + (file == null ? "null" : file.getAbsolutePath()));
            }
                        
            String ext = getExtension(file.getName());
            Document doc = new DefaultStyledDocument();
            
            if (ext.equals("rtf")) {
                RTFEditorKit kit = new RTFEditorKit();
                try {
                    FileInputStream entrada = new FileInputStream(file);
                    kit.read(entrada, doc, 0);
                } catch (Throwable e) {
                }
            } else {
                doc.insertString(0, Files.readString(file.toPath(), StandardCharsets.UTF_8), null);
            }
            
            Texto.setDocument(doc);
            Texto.setCaretPosition(0);
            
            ArchivoActual = file;
            Sucio = false;
            setTitle("Mini-Windows - Editor de Texto - " + file.getName());
                        
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir:\n " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

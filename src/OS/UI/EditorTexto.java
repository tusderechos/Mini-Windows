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
import OS.UI.util.GradientWallpaper;
import OS.UI.util.TemaOscuro;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.EmptyBorder;
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
        
        GradientWallpaper barra = new GradientWallpaper();
        barra.setGradient(new Color(35, 35, 40), Color.MAGENTA.darker().darker());
        barra.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 70)));
        
        String[] fuentes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        JComboBox<String> CBFuente = new JComboBox<>(fuentes);
        CBFuente.setSelectedItem("Segoe UI");
        stydark(CBFuente);
        
        Integer[] tamanos = {10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 32};
        JComboBox<Integer> CBTamano = new JComboBox<>(tamanos);
        CBTamano.setSelectedItem(14);
        stydark(CBTamano);
        
        JButton BtnColor = CrearBoton("Color", true, 18);
        JToggleButton BtnBold = CrearToggle("B", 14);
        JToggleButton BtnItalica = CrearToggle("I", 14);
        
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
        MiGuardarRTF.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        
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
        boton.setFont(boton.getFont().deriveFont(Font.BOLD, 14f));
        boton.setBorder(new EmptyBorder(6, 16, 6, 16));
        
        return boton;
    }
    
    private JToggleButton CrearToggle(String texto, int radio) {
        //Colores base
        final Color BG_BASE = new Color(44, 44, 50);
        final Color BG_HOVER = new Color(60, 60, 68);
        final Color BG_PRESS = new Color(24, 24, 28);
        final Color BG_ON = new Color(110, 50, 150);
        final Color FG_BASE = new Color(230, 230, 230);
        final Color FG_ON = Color.WHITE;

        JToggleButton t = new JToggleButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                int w = getWidth(), h = getHeight();
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                ButtonModel m = getModel();
                boolean seleccion = isSelected();
                Color fill = !isEnabled() ? BG_BASE.darker().darker() : m.isPressed() ? (seleccion ? BG_ON.darker() : BG_PRESS) : m.isRollover() ? (seleccion ? BG_ON.brighter() : BG_HOVER) : (seleccion ? BG_ON : BG_BASE);
                Color fg = seleccion ? FG_ON : FG_BASE;

                Shape rr = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, radio, radio);

                //sombra
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fill(new RoundRectangle2D.Float(2, 3, w - 4, h - 5, radio + 2, radio + 2));

                //relleno
                g2.setColor(fill);
                g2.fill(rr);

                //borde leve
                g2.setColor(new Color(0, 0, 0, 40));
                g2.draw(new RoundRectangle2D.Float(2, 3, w - 1, h - 1, radio, radio));

                //contenido
                g2.setClip(rr);
                setForeground(fg);
                super.paintComponent(g2);
                g2.dispose();
            }
            
            @Override
            public boolean contains(int x, int y) {
                Shape rr = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
                return rr.contains(x, y);
            }
        };

        t.setContentAreaFilled(false);
        t.setOpaque(false);
        t.setBorderPainted(false);
        t.setFocusPainted(false);
        t.setRolloverEnabled(true);
        t.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        t.setFont(t.getFont().deriveFont(Font.BOLD, 12f));
        t.setBorder(new EmptyBorder(4, 12, 4, 12));
        return t;
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

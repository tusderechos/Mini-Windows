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
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.TreePath;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import OS.Archivos.ResultadoListado;
import OS.Archivos.SistemaArchivo;
import OS.UI.util.TemaOscuro;
import OS.UI.util.NodoArchivo;
import OS.UI.util.RendererTreeOscuro;
import OS.Archivos.Archivo;
import OS.Archivos.Carpeta;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

public class NavegadorArchivos extends JFrame {
    
    private JTree Arbol;
    private DefaultTreeModel ModeloArbol;
    private JSplitPane Split;
    
    private static final String F_DOCS = "Documentos";
    private static final String F_INGS = "Imagenes";
    private static final String F_AUDIO = "Musica";
    
    private String DirActual;
    private final JLabel LblRuta = new JLabel();
    
    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Nombre", "Tipo", "Tamaño", "Modificado"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch(columnIndex) {
                case 0 -> String.class; //Nombre
                case 1 -> String.class; //Tipo
                case 2 -> Long.class; //Tamaño en bytes 
                case 3 -> Long.class; //Fecha lastmodified
                default -> Object.class;
            };
        }
    };
    
    private final JTable tabla;
    private TableRowSorter Sorter;
    private final DefaultTableCellRenderer daterenderer;
    private final DefaultTableCellRenderer sizerenderer;
    
    public NavegadorArchivos() {
        DirActual = SistemaArchivo.getRutaUsuario();
        
        setTitle("Mini-Windows - Archivos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(760, 420));
        setLayout(new BorderLayout(0, 4));
        
        getContentPane().setBackground(TemaOscuro.BG);
        
        //Barra arriba
        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.setBackground(TemaOscuro.BG);
        
        JButton BtnUp = BotonStyle("Arriba");
        JButton BtnAbrir = BotonStyle("Abrir");
        JButton BtnMkdir = BotonStyle("Nueva carpeta");
        JButton BtnMktxt = BotonStyle("Nuevo .txt");
        JButton BtnRenombrar = BotonStyle("Renombrar");
        JButton BtnBorrar = BotonStyle("Eliminar");
        JButton BtnCopiar = BotonStyle("Copiar a...");
        JButton BtnMover = BotonStyle("Mover a...");
        JButton BtnOrganizar = BotonStyle("Organizar por tipo");
        JButton BtnRefrescar = BotonStyle("Refrescar");
        
        top.add(BtnUp);
        top.add(BtnAbrir);
        top.add(BtnMkdir);
        top.add(BtnMktxt);
        top.add(BtnRenombrar);
        top.add(BtnBorrar);
        top.add(BtnCopiar);
        top.add(BtnMover);
        top.add(BtnOrganizar);
        top.add(BtnRefrescar);
        
        tabla = new JTable(modelo);
        tabla.setBackground(TemaOscuro.BG);
        tabla.setForeground(TemaOscuro.TEXTO);
        tabla.setGridColor(TemaOscuro.LINEA);
        tabla.setSelectionBackground(TemaOscuro.CARD);
        tabla.setSelectionForeground(TemaOscuro.TEXTO);
        tabla.setRowHeight(22);
        tabla.setFillsViewportHeight(true);
        
        sizerenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Long length) {
                    setText(HumanSize(length));
                } else {
                    setText("");
                }
            }
        };
        sizerenderer.setOpaque(false);
        
        daterenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Long time) {
                    String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(time));
                    setText(fecha);
                } else {
                    setText("");
                }
            }
        };
        daterenderer.setOpaque(false);
        
        Sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(Sorter);
        
        Sorter.setComparator(1, (a, b) -> {
            String s1 = (String) a;
            String s2 = (String) b;
            boolean d1 = s1.equalsIgnoreCase("<DIR>");
            boolean d2 = s2.equalsIgnoreCase("<DIR>");
            
            if (d1 && !d2) {
                return -1;
            }
            if (!d1 && d2) {
                return 1;
            }
            
            return s1.compareToIgnoreCase(s2);
        });
        
        JScrollPane scrolltabla = new JScrollPane(tabla);
        scrolltabla.getViewport().setBackground(TemaOscuro.BG);
        scrolltabla.setBorder(BorderFactory.createEmptyBorder());
        
        tabla.getColumnModel().getColumn(2).setCellRenderer(sizerenderer);
        tabla.getColumnModel().getColumn(3).setCellRenderer(daterenderer);
        
        File raiz = new File(SistemaArchivo.getRutaUsuario());
        NodoArchivo nodoruta = new NodoArchivo(raiz);
        ModeloArbol = new DefaultTreeModel(nodoruta);
        Arbol = new JTree(ModeloArbol);
        Arbol.setRootVisible(true);
        Arbol.setShowsRootHandles(true);
        Arbol.setBackground(TemaOscuro.BG);
        Arbol.setRowHeight(22);
        Arbol.setCellRenderer(new RendererTreeOscuro());
        
        CargarHijos(nodoruta);
        
        Arbol.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) {
                NodoArchivo nodo = (NodoArchivo) event.getPath().getLastPathComponent();
                
                if (!nodo.cargado) {
                    CargarHijos(nodo);
                }
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) {
            }
        });
        
        Arbol.addTreeSelectionListener((TreeSelectionListener) e -> {
            NodoArchivo nodo = (NodoArchivo) Arbol.getLastSelectedPathComponent();
            
            if (nodo != null) {
                DirActual = nodo.file.getAbsolutePath();
                Refrescar();
            }
        });
        
        JScrollPane scrollarbol = new JScrollPane(Arbol);
        scrollarbol.getViewport().setBackground(TemaOscuro.BG);
        scrollarbol.setBorder(BorderFactory.createEmptyBorder());
        
        Split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollarbol, scrolltabla);
        Split.setDividerSize(6);
        Split.setDividerLocation(260);
        Split.setBorder(BorderFactory.createEmptyBorder());
        
        JPanel barraruta = new JPanel();
        barraruta.setLayout(new BorderLayout());
        barraruta.setBackground(TemaOscuro.BAR);
        barraruta.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        LblRuta.setForeground(TemaOscuro.SUTIL);
        barraruta.add(LblRuta, BorderLayout.CENTER);
                
        add(top, BorderLayout.NORTH);
        add(Split, BorderLayout.CENTER);
        add(barraruta, BorderLayout.SOUTH);
        
        BtnUp.addActionListener(e -> GoUp());
        BtnAbrir.addActionListener(e -> AbrirSeleccion());
        BtnMkdir.addActionListener(e -> CrearCarpeta());
        BtnMktxt.addActionListener(e -> CrearTxt());
        BtnRenombrar.addActionListener(e -> Renombrar());
        BtnBorrar.addActionListener(e -> Borrar());
        BtnCopiar.addActionListener(e -> CopiarMover(true));
        BtnMover.addActionListener(e -> CopiarMover(false));
        BtnOrganizar.addActionListener(e -> {
            try {
                SistemaArchivo.OrganizarPorTipo(DirActual);
                Refrescar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al organizar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        BtnRefrescar.addActionListener(e -> Refrescar());
        
        Arbol.setSelectionPath(new TreePath(nodoruta.getPath()));
        Refrescar();
    }
    
    private void GoUp() {
        File actual = new File(DirActual);
        File padre = actual.getParentFile();
        
        if (padre == null) {
            return;
        }
        
        String ruta = SistemaArchivo.getRutaUsuario().replace("\\", File.separator).replace("/", File.separator);
        String siguiente = padre.getAbsolutePath();
        
        if (!siguiente.startsWith(ruta)) {
            DirActual = ruta;
        } else {
            DirActual = siguiente;
        }
        
        Refrescar();
    }
    
    private void AbrirSeleccion() {
        int fila = tabla.getSelectedRow();
        
        if (fila < 0) {
            return;
        }
        
        String nombre = (String) modelo.getValueAt(fila, 0);
        String tipo = (String) modelo.getValueAt(fila, 1);
        File destino = new File(DirActual, nombre);
        
        if (tipo.equals("<DIR>")) {
            
            if (destino.exists() && destino.isDirectory()) {
                DirActual = destino.getAbsolutePath();
                Refrescar();
            }
        } else {
            String lower = destino.getName().toLowerCase();
            
            if (lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
                new VisorImagenes(destino).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No hay aplicacion para abrir este archivo");
            }
        }
    }
    
    private void CrearCarpeta() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la carpeta:");
        
        if (nombre == null || nombre.isBlank()) {
            return;
        }
        
        boolean ok = SistemaArchivo.CrearCarpeta(DirActual, nombre.trim());
        
        if (!ok) {
            JOptionPane.showMessageDialog(this, "No se pudo crear");
        }
        
        Refrescar();
    }
    
    private void CrearTxt() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del archivo(sin extension):");
        
        if (nombre == null || nombre.isBlank()) {
            return;
        }
        
        try {
            boolean ok = SistemaArchivo.CrearArchivo(DirActual, nombre.trim(), "txt");
            
            if (!ok) {
                JOptionPane.showMessageDialog(this, "No se pudo crear");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
        
        Refrescar();
    }
    
    private void Renombrar() {
        int fila = tabla.getSelectedRow();
        
        if (fila < 0) {
            return;
        }
        
        String nombre = (String) modelo.getValueAt(fila, 0);
        String nuevo = JOptionPane.showInputDialog(this, "Nuevo Nombre:", nombre);
        
        if (nuevo == null || nuevo.isBlank()) {
            return;
        }
        
        boolean ok = SistemaArchivo.Renombrar(new File(DirActual, nombre).getAbsolutePath(), nuevo.trim());
        
        if (!ok) {
            JOptionPane.showMessageDialog(this, "No se pudo renombrar");
        }
        
        Refrescar();
    }
    
    private void Borrar() {
        int fila = tabla.getSelectedRow();
        
        if (fila < 0) {
            return;
        }
        
        String nombre = (String) modelo.getValueAt(fila, 0);
        File objetivo = new File(DirActual, nombre);
        int r = JOptionPane.showConfirmDialog(this, "Eliminar " + nombre + "?", "Confirmacion", JOptionPane.YES_NO_OPTION);
        
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        
        boolean ok = SistemaArchivo.Eliminar(objetivo.getAbsolutePath());
        
        if (!ok) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar");
        }
        
        Refrescar();
    }
    
    private void CopiarMover(boolean copiar) {
        int fila = tabla.getSelectedRow();
        
        if (fila < 0) {
            return;
        }
        
        String nombre = (String) modelo.getValueAt(fila, 0);
        File origen = new File(DirActual, nombre);
        String destino = JOptionPane.showInputDialog(this, "Carpeta destino:", DirActual);
        
        if (destino == null || destino.isBlank()) {
            return;
        }
        
        //Resolver el destino relativo al DirActual
        String separador = File.separator;
        String ruta = SistemaArchivo.getRutaUsuario().replace("\\", separador).replace("/", separador);
        String dentro = destino.replace("\\", separador).replace("/", separador);
        String destinoresuelto = dentro.startsWith(ruta) ? dentro : (dentro.startsWith(separador) ? ruta + dentro : DirActual + separador + dentro);
        
        try {
            boolean ok = copiar ? SistemaArchivo.Copiar(origen.getAbsolutePath(), destinoresuelto) : SistemaArchivo.Mover(origen.getAbsolutePath(), destinoresuelto);
            
            if (!ok) {
                JOptionPane.showMessageDialog(this, (copiar ? "No se pudo copiar" : "No se pudo mover"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
        
        Refrescar();
    }
    
    private void Refrescar() {
        File dir = new File(DirActual);
        
        modelo.setRowCount(0);
        
        File[] listado = dir.listFiles();
        
        if (listado == null) {
            return;
        }
        
        for (File file : listado) {
            final String nombre = file.getName();
            final boolean esdir = file.isDirectory();
            final String tipo = esdir ? "<DIR>" : getExtension(nombre);
            final long tamano = esdir ? 0L : file.length();
            final long mod = file.lastModified();

            modelo.addRow(new Object[]{nombre, tipo, tamano, mod});
        }
        
        LblRuta.setText(DirActual);
        ResultadoListado rl = SistemaArchivo.ListarContenido(DirActual);
        
        //Carpetas primero
        for (Carpeta carpeta : rl.getCarpetas()) {
            File file = new File(carpeta.getRutaAbsoluta());
            modelo.addRow(new Object[]{ file.getName(), "<DIR>", "" });
        }
        
        //Archivos
        for (Archivo archivo : rl.getArchivos()) {
            File file = new File(archivo.getRutaAbsoluta());
            modelo.addRow(new Object[]{ file.getName(), "Archivo", file.length() });
        }
    }
    
    private void CargarHijos(NodoArchivo padre) {
        padre.removeAllChildren();
        File[] dirs = padre.file.listFiles(File::isDirectory);
        
        if (dirs != null) {
            for (int i = 0; i < dirs.length; i++) {
                int min = i;
                for (int j = i + 1; j < dirs.length; j++) {
                    String a = dirs[j].getName().toLowerCase();
                    String b = dirs[min].getName().toLowerCase();
                    
                    if (a.compareTo(b) < 0) {
                        min = j;
                    }
                }
                
                if (min != i) {
                    File temp = dirs[i];
                    dirs[i] = dirs[min];
                    dirs[min] = temp;
                }
            }
            
            for (File file : dirs) {
                padre.add(new NodoArchivo(file));
            }
        }
        
        padre.cargado = true;
        ModeloArbol.reload(padre);
    }
    
    public static String getExtension(String nombre) {
        if (nombre == null) {
            return "";
        }
        
        int punto = nombre.lastIndexOf('.');
        
        if (punto <= 0 || punto == nombre.length() - 1) {
            return ""; //Por si no hay extension
        }
        
        return nombre.substring(punto + 1).toLowerCase(); //Sin el punto
    }
    
    private String HumanSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        
        return String.format(Locale.US, "%.1f %sB", bytes / Math.pow(1024, exp), pre);
}
    
    private JButton BotonStyle(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(TemaOscuro.CARD);
        boton.setForeground(TemaOscuro.TEXTO);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(new LineBorder(TemaOscuro.LINEA), new EmptyBorder(6, 12, 6, 12)));
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        return boton;
    }
}

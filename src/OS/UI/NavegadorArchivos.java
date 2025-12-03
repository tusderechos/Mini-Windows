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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;

public class NavegadorArchivos extends JFrame {
    
    private final String RUTA;
    
    private JTree Arbol;
    private DefaultTreeModel ModeloArbol;
    private JSplitPane Split;
    
    private JTextField TxtRuta;
    private JPopupMenu MenuTabla;
    
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
        RUTA = Normalizar(SistemaArchivo.getRutaUsuario());
        DirActual = RUTA;
        
        setTitle("Mini-Windows - Archivos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(760, 420));
        setLayout(new BorderLayout(0, 4));
        
        getContentPane().setBackground(TemaOscuro.BG);
        
        //Barra arriba
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout(8, 8));
        top.setBackground(TemaOscuro.BAR);
        
        JPanel acciones = new JPanel();
        acciones.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        acciones.setBackground(TemaOscuro.BAR);
        
        JButton BtnUp = BotonStyle("Arriba");
        JButton BtnRefrescar = BotonStyle("Refrescar");
        
        acciones.add(BtnUp);
        acciones.add(BtnRefrescar);
        
        TxtRuta = new JTextField(DirActual);
        TxtRuta.setBackground(TemaOscuro.CARD);
        TxtRuta.setForeground(TemaOscuro.TEXTO);
        TxtRuta.setCaretColor(TemaOscuro.TEXTO);
        TxtRuta.setBorder(BorderFactory.createLineBorder(TemaOscuro.LINEA));
        
        top.add(acciones, BorderLayout.WEST);
        top.add(TxtRuta, BorderLayout.CENTER);
        
        add(top, BorderLayout.NORTH);
        
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
        
        tabla = new JTable(modelo);
        tabla.setBackground(TemaOscuro.BG);
        tabla.setForeground(TemaOscuro.TEXTO);
        tabla.setGridColor(TemaOscuro.LINEA);
        tabla.setSelectionBackground(TemaOscuro.CARD);
        tabla.setSelectionForeground(TemaOscuro.TEXTO);
        tabla.setRowHeight(22);
        tabla.setFillsViewportHeight(true);
        
        JScrollPane scrolltabla = new JScrollPane(tabla);
        scrolltabla.getViewport().setBackground(TemaOscuro.BG);
        scrolltabla.setBorder(BorderFactory.createEmptyBorder());
        
        tabla.getColumnModel().getColumn(2).setCellRenderer(sizerenderer);
        tabla.getColumnModel().getColumn(3).setCellRenderer(daterenderer);
        
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    AbrirSeleccion();
                }
            }
        });
        
        tabla.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {    
                    AbrirSeleccion();
                    e.consume();
                }
            }
        });
        
        MenuTabla = new JPopupMenu();
        JMenuItem MiAbrir = new JMenuItem("Abrir");
        JMenuItem MiRenombrar = new JMenuItem("Renombrar");
        JMenuItem MiMover = new JMenuItem("Mover a...");
        JMenuItem MiCopiar = new JMenuItem("Copiar a...");
        JMenuItem MiEliminar = new JMenuItem("Eliminar");
        JMenuItem MiNuevoTxt = new JMenuItem("Nuevo .txt");
        JMenuItem MiNuevaCarpeta = new JMenuItem("Nueva Carpeta");
        
        MenuTabla.add(MiAbrir);
        MenuTabla.add(MiRenombrar);
        MenuTabla.add(MiMover);
        MenuTabla.add(MiCopiar);
        MenuTabla.add(MiEliminar);
        MenuTabla.add(MiNuevoTxt);
        MenuTabla.add(MiNuevaCarpeta);
        
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                show(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                show(e);
            }
            
            private void show(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int r = tabla.rowAtPoint(e.getPoint());
                    
                    if (r >= 0) {
                        tabla.setRowSelectionInterval(r, r);
                    }
                    
                    MenuTabla.show(tabla, e.getX(), e.getY());
                }
            }
        });
        
        MiAbrir.addActionListener(e -> AbrirSeleccion());
        MiRenombrar.addActionListener(e -> Renombrar());
        MiMover.addActionListener(e -> CopiarMover(false));
        MiCopiar.addActionListener(e -> CopiarMover(true));
        MiEliminar.addActionListener(e -> Borrar());
        MiNuevoTxt.addActionListener(e -> CrearTxt());
        MiNuevaCarpeta.addActionListener(e -> CrearCarpeta());
        
        File raiz = new File(SistemaArchivo.getRutaUsuario());
        NodoArchivo nodoruta = new NodoArchivo(raiz);
        
        ModeloArbol = new DefaultTreeModel(nodoruta);
        Arbol = new JTree(ModeloArbol);
        
        Arbol.setBackground(TemaOscuro.BG);
        Arbol.setRootVisible(true);
        Arbol.setRowHeight(22);
        Arbol.setShowsRootHandles(true);
        Arbol.setCellRenderer(new RendererTreeOscuro());
        
        CargarHijos(nodoruta);
        
        //Expandir al abrir nodo
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
        
        //Seleccion = navegar
        Arbol.addTreeSelectionListener(e -> {
            NodoArchivo nodo = (NodoArchivo) Arbol.getLastSelectedPathComponent();
            
            if (nodo != null) {
                Navegar(nodo.file.getAbsolutePath());
            }
        });
        
        //Doble click = navegar
        Arbol.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TreePath path = Arbol.getClosestPathForLocation(e.getX(), e.getY());
                    
                    if (path != null) {
                        NodoArchivo nodo = (NodoArchivo) path.getLastPathComponent();
                        
                        if (nodo.file.isDirectory()) {
                            Navegar(nodo.file.getAbsolutePath());
                        }
                    }
                }
            }
        });
        
        JScrollPane scrollarbol = new JScrollPane(Arbol);
        scrollarbol.getViewport().setBackground(TemaOscuro.BG);
        scrollarbol.setBorder(BorderFactory.createEmptyBorder());
        
        Split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollarbol, scrolltabla);
        Split.setDividerSize(6);
        Split.setDividerLocation(260);
        Split.setBorder(BorderFactory.createEmptyBorder());
        
        add(Split, BorderLayout.CENTER);
        
        JPanel barraruta = new JPanel();
        barraruta.setLayout(new BorderLayout());
        barraruta.setBackground(TemaOscuro.BAR);
        barraruta.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        LblRuta.setForeground(TemaOscuro.SUTIL);
        barraruta.add(LblRuta, BorderLayout.CENTER);
                
        add(barraruta, BorderLayout.SOUTH);
        
        BtnUp.addActionListener(e -> GoUp());
        BtnRefrescar.addActionListener(e -> Refrescar());
        
        TxtRuta.addActionListener(e -> {
            String entrada = TxtRuta.getText().trim();
            
            if (entrada.isEmpty()) {
                return;
            }
            
            File file = entrada.startsWith(File.separator) ? new File(RUTA + entrada) : new File(entrada);
            String destino = ClampToRoot(file.getAbsolutePath());
            
            if (new File(destino).isDirectory()) {
                Navegar(file.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(this, "Ruta invalida");
                TxtRuta.setText(DirActual);
            }
        });
        
        Arbol.setSelectionPath(new TreePath(nodoruta.getPath()));
        Refrescar();
        SeleccionarNodoEnArbol(DirActual);
    }
    
    private void GoUp() {
        File actual = new File(DirActual);
        File padre = actual.getParentFile();
        
        if (padre == null) {
            return;
        }
        
        String siguiente = ClampToRoot(padre.getAbsolutePath());
        
        Navegar(siguiente);
    }
    
    private void AbrirSeleccion() {
        int filavista = tabla.getSelectedRow();
        
        if (filavista < 0) {
            return;
        }
        
        AbrirItemDobleClick(filavista);
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
        int filavista = tabla.getSelectedRow();
        
        if (filavista < 0) {
            return;
        }
        
        //Converir indice si hay orter
        int fila = tabla.convertRowIndexToModel(filavista); 
        
        String nombre = (String) modelo.getValueAt(fila, 0);
        File origen = new File(DirActual, nombre);
        
        String input = JOptionPane.showInputDialog(this, "Carpeta destino:", DirActual);

        if (input == null || input.isBlank()) {
            return;
        }
        
        //Resolver destino
        String separador = File.separator;
        String destinoraw;
        
        if (input.startsWith("\\") || input.startsWith("/")) {
            destinoraw = RUTA + input; //Relativo a raiz
        } else if (new File(input).isAbsolute()) {
            destinoraw = input; //Absoluto
        } else {
            destinoraw = DirActual + separador + input; //Relativo al dir actual
        }
        
        String destinoresuelto = ClampToRoot(destinoraw);
        File destinodir = new File(destinoresuelto);
        
        //Debe ser carpeta (crear si no existe)
        if (!destinodir.exists()) {
            int r = JOptionPane.showConfirmDialog(this, "La carpeta no existe. Crear?", "Crear destino", JOptionPane.YES_NO_OPTION);
            
            if (r == JOptionPane.YES_OPTION) {
                if (!destinodir.mkdirs()) {
                    JOptionPane.showMessageDialog(this, "No se pudo crear el destino");
                    return;
                }
            } else {
                return;
            }
        }
        
        if (!destinodir.isDirectory()) {
            JOptionPane.showMessageDialog(this, "El destino debe ser una carpeta");
            return;
        }
        
        //Validaciones de seguridad
        String origenabs = Normalizar(origen.getAbsolutePath());
        String destinoabs = Normalizar(destinodir.getAbsolutePath());
        
        //No salir de la raiz
        if (!isDentroRuta(destinoabs)) {
            JOptionPane.showMessageDialog(this, "Destino fuer de Z:\\usuario\\");
            return;
        }
        
        //No copiar/mover a si mismo
        if (origenabs.equals(destinoabs)) {
            JOptionPane.showMessageDialog(this, "Origen y destino son el mismo");
            return;
        }
        
        //No mover/copiar carpeta dentro de si misma
        if (origen.isDirectory() && destinoabs.startsWith(origenabs + separador)) {
            JOptionPane.showMessageDialog(this, "No puedes colocar una carpeta dentro de si misma");
            return;
        }
        
        try {
            boolean ok = copiar ? SistemaArchivo.Copiar(origenabs, destinoabs) : SistemaArchivo.Mover(origenabs, destinoabs);
            
            if (!ok) {
                JOptionPane.showMessageDialog(this, (copiar ? "No se pudo copiar" : "No se pudo mover"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
        
        Refrescar();
    }
    
    private void Refrescar() {
        modelo.setRowCount(0);
        
        ResultadoListado rl = SistemaArchivo.ListarContenido(DirActual);
        
        //Carpetas primero
        for (Carpeta carpeta : rl.getCarpetas()) {
            File file = new File(carpeta.getRutaAbsoluta());
            modelo.addRow(new Object[]{ file.getName(), "<DIR>", 0L, file.lastModified() });
        }
        
        //Archivos
        for (Archivo archivo : rl.getArchivos()) {
            File file = new File(archivo.getRutaAbsoluta());
            modelo.addRow(new Object[]{ file.getName(), getExtension(file.getName()), file.length() });
        }
        
        LblRuta.setText(DirActual);
        if (TxtRuta != null && !DirActual.equals(TxtRuta.getText())) {
            TxtRuta.setText(DirActual);
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
    
    private void AbrirItemDobleClick(int filavista) {
        int fila = tabla.convertRowIndexToModel(filavista);
        String nombre = (String) modelo.getValueAt(fila, 0);
        File file = new File(DirActual, nombre);
        
        if (file.isDirectory()) {
            Navegar(file.getAbsolutePath());
            return;
        }
        
        String ext = getExtension(file.getName());
        
        switch (ext) {
            case "txt":
            case "rtf":
                SwingUtilities.invokeLater(() -> {
                    new EditorTexto(file).setVisible(true);
                });
                break;
                
            case "jpg":
            case "jpeg":
            case "png":
                SwingUtilities.invokeLater(() -> {
                    new VisorImagenes(file).setVisible(true);
                });
                break;
                
            default:
                JOptionPane.showMessageDialog(this, "Tipo de archivo no soportado actualmente: ." + (ext.isEmpty() ? "(sin extension)" : ext), "Error abriendo archivo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void Navegar(String ruta) {
        if (ruta == null) {
            return;
        }
        
        String destino = ClampToRoot(ruta);
        File dir = new File(destino);
        
        if (!dir.isDirectory()) {
            return;
        }
        
        DirActual = dir.getAbsolutePath();
        Refrescar();
        SeleccionarNodoEnArbol(DirActual);
        TreePath path = Arbol.getSelectionPath();
        
        if (path != null) {
            Arbol.expandPath(path);
        }
    }
    
    private void SeleccionarNodoEnArbol(String ruta) {
        DefaultTreeModel modeloarbol = (DefaultTreeModel) Arbol.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) modeloarbol.getRoot();
        TreePath path = BuscarRuta(root, ruta);
        
        if (path != null) {
            Arbol.setSelectionPath(path);
            Arbol.scrollPathToVisible(path);
            Arbol.expandPath(path);
        }
    }
    
    private TreePath BuscarRuta(DefaultMutableTreeNode nodo, String ruta) {
        File file = ((NodoArchivo) nodo).file;
        
        if (file.getAbsolutePath().equalsIgnoreCase(ruta)) {
            return new TreePath(nodo.getPath());
        }
        
        for (int i = 0; i < nodo.getChildCount(); i++) {
            TreePath resultado = BuscarRuta((DefaultMutableTreeNode) nodo.getChildAt(i), ruta);
            
            if (resultado != null) {
                return resultado;
            }
        }
        
        return null;
    }
    
    private String Normalizar(String path) {
        if (path == null) {
            return "";
        }
        
        File file = new File(path);
        
        String norm = file.getAbsoluteFile().toPath().normalize().toString();
        
        return norm.replace("/", File.separator).replace("\\", File.separator);
    }
    
    private boolean isDentroRuta(String path) {
        String p = Normalizar(path);
        return p.equals(RUTA) || p.startsWith(RUTA + File.separator);
    }
    
    private String ClampToRoot(String path) {
        String p = Normalizar(path);
        return isDentroRuta(path) ? p : RUTA;
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

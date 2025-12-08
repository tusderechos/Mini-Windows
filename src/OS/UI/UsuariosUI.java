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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.time.format.DateTimeFormatter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.geom.RoundRectangle2D;

import OS.UI.util.TemaOscuro;
import Compartidas.ManejoUsuarios;
import Compartidas.Repos;
import Compartidas.Usuario;
import OS.Archivos.SistemaArchivo;
import OS.Core.SesionActual;
import OS.Core.SistemaOperativo;
import OS.UI.util.GradientWallpaper;

public class UsuariosUI extends JFrame {
    
    private final ManejoUsuarios manejoUsuarios = Repos.USUARIOS;
    private DefaultTableModel Modelo = new DefaultTableModel(new Object[]{"Usuario", "Nombre", "Edad", "Genero", "Administrador", "Activo", "Creado"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int c) {
            return switch (c) {
                case 2 -> Integer.class;
                case 4 -> Boolean.class;
                case 5 -> Boolean.class;
                default -> String.class;
            };
        }
    };
    
    private JTable Tabla = new JTable(Modelo);
    private final DateTimeFormatter fecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public UsuariosUI(Usuario UsuarioActual) {
        //Asegurar que solo el admin pueda entrar
        if (UsuarioActual == null || !UsuarioActual.isAdministrador()) {
            JOptionPane.showMessageDialog(null, "Acceso Denegado: Solo Administradores.");
            dispose();
            return;
        }
        
        setTitle("Mini-Windows - Usuarios");
        setSize(800, 520);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(720, 440));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(TemaOscuro.BG);
        setLayout(new BorderLayout(8, 8));
                
        GradientWallpaper top = new GradientWallpaper();
        top.setPreferredSize(new Dimension(0, 44));
        top.setGradient(new Color(35, 35, 40), Color.MAGENTA.darker().darker());
        top.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 70)));
        
        JButton BtnNuevo = CrearBoton("Nuevo", true, 16);
        JButton BtnEditar = CrearBoton("Editar", false, 16);
        JButton BtnActivar = CrearBoton("Activar/Desactivar", false, 16);
        JButton BtnEliminar = CrearBoton("Eliminar", false, 16);
        JButton BtnRefrescar = CrearBoton("Refrescar", false, 16);
        
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        
        top.add(BtnNuevo);
        top.add(BtnEditar);
        top.add(BtnActivar);
        top.add(BtnEliminar);
        top.add(BtnRefrescar);
        
        add(top, BorderLayout.NORTH);
        
        TableRowSorter sorter = new TableRowSorter<>(Modelo);
        Tabla.setRowSorter(sorter);
        
        JScrollPane scroll = new JScrollPane(Tabla);
        scroll.getViewport().setBackground(TemaOscuro.BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        EstilizarTabla(Tabla);
        
        add(scroll, BorderLayout.CENTER);
        
        BtnNuevo.addActionListener(e -> CrearUsuario());
        BtnEditar.addActionListener(e -> EditarSeleccionado());
        BtnActivar.addActionListener(e -> ToggleSeleccionado());
        BtnEliminar.addActionListener(e -> EliminarSeleccionado());
        BtnRefrescar.addActionListener(e -> Refrescar());
        
        //Cargar datos
        Refrescar();
    }
    
    private void Refrescar() {
        Modelo.setRowCount(0);
        
        for(Usuario usuario : manejoUsuarios.getUsuarios()) {
            Modelo.addRow(new Object[]{usuario.getUsuario(), usuario.getNombreUsuario(), usuario.getEdad(), String.valueOf(usuario.getGenero()), (usuario.isAdministrador() ? "SI" : "NO"), (usuario.isActivo() ? "SI" : "NO"), usuario.getFechaEntrada() == null ? "" : fecha.format(usuario.getFechaEntrada())});
        }
    }
    
    private Usuario getSeleccionado() {
        int filavisual = Tabla.getSelectedRow();
        
        if (filavisual < 0) {
            return null;
        }
        
        int fila = Tabla.convertRowIndexToModel(filavisual);
        String username = (String) Modelo.getValueAt(fila, 0);
        
        return manejoUsuarios.Buscar(username);
    }
    
    private void CrearUsuario() {
        Usuario nuevo = DialogoUsuario.mostrar(this, null);
        
        if (nuevo == null) {
            return;
        }
        
        if (!manejoUsuarios.Agregar(nuevo)) {
            JOptionPane.showMessageDialog(this, "El usuario ya existe.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        
        Refrescar();
    }
    
    private void EditarSeleccionado() {
        Usuario base = getSeleccionado();
        
        if (base == null) {
            return;
        }
        
        Usuario editado = DialogoUsuario.mostrar(this, base);
        
        if (editado == null) {
            return;
        }
        
        if (!manejoUsuarios.Actualizar(editado)) {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        Refrescar();
    }
    
    private void ToggleSeleccionado() {
        Usuario usuario = getSeleccionado();
        
        if (usuario == null) {
            return;
        }
        
        Usuario actual = SesionActual.getUsuario();
        if (actual != null && actual.getUsuario().equalsIgnoreCase(usuario.getUsuario())) {
            JOptionPane.showMessageDialog(this, "Tu cuenta fue desactivada. Se cerrara la sesion.");
            
            SwingUtilities.invokeLater(() -> {
                //Cerrar esta ventana
                Window w = SwingUtilities.getWindowAncestor(this);
                if (w != null) {
                    w.dispose();
                }
                
                //Cerrar el Escritorio tambien si esta abierto
                for(Window win : Window.getWindows()) {
                    if (win instanceof Escritorio) {
                        win.dispose();
                    }
                }
                
                //Volver al login
                new Login(new SistemaOperativo()).setVisible(true);
            });
            
            return;
        }
        
        if (!manejoUsuarios.ToggleActivo(usuario.getUsuario())) {
            JOptionPane.showMessageDialog(this, "No se pudo cambiar el estado", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        Refrescar();
        Tabla.repaint();
    }
    
    private void EliminarSeleccionado() {
        Usuario usuario = getSeleccionado();
        
        if (usuario == null) {
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this, "Eliminar al usuario \"" + usuario.getUsuario() + "\"?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        Usuario actual = SesionActual.getUsuario();
        
        //Si se esta eliminando la cuenta a la que esta conectado el usuario
        if (actual != null && actual.getUsuario().equalsIgnoreCase(usuario.getUsuario())) {
            //Persistir la eliminacion
            if (!manejoUsuarios.Eliminar(usuario.getUsuario())) {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario: " + usuario.getUsuario(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            //Borrar la carpeta fisica del usuario
            try {
                SistemaArchivo.EliminarCarpetaUsuario(usuario.getUsuario());
            } catch (Exception ignorar) {
            }
            
            //Cerrar sesion
            SesionActual.CerrarSesion();
            
            //Cerrar esta ventana y el escritorio y volver a la ventana de login
            EventQueue.invokeLater(() -> {
                //Cerrar esta ventana
                UsuariosUI.this.dispose();
                
                //Cerrar el Escritorio tambien si esta abierto
                for(Window win : Window.getWindows()) {
                    if (win instanceof Escritorio) {
                        win.dispose();
                    }
                }
                
                //Volver al login
                new Login(new SistemaOperativo()).setVisible(true);
            });
            
            return;
        }
        
        //Eliminar a otro usuario que no este logueado
        if (!manejoUsuarios.Eliminar(usuario.getUsuario())) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar al usuario: " + usuario.getUsuario(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            SistemaArchivo.EliminarCarpetaUsuario(usuario.getUsuario());
        } catch (Exception ignorar) {
        }
            
        Refrescar();
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
    
    private void EstilizarTabla(JTable tabla) {
        JTableHeader header = tabla.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(new Color(45, 45, 50));
        header.setForeground(new Color(220, 220, 225));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 70)));
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        
        //Columnas con un estilo de zebra
        final Color filapar = new Color(34, 34, 38);
        final Color filaimpar = new Color(28, 28, 32);
        final Color selBG = new Color(0x4C8DFF);
        final Color selFG = Color.WHITE;
        
        DefaultTableCellRenderer zebra = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                setText(value == null ? "" : value.toString());
            }
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    setBackground(selBG);
                    setForeground(selFG);
                } else {
                    setBackground((row % 2 == 0) ? filapar : filaimpar);
                    setForeground(TemaOscuro.TEXTO);
                }
                
                setBorder(new EmptyBorder(0, 8, 0, 8));
                
                return this;
            }
        };
        
        //Aplicar el renderer por defecto a todo lo que existe
        tabla.setDefaultRenderer(Object.class, zebra);
        tabla.setDefaultRenderer(String.class, zebra);
        tabla.setDefaultRenderer(Integer.class, zebra);
        
        //Checkboxes centrados
        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        
        tabla.getColumnModel().getColumn(4).setCellRenderer(centro); //Administrador
        tabla.getColumnModel().getColumn(5).setCellRenderer(centro); //Activo
        
        //Anchos
        tabla.getColumnModel().getColumn(0).setPreferredWidth(120); //Usuario
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180); //Nombre
        tabla.getColumnModel().getColumn(2).setPreferredWidth(70); //Edad
        tabla.getColumnModel().getColumn(3).setPreferredWidth(70); //Genero
        tabla.getColumnModel().getColumn(4).setPreferredWidth(110); //Administrador
        tabla.getColumnModel().getColumn(5).setPreferredWidth(80); //Activo
        tabla.getColumnModel().getColumn(6).setPreferredWidth(120); //Creado
        
        //Fila/colores base
        tabla.setBackground(TemaOscuro.BG);
        tabla.setForeground(TemaOscuro.TEXTO);
        tabla.setGridColor(TemaOscuro.LINEA);
        tabla.setRowHeight(22);
        tabla.setSelectionBackground(selBG);
        tabla.setSelectionForeground(selFG);
    }
}

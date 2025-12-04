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

import OS.UI.util.TemaOscuro;
import Compartidas.ManejoUsuarios;
import Compartidas.Repos;
import Compartidas.Usuario;
import OS.Core.SesionActual;
import OS.Core.SistemaOperativo;

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
                
        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.setBackground(TemaOscuro.BAR);
        
        JButton BtnNuevo = BotonStyle("Nuevo");
        JButton BtnEditar = BotonStyle("Editar");
        JButton BtnActivar = BotonStyle("Activar/Desactivar");
        JButton BtnEliminar = BotonStyle("Eliminar");
        JButton BtnRefrescar = BotonStyle("Refrescar");
        
        top.add(BtnNuevo);
        top.add(BtnEditar);
        top.add(BtnActivar);
        top.add(BtnEliminar);
        top.add(BtnRefrescar);
        
        add(top, BorderLayout.NORTH);
        
        Tabla.setBackground(TemaOscuro.BG);
        Tabla.setForeground(TemaOscuro.TEXTO);
        Tabla.setSelectionBackground(TemaOscuro.CARD);
        Tabla.setSelectionForeground(TemaOscuro.TEXTO);
        Tabla.setGridColor(TemaOscuro.LINEA);
        Tabla.setRowHeight(22);
        
        TableRowSorter sorter = new TableRowSorter<>(Modelo);
        Tabla.setRowSorter(sorter);
        
        JScrollPane scroll = new JScrollPane(Tabla);
        scroll.getViewport().setBackground(TemaOscuro.BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
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
            Modelo.addRow(new Object[]{usuario.getUsuario(), usuario.getNombreUsuario(), usuario.getEdad(), String.valueOf(usuario.getGenero()), usuario.isAdministrador(), usuario.isActivo(), usuario.getFechaEntrada() == null ? "" : fecha.format(usuario.getFechaEntrada())});
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
        if (actual != null && actual.getUsuario().equalsIgnoreCase(usuario.getUsuario())) {
            JOptionPane.showMessageDialog(this, "Tu cuenta fue eliminada. Se cerrara la sesion.");
            
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
        
        if (!manejoUsuarios.Eliminar(usuario.getUsuario())) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar al usuario: " + usuario.getUsuario(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
            
        Refrescar();
    }
    
    private JButton BotonStyle(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(TemaOscuro.CARD);
        boton.setForeground(TemaOscuro.TEXTO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(TemaOscuro.LINEA));
        
        return boton;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Insta.GestorInsta;
import Insta.Insta;
import Compartidas.Usuario;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author HP
 */
public class vtnInstaPrincipal extends JFrame {
    
    //3 vtn
    private final Usuario usuarioActual;
    private final JScrollPane scrollTimeLine;
    private final JPanel panelContenido;
    private JTextField txtBusqueda;
    
    public vtnInstaPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("INSTA - TimeLine de @" + usuario.getNombreUsuario());
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        
        scrollTimeLine = new JScrollPane(panelContenido);
        scrollTimeLine.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollTimeLine.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollTimeLine, BorderLayout.CENTER);
        
        inicializarComponentes();
        cargarTimeLine();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        
        JPanel panelControlSuperior = new JPanel(new BorderLayout(5, 5));
        
        JButton btnPerfil = new JButton("Mi Perfil");
        btnPerfil.addActionListener(e -> {
            vtnPerfil perfil = new vtnPerfil(usuarioActual);
            perfil.setVisible(true);
        });
        
        JButton btnCrearInsta = new JButton("Publicar");
        btnCrearInsta.addActionListener(e -> {
            vtnCrearInsta crearInsta = new vtnCrearInsta(this);
            crearInsta.setVisible(true);
        });
        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        
        txtBusqueda = new JTextField("Buscar usuarios...");
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarUsuarios());
        
        panelBusqueda.add(txtBusqueda, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);
        
        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelNavegacion.add(btnPerfil);
        panelNavegacion.add(btnCrearInsta);
        
        panelControlSuperior.add(panelNavegacion, BorderLayout.WEST);
        panelControlSuperior.add(panelBusqueda, BorderLayout.CENTER);
        
        add(panelControlSuperior, BorderLayout.NORTH);
        
        add(scrollTimeLine, BorderLayout.CENTER);
    }
    
    private void buscarUsuarios() {
        String texto = txtBusqueda.getText().trim();
        if (texto.isEmpty() || texto.equals("Buscar usuario...")) {
            JOptionPane.showMessageDialog(this, "Ingrese un termino de busqueda valido.");
            return;
        }
        
        try {
            ArrayList<Usuario> resultados = GestorInsta.buscarPersonas(texto, usuarioActual.getNombreUsuario());
            JDialog rd = new JDialog(this, "Resultados de Busqueda", true);
            rd.setSize(400, 500);
            rd.setLayout(new BorderLayout());
            
            JPanel panelLista = new JPanel();
            panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
            
            if (resultados.isEmpty()) {
                panelLista.add(new JLabel("No se encontraron usuarios que coincidan con su busqueda."));
            } else {
                for (Usuario u : resultados) {
                    JPanel userPanel = crearPanelUsuario(u, rd);
                    panelLista.add(userPanel);
                    panelLista.add(new JSeparator(SwingConstants.HORIZONTAL));
                }
            }
            rd.add(new JScrollPane(panelLista), BorderLayout.CENTER);
            rd.setLocationRelativeTo(this);
            rd.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de E/S al buscar usuarios: " + e.getMessage());
        }
    }
    
    private JPanel crearPanelUsuario(Usuario usuarioEncontrado,JDialog dialogoBusqueda) throws IOException {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel labelUsername = new JLabel("<html><b><a href='#'>@" + usuarioEncontrado.getUsuario() + "</a></b> (" + usuarioEncontrado.getUsuario() + ")</html>");
        labelUsername.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelUsername.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
                dialogoBusqueda.dispose();
                abrirPerfilUsuario(usuarioEncontrado.getUsuario());
            }
        });
        
        panel.add(labelUsername, BorderLayout.WEST);
        
        JButton btnFollow = new JButton();
        boolean esSiguiendo = GestorInsta.estaSiguiendo(usuarioActual.getUsuario(), usuarioEncontrado.getUsuario());
        btnFollow.setText(esSiguiendo ? "Dejar de Seguir" : "Seguir");
        
        btnFollow.addActionListener(e -> {
            manejarFollow(usuarioEncontrado.getUsuario(), !esSiguiendo, btnFollow, dialogoBusqueda);
        });
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnFollow);
        panel.add(btnPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void abrirPerfilUsuario(String username){
        if(username.equalsIgnoreCase(usuarioActual.getUsuario())){
            vtnPerfil p = new vtnPerfil(usuarioActual);
            p.setVisible(true);
        }else{
            new vtnOtroPerfil(username, this);
        }
    }
    
    private void manejarFollow(String seguido, boolean nuevoEstado, JButton btnFollow, JDialog dialogoBusqueda) {
        String seguidor = usuarioActual.getUsuario();
        
        try {
            if (!nuevoEstado) {
                int confirmacion = JOptionPane.showConfirmDialog(this,
                        "¿Desea dejar de seguir a @" + seguido + "?",
                        "Confirmar Unfollow", JOptionPane.YES_NO_OPTION);
                
                if (confirmacion != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            GestorInsta.actualizarEstadoFollow(seguidor, seguido, nuevoEstado);
            dialogoBusqueda.dispose();
            btnFollow.setText(nuevoEstado ? "Dejar de Seguir" : "Seguir");
            
            JOptionPane.showMessageDialog(this,
                    (nuevoEstado ? "¡Ahora sigues a @" : "Has de jado de seguir a @") + seguido,
                    "Actualizacion de Follow", JOptionPane.INFORMATION_MESSAGE);
            
            cargarTimeLine();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de E/S al actualizar el estado: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrio un error: " + e.getMessage());
        }
    }
    
    public void cargarTimeLine() {
        panelContenido.removeAll();
        
        try {
            ArrayList<Insta> timeLine = GestorInsta.generarTimeLine(usuarioActual.getUsuario());
            
            if (timeLine.isEmpty()) {
                panelContenido.add(new JLabel("Aun no sigues a nadie o no hay post para mostrar."));
            } else {
                for (Insta post : timeLine) {
                    JPanel postPanel = crearPanelPost(post);
                    panelContenido.add(postPanel);
                    panelContenido.add(new JSeparator(SwingConstants.HORIZONTAL));
                }
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de E/S al cargcar el TimeLine: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar el TimeLine: " + e.getMessage());
        }
        
        panelContenido.revalidate();
        panelContenido.repaint();
    }
    
    private JPanel crearPanelPost(Insta post) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BorderLayout(10, 10));

        //encabezado: autor y fecha
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelAutor = new JLabel("<html><b>@" + post.getAutorUsername() + "</b></html>");
        panelHeader.add(labelAutor);

        //cuerpo: img y texto
        JPanel panelCuerpo = new JPanel(new BorderLayout());

        //simulacionde la img
        JLabel labelImg = new JLabel("img aqui: " + post.getRutaImg());
        labelImg.setPreferredSize(new Dimension(550, 400));
        labelImg.setHorizontalAlignment(SwingConstants.CENTER);
        labelImg.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        //texto
        JTextArea txtCaption = new JTextArea(post.getTexto());
        txtCaption.setWrapStyleWord(true);
        txtCaption.setLineWrap(true);
        txtCaption.setEditable(false);
        txtCaption.setBackground(panel.getBackground());
        
        panelCuerpo.add(labelImg, BorderLayout.NORTH);
        panelCuerpo.add(txtCaption, BorderLayout.CENTER);
        
        return panel;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Insta.GestorInsta;
import Insta.Insta;
import Insta.Usuario;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author HP
 */
public class vtnInstaPrincipal extends JFrame{
    private final Usuario usuarioActual;
    private final JScrollPane scrollTimeLine;
    private final JPanel panelContenido;
    
    public vtnInstaPrincipal(Usuario usuario){
        this.usuarioActual = usuario;
        setTitle("INSTA - TimeLine de @"+usuario.getUsername());
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        
        scrollTimeLine = new JScrollPane(panelContenido);
        scrollTimeLine.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollTimeLine.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollTimeLine, BorderLayout.CENTER);
        
        cargarTimeLine();
    }
    
    private void cargarTimeLine(){
        panelContenido.removeAll();
        
        try{
            ArrayList<Insta> timeLine = GestorInsta.generarTimeLine(usuarioActual.getUsername());
            
            if(timeLine.isEmpty()){
                panelContenido.add(new JLabel("Aun no sigues a nadie o no hay post para mostrar."));
            }else{
                for(Insta post : timeLine){
                    JPanel postPanel = crearPanelPost(post);
                    panelContenido.add(postPanel);
                    panelContenido.add(new JSeparator(SwingConstants.HORIZONTAL));
                }
            }
            
        }catch(IOException e){
            JOptionPane.showMessageDialog(this, "Error de E/S al cargcar el TimeLine: "+e.getMessage());
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error al generar el TimeLine: "+e.getMessage());
        }
        
        panelContenido.revalidate();
        panelContenido.repaint();
    }
    
    private JPanel crearPanelPost(Insta post){
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BorderLayout(10, 10));
        
        //encabezado: autor y fecha
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelAutor = new JLabel("<html><b>@"+post.getAutorUsername()+"</b></html>");
        panelHeader.add(labelAutor);
        
        //cuerpo: img y texto
        JPanel panelCuerpo = new JPanel(new BorderLayout());
        
        //simulacionde la img
        JLabel labelImg = new JLabel("img aqui: "+post.getRutaImg());
        labelImg.setPreferredSize(new Dimension(550, 400));
        labelImg.setHorizontalAlignment(SwingConstants.CENTER);
        labelImg.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        //texto
        JTextArea txtCaption =new JTextArea(post.getTexto());
        txtCaption.setWrapStyleWord(true);
        txtCaption.setLineWrap(true);
        txtCaption.setEditable(false);
        txtCaption.setBackground(panel.getBackground());
        
        panelCuerpo.add(labelImg, BorderLayout.NORTH);
        panelCuerpo.add(txtCaption, BorderLayout.CENTER);
        
        return panel;
    }
}

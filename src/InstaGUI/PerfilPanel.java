/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Insta.DatosPerfil;
import Insta.GestorInsta;
import Insta.Insta;
import Insta.PerfilNoEncontrado;
import Insta.SesionManager;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author HP
 */
public class PerfilPanel extends JPanel{
    private String usernamePerfil;
    private JLabel username, nombre, contadorPost, contadorFollowers, ContadorFollowing;
    private JButton btnAccion;
    
    public PerfilPanel(String usernamePerfil){
        this.usernamePerfil = usernamePerfil;
        setLayout(new BorderLayout());
        cargarDatosYRenderizar();
    }
    
    private void cargarDatosYRenderizar(){
        removeAll();
        try{
            String usuarioLogueado = SesionManager.getUsuarioActual().getUsername();
            DatosPerfil datos = GestorInsta.obtenerPefilCompleto(usernamePerfil, usuarioLogueado);
            add(crearEncabezadoPerfil(datos, usuarioLogueado), BorderLayout.NORTH);
            add(crearGridPublicaciones(datos.getInstasPropios()), BorderLayout.CENTER);
        } catch(PerfilNoEncontrado e){
            add(new JLabel("<html><h1>Error: Perfil no encontrado<h1><p>"+e.getMessage()+"<p></html>"), BorderLayout.CENTER);
        } catch(IOException e){
            add(new JLabel("<html><h1>Error de Archivos<h1><p>No se pudieron cargar los datos del perfil<p></html>"));
        } catch(Exception e){
            add(new JLabel("Error: "+e.getMessage()), BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }
    
    private JPanel crearEncabezadoPerfil(DatosPerfil datos, String usuarioLogueado){
        JPanel panel = new JPanel();
    return panel;
    }
    
    private JScrollPane crearGridPublicaciones(ArrayList<Insta> instas){
        JPanel grid = new JPanel(new GridLayout(0, 3, 5, 5));
    return new JScrollPane(grid);
    }
}

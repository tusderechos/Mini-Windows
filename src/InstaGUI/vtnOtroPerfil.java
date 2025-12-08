/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author HP
 */
public class vtnOtroPerfil extends JPanel{
    private final vtnInstaPrincipal vtnP;
    private final PerfilPanel panelContenido;
    
    //otro perfil
    public vtnOtroPerfil(String usernameOtro, vtnInstaPrincipal vtnP){
        super(new BorderLayout());
        this.vtnP = vtnP;
        this.panelContenido = new PerfilPanel(usernameOtro);
        
        add(this.panelContenido, BorderLayout.CENTER);
    }
    
    public void recargarPerfil() {
    this.panelContenido.cargarDatosYRenderizar();
}
}

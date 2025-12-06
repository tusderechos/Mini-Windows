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
public class vtnOtroPerfil extends JDialog{
    
    //otro perfil
    public vtnOtroPerfil(String usernameOtro, Frame owner){
        setTitle("INST - Perfil de @"+usernameOtro);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(600, 800);
        setLocationRelativeTo(null);
        
        PerfilPanel panelOtro = new PerfilPanel(usernameOtro);
        add(panelOtro, BorderLayout.CENTER);
        setVisible(true);
    }
}

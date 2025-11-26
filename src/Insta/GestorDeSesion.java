/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

/**
 *
 * @author HP
 */
public class GestorDeSesion {
    private static Usuario usuarioActual = null;
    
    public static void setUsuarioActual(Usuario usuario){
        usuarioActual = usuario;
        System.out.println("Sesion iniciada para: "+usuario.getUsername());
    }
    
    public static Usuario getUsuarioActual(){
        return usuarioActual;
    }
    
    public static boolean cerrarSesion(){
        if(usuarioActual != null){
            String usernameCerrado = usuarioActual.getUsername();
            usuarioActual = null;
            System.out.println("Sesion de "+usernameCerrado+" cerrada");
            return true;
        }   
        return false;
    }
}

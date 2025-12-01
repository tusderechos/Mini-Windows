/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

/**
 *
 * @author HP
 */
public class SesionManager {
    private static Usuario usuarioActual = null;
    
    public static void setUsuarioActual(Usuario usuario){
        usuarioActual = usuario;
    }
    
    public static Usuario getUsuarioActual(){
        return usuarioActual;
    }
    
    public static void cerrarSesion(){
        usuarioActual = null;
    }
    
    public static boolean estaLogueado(){
        return usuarioActual != null;
    }
}

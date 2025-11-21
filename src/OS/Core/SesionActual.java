/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Core;

/**
 *
 * @author Hp
 */

import Compartidas.Usuario;

public class SesionActual {
    
    private static Usuario UsuarioActual = null;
    
    /*
        Establecer usuario logueado
    */
    public static void IniciarSesion(Usuario usuario) {
        UsuarioActual = usuario;
    }
    
    /*
        Obtener el usuario logueado
    */
    public static Usuario getUsuario() {
        return UsuarioActual;
    }
    
    /*
        Saber si hay un usuario que ya este logueado
    */
    public static boolean haySesion() {
        return UsuarioActual != null;
    }
    
    /*
        Cerrar sesion
    */
    public static void CerrarSesion() {
        UsuarioActual = null;
    }
}

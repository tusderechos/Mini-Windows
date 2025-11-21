/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Core;

/**
 *
 * @author Hp
 */

import Compartidas.ManejoUsuarios;
import Compartidas.Usuario;
import MiniWindows.Core.GestorCarpetasUsuario;

import javax.swing.JOptionPane;

public class SistemaOperativo {
    
    private ManejoUsuarios manejoUsuarios;
    private Usuario UsuarioActual;
    
    public SistemaOperativo() {
        manejoUsuarios = new ManejoUsuarios();
        UsuarioActual = null;
    }
    
    /*
        Registra un nuevo usuario en el sistema
    */
    public Usuario RegistrarUsuario(String NombreCompleto, char Genero, String Usuario, String Contrasena, int Edad, String RutaFotoPerfil) {
        if (NombreCompleto == null || NombreCompleto.isBlank() || Usuario == null || Usuario.isBlank() || Contrasena == null || Contrasena.isBlank()) {
            JOptionPane.showMessageDialog(null, "Error:\nNombre, Usuario y Contraseña son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        if (!manejoUsuarios.UsernameDisponible(Usuario)) {
            JOptionPane.showMessageDialog(null, "Error:\nEl Usuario '" + Usuario + "' ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        //Crear el nuevo usuario
        Usuario nuevo = new Usuario(NombreCompleto, Genero, Usuario, Contrasena, Edad, (RutaFotoPerfil == null || RutaFotoPerfil.isBlank()) ? null : RutaFotoPerfil);
        
        //Agregar el nuevo usuario
        boolean usuarioagregado = manejoUsuarios.Agregar(nuevo);
        
        if (!usuarioagregado) {
            JOptionPane.showMessageDialog(null, "Error:\nNo se pudo agregar el usuario a users.ins", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        //Crear las carpetas base (Mis Documentos, Musica, Mis Imagenes) para el usuario
        GestorCarpetasUsuario.CrearEstructuraUsuario(Usuario);
        
        JOptionPane.showMessageDialog(null, "Usuario '" + Usuario + "' creado correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
        
        return nuevo;
    }
    
    /*
        Intenta iniciar sesion con el usuario y la contrasena
    */
    public boolean IniciarSesion(String usuario, String contrasena) {
        Usuario usuariousuario = manejoUsuarios.ValidarLogin(usuario, contrasena);
        
        if (usuariousuario != null) {
            UsuarioActual = usuariousuario;
            JOptionPane.showMessageDialog(null, "Login exitoso.\nBienvenido, " + usuariousuario.getNombreUsuario());
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Login fallido.\nVerifica tus credenciales o si la cuenta esta activa");
            return false;
        }
    }
    
    /*
        Cerrar la sesion actual
    */
    public void CerrarSesion() {
        if (UsuarioActual != null) {
            JOptionPane.showMessageDialog(null, "Sesion cerrada para: " + UsuarioActual.getUsuario());
        }
        
        UsuarioActual = null;
    }
    
    public boolean hayUsuarioLogueado() {
        return UsuarioActual != null;
    }
    
    public Usuario getUsuarioActual() {
        return UsuarioActual;
    }
    
    /*
        Por si me dan ganas de acceder a lo de manejo de usuario desde otras partes
    */
    public ManejoUsuarios getManejoUsuarios() {
        return manejoUsuarios;
    }
}

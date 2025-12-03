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
import Compartidas.Repos;
import Compartidas.Usuario;
import java.io.File;

import javax.swing.JOptionPane;

public class SistemaOperativo {
    
    private ManejoUsuarios manejoUsuarios = Repos.USUARIOS;
    private Usuario UsuarioActual;
    
    public SistemaOperativo() {
        UsuarioActual = null;
        
        GestorCarpetasUsuario.AsegurarBase();
        AsegurarAdminInicial();
    }
    
    /*
        Registra un nuevo usuario en el sistema
    */
    public Usuario RegistrarUsuario(String NombreCompleto, char Genero, String Usuario, String Contrasena, int Edad) {
        if (!esAdminActual()) {
            throw new SecurityException("Solo el administrador puede crear cuentas");
        }
        
        if (NombreCompleto == null || NombreCompleto.isBlank() || Usuario == null || Usuario.isBlank() || Contrasena == null || Contrasena.isBlank()) {
            JOptionPane.showMessageDialog(null, "Error:\nNombre, Usuario y Contraseña son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        if (!manejoUsuarios.UsernameDisponible(Usuario)) {
            JOptionPane.showMessageDialog(null, "Error:\nEl Usuario '" + Usuario + "' ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        //Crear el nuevo usuario
        Usuario nuevo = new Usuario(NombreCompleto, Genero, Usuario, Contrasena, Edad, false);
        
        //Agregar el nuevo usuario
        boolean usuarioagregado = manejoUsuarios.Agregar(nuevo);
        
        if (!usuarioagregado) {
            JOptionPane.showMessageDialog(null, "Error:\nNo se pudo agregar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
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
        
        if (usuariousuario == null) {
            return false;
        }
        
        UsuarioActual = usuariousuario;
        
        try {
            GestorCarpetasUsuario.ValidarOCrearEstructuraUsuario(usuariousuario.getUsuario());
        } catch (Throwable ignorar) {
        }
        
        JOptionPane.showMessageDialog(null, "Login exitoso.\nBienvenido, " + usuariousuario.getUsuario());
        
        return true;
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
    
    private void AsegurarAdminInicial() {
        //Si no existe el administrador por alguna razon, se crea
        if (manejoUsuarios.UsernameDisponible("admin")) {
            Usuario admin = new Usuario("Administrador del sistema", 'M', "admin", "admin123", 19, true);
            manejoUsuarios.Agregar(admin);
            
            //Crear su estructura de carpetas tambien
            GestorCarpetasUsuario.CrearEstructuraUsuario("admin"); //Crea Z/admin + subcarpetas
            
            System.out.println("Admin creado\nuser: admin\npass: admin123");
            System.out.println("admin creado en: " + new File(Compartidas.Constantes.RUTA_BASE, "admin").getAbsolutePath());
        } else {
            //Si ya existia el usuario admin, siemp se garantiza su carpeta
            GestorCarpetasUsuario.ValidarOCrearEstructuraUsuario("admin");
        }
    }
    
    private boolean esAdminActual() {
        return UsuarioActual != null && UsuarioActual.isAdministrador();
    }
}

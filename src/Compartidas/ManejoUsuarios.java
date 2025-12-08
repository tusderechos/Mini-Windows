/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Compartidas;

/**
 *
 * @author Hp
 */

import OS.Archivos.SistemaArchivo;
import OS.Core.GestorCarpetasUsuario;
import java.util.ArrayList;
import java.io.*;
import javax.swing.JOptionPane;

public class ManejoUsuarios {
    
    private final File Archivo;
    private ArrayList<Usuario> Lista;
    
    public ManejoUsuarios() {
        Archivo = new File(Constantes.ARCHIVO_USUARIOS);
        Lista = new ArrayList<>();
        
        Cargar();
    }
    
    private void Cargar() {
        if (!Archivo.exists()) {
            return;
        }
        
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Archivo));
            
            Object obj = ois.readObject();
            
            if (obj instanceof ArrayList) {
                Lista = (ArrayList<Usuario>) obj;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar users.os");
            Lista = new ArrayList<>();
        }
    }
    
    private void Guardar() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Archivo));
            
            oos.writeObject(Lista);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar users.os");
        }
    }
    
    public boolean UsernameDisponible(String username) {
        return Buscar(username) == null;
    }
    
    /*
        Buscar un usuario por su username para ver si existe o no
    */
    public Usuario Buscar(String username) {
        for(Usuario usuario : Lista) {
            if (usuario.getUsuario().equalsIgnoreCase(username)) {
                return usuario;
            }
        }
        
        return null;
    }
    
    /*
        Agregar nuevo usuario
    */
    public boolean Agregar(Usuario usuario) {
        if (!UsernameDisponible(usuario.getUsuario())) {
            return false; //Por si ya existe
        }
        
        usuario.setActivo(true);
        
        Lista.add(usuario);
        Guardar();
        
        try {
            GestorCarpetasUsuario.CrearEstructuraUsuario(usuario.getUsuario());
        } catch (Throwable ignorar) {
        }
        
        return true;
    }
    
    /*
        Actualiza los datos de un usuario existente
    */
    public boolean Actualizar(Usuario actualizado) {
        for (int i = 0; i < Lista.size(); i++) {
            if (Lista.get(i).getUsuario().equalsIgnoreCase(actualizado.getUsuario())) {
                Lista.set(i, actualizado);
                Guardar();
                return true;
            }
        }
        
        return false;
    }
    
    /*
        Elimina el usuario segun su username
    */
    public boolean Eliminar(String username) {
        Usuario usuario = Buscar(username);
        
        if (usuario == null) {
            return false;
        }
        
        Lista.remove(usuario);
        Guardar();
        
        try {
        String base = Constantes.RUTA_BASE;
        File home = new File(base, username);
        
        SistemaArchivo.EliminarCarpetaUsuario(home.getAbsolutePath());
        } catch (Exception e) {
        }
        
        return true;
    }
    
    /*
        Activa/Desactiva usuario segun su username
    */
    public boolean ToggleActivo(String username) {
        Usuario usuario = Buscar(username);
        
        if (usuario == null) {
            return false;
        }
        
        usuario.setActivo(!usuario.isActivo());
        Guardar();
        return true;
    }
    
    /*
        Cambia la contrasena del usuario
    */
    public boolean CambiarContrasena(String username, String nuevacontrasena) {
        Usuario usuario = Buscar(username);
        
        if (usuario != null) {
            return false;
        }
        
        usuario.setContrasena(nuevacontrasena);
        Guardar();
        return true;
    }
    
    /*
        Validar el Login en si y devolver el usuario si es correcto
        si no es correcto, entonces devuelve null
    */
    public Usuario ValidarLogin(String username, String contrasena) {
        Usuario usuario = Buscar(username);
        
        if (usuario != null && usuario.isActivo() && usuario.getContrasena().equals(contrasena)) {
            return usuario;
        }
        
        return null;
    }

    public ArrayList<Usuario> getUsuarios() {
        return Lista;
    }
}

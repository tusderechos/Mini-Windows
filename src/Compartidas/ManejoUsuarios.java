/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Compartidas;

/**
 *
 * @author Hp
 */

import java.util.ArrayList;
import java.io.*;
import javax.swing.JOptionPane;

public class ManejoUsuarios {
    
    private File Archivo;
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
            JOptionPane.showMessageDialog(null, "Error al cargar users.ins");
        }
    }
    
    private void Guardar() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Archivo));
            
            oos.writeObject(Lista);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar users.ins");
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
        
        Lista.add(usuario);
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

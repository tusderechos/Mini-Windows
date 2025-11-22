/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class ManejoArchivosBinarios {
    public static final String archivoUsers = "Z:\\users.ins";
    
    public static void escribirUsuario(Usuario nuevoUsuario)throws IOException{
        try(FileOutputStream fos = new FileOutputStream(archivoUsers, true);
            AppendingObjectOutputStream oos = new AppendingObjectOutputStream(fos)){
            
            oos.writeObject(nuevoUsuario);
            System.out.println("usuario "+nuevoUsuario.getUsername()+" escrito con exito");
        } catch(FileNotFoundException e){
            System.err.println("archivo no encontrado: "+e.getMessage());
            throw e;
        }
    }
    
    public static ArrayList<Usuario> leerTodosLosUsuarios() throws IOException{
        ArrayList<Usuario> usuarios = new ArrayList<>();
        
        File archivo = new File(archivoUsers);
        if(!archivo.exists() || archivo.length() == 0){
            return usuarios;
        }
        
        try(FileInputStream fis = new FileInputStream(archivoUsers);
            ObjectInputStream ois = new ObjectInputStream(fis)){
            
            while(true){
                try{
                    Usuario usuario = (Usuario) ois.readObject();
                    usuarios.add(usuario);
                } catch(EOFException e){
                    break;
                }
            }
        } catch(ClassNotFoundException e){
            System.err.println("Clase no encontrada: "+e.getMessage());
        }
        return usuarios;
    }
    
    public static boolean existeUsername(String unsername){
        try{
            ArrayList<Usuario> listaUsuarios = leerTodosLosUsuarios();
            for(Usuario u : listaUsuarios){
                if(u.getUsername().equalsIgnoreCase(unsername)){
                    return true;
                }
            }
            return false;
        } catch(IOException e){
            System.err.println("Error al leer la lista de usuarios: "+e.getMessage());
            return false;
        }
    }
}

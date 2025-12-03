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
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class ManejoArchivosBinarios {

    public static final String archivoUsers = "Z:\\users.ins";

    public static void escribirUsuario(Usuario nuevoUsuario) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(archivoUsers, true); AppendingObjectOutputStream oos = new AppendingObjectOutputStream(fos)) {

            oos.writeObject(nuevoUsuario);
            System.out.println("usuario " + nuevoUsuario.getUsername() + " escrito con exito");
        } catch (FileNotFoundException e) {
            System.err.println("archivo no encontrado: " + e.getMessage());
            throw e;
        }
    }

    public static ArrayList<Usuario> leerTodosLosUsuarios() throws IOException {
        ArrayList<Usuario> usuarios = new ArrayList<>();

        File archivo = new File(archivoUsers);
        if (!archivo.exists() || archivo.length() == 0) {
            return usuarios;
        }

        try (FileInputStream fis = new FileInputStream(archivoUsers); ObjectInputStream ois = new ObjectInputStream(fis)) {

            while (true) {
                try {
                    Usuario usuario = (Usuario) ois.readObject();
                    usuarios.add(usuario);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Clase no encontrada: " + e.getMessage());
        }
        return usuarios;
    }

    public static boolean existeUsername(String unsername) {
        try {
            ArrayList<Usuario> listaUsuarios = leerTodosLosUsuarios();
            for (Usuario u : listaUsuarios) {
                if (u.getUsername().equalsIgnoreCase(unsername)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            System.err.println("Error al leer la lista de usuarios: " + e.getMessage());
            return false;
        }
    }

    public static void escribirInsta(Insta nuevoInsta, String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);
        
        if(!archivo.exists() || archivo.length() == 0){
            try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))){
            oos.writeObject(nuevoInsta);
            }
        }else{
            try(AppendingObjectOutputStream aoos = new AppendingObjectOutputStream(new FileOutputStream(archivo, true))){
            aoos.writeObject(nuevoInsta);
            }
        }
    }
    
    public static ArrayList<Insta> leerInstasDeUsuario(String username) throws IOException{
        ArrayList<Insta> instas = new ArrayList<>();
        String rutaArchivo = "Z:\\"+username+"\\instas.ins";
        File archivo = new File(rutaArchivo);
        
        if(!archivo.exists() || archivo.length()==0){
            return instas;
        }
        
        try(FileInputStream fis = new FileInputStream(rutaArchivo);
            ObjectInputStream ois = new ObjectInputStream(fis)){
            
            while(true){
                try{
                    Insta insta = (Insta)ois.readObject();
                    instas.add(insta);
                }catch(EOFException e){
                    break;
                }catch(ClassNotFoundException e){
                    throw new IOException("Error de formato al leer Insta: "+e.getMessage(), e);
                }
            }
        } catch(FileNotFoundException e){
            throw e;
        } catch(IOException e){
            throw e;
        }
        return instas;
    }
    
    /*public static void escribirFollow(String rutaArchivo, Follow follow) throws IOException{
        try(FileOutputStream fos = new FileOutputStream(rutaArchivo, true);
            AppendingObjectOutputStream oos = new AppendingObjectOutputStream(fos)){
            oos.writeObject(follow);
        } catch(FileNotFoundException e){
            System.err.println("Archivo de follow no encontrado: "+e.getMessage());
            throw e;
        }
    }*/
    
    public static ArrayList<Follow> leerListaFollows(String rutaArchivo) throws IOException{
        ArrayList<Follow> follows = new ArrayList();
        
        File archivo = new File(rutaArchivo);
        if(!archivo.exists() || archivo.length()==0){
            return follows;
        }
        
        try(FileInputStream fis = new FileInputStream(rutaArchivo);
            ObjectInputStream ois = new ObjectInputStream(fis)){
            
            while(true){
                try{
                    Follow follow = (Follow)ois.readObject();
                    follows.add(follow);
                }catch(EOFException e){
                    break;
                }catch(ClassNotFoundException e){
                    throw new IOException("Error de formato en archivo binario: "+e.getMessage(), e);
                }
            }
            
        }
        return follows;
    }
    
    public static void reescribirFollows(String rutaArchivo, ArrayList<Follow> follows) throws IOException{
        try(FileOutputStream fos = new FileOutputStream(rutaArchivo, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            
            for(Follow f : follows){
                oos.writeObject(f);
            }
        }
    }
    
    public static void reescribirTodosLosUsuarios(ArrayList<Usuario> listaUsuarios) throws IOException{
        try(FileOutputStream fos = new FileOutputStream(archivoUsers, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            
            for(Usuario u : listaUsuarios){
                oos.writeObject(u);
            }
            
        } catch(FileNotFoundException e){
            System.err.println("Archivo users.ins no encontrado para reescritura");
            throw e;
        }
    }
}

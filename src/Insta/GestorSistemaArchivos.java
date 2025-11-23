/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author HP
 */
public class GestorSistemaArchivos {
    private static final String rutaRaiz = "Z:\\";
    
    public static boolean crearEstructuraUsuario(String username){
        String rutaBaseUsuario = rutaRaiz+username;
        
        File directorioUsuario = new File(rutaBaseUsuario);
        if(!directorioUsuario.mkdirs()){
            System.err.println("Error al crear el directorio base del usuario: "+rutaBaseUsuario);
            return false;
        }
        
        String[] carpetasBasicas = {"Mis Documentos", "Musica", "Mis Imagenes"};
        for(String nombreCarpeta : carpetasBasicas){
            new File(rutaBaseUsuario, nombreCarpeta).mkdir();
        }
        
        try{
            new FileOutputStream(rutaBaseUsuario+"\\following.ins").close();
            new FileOutputStream(rutaBaseUsuario+"\\followers.ins").close();
            new FileOutputStream(rutaBaseUsuario+"\\insta.ins").close();
        }catch(IOException e){
            System.err.println("Error al crear los archivos: "+e.getMessage());
            return false;
        }
        System.out.println("Estructura de archivos creada para: "+username);
        return true;
    }
}

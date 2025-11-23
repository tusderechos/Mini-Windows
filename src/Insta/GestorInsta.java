/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class GestorInsta {
    public static boolean crearNuevaCuenta(Usuario nuevoUsuario) throws UsernameYaExiste, IOException{
        String username = nuevoUsuario.getUsername();
        if(ManejoArchivosBinarios.existeUsername(username)){
            throw new UsernameYaExiste("El usernanme "+username+" no esta disponible");
        }
        
        try{
            ManejoArchivosBinarios.escribirUsuario(nuevoUsuario);
        }catch(IOException e){
            System.err.println("Error al guardar el usuario: "+e.getMessage());
            throw e;
        }
        
        boolean estructuraCreada = GestorSistemaArchivos.crearEstructuraUsuario(username);
        
        if(!estructuraCreada){
            System.err.println("Error al crear estructura de carpetas para "+username);
        }
        return true;
    }
    
    public static void agregarFollow(String seguidor, String seguido) throws IOException {
        String rutaFollowing = "Z\\"+seguidor+"\\following.ins";
        Follow followNuevo = new Follow(seguido);
        ManejoArchivosBinarios.escribirFollow(rutaFollowing, followNuevo);
        
        String rutaFollowers = "Z\\"+seguido+"\\followers.ins";
        Follow followerNuevo = new Follow(seguidor);
        ManejoArchivosBinarios.escribirFollow(rutaFollowers, followerNuevo);
        
        System.out.println(seguidor+" ahora sigue a "+seguido);
    }
    
    public static ArrayList<Insta> generarTimeLine(String usuarioActual){
        ArrayList<Insta> timeLine = new ArrayList<>();
        
        ArrayList<Follow> seguidos = ManejoArchivosBinarios.leerListaFollows(usuarioActual, true);
        
        for(Follow f : seguidos){
            if(f.isActivo()){
                ArrayList<Insta> instasSeguido = ManejoArchivosBinarios.leerInstasDeUsuario(f.getUsername());
                timeLine.addAll(instasSeguido);
            }
        }
        
        ArrayList<Insta> instasPropios = ManejoArchivosBinarios.leerInstasDeUsuario(usuarioActual);
        timeLine.addAll(instasPropios);
        
        java.util.Collections.sort(timeLine);
        return timeLine;
    }
}

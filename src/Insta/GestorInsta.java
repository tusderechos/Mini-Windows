/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import java.io.IOException;

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
}

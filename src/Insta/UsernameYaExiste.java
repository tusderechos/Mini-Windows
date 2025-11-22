/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

/**
 *
 * @author HP
 */
public class UsernameYaExiste extends Exception{
    //vaina de la exception, despues creo las otras 2 :D
    
    public UsernameYaExiste(String mensaje){
        super(mensaje);
    }
    
    public UsernameYaExiste(){
        super("El nombre de usuario ya esta registrado en el sistema");
    }
}

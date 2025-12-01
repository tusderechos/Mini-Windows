/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

/**
 *
 * @author HP
 */
public class CredencialesInvalidas extends Exception{
    public CredencialesInvalidas(String mensaje){
        super(mensaje);
    }
    
    public CredencialesInvalidas(){
        super("Las credenciales proporcionadas(usuario o contraseña) son invalidas");
    }
}

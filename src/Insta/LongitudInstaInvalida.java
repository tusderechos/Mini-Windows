/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

/**
 *
 * @author HP
 */
public class LongitudInstaInvalida extends Exception{
    //exception 3
    
    private int longitudMaxima;
    
    public LongitudInstaInvalida(String mensaje, int maxima){
        super(mensaje);
        this.longitudMaxima = maxima;
    }
    
    public int getLongitudMaxima(){
        return longitudMaxima;
    }
}

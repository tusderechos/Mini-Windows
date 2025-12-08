/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI.util;

/**
 *
 * @author Hp
 */
public class Mp3Utils {
    
    /*
        Formatea de milisegundos a mm:ss
    */
    public static String fmtt(int ms) {
        if (ms < 0) {
            ms = 0;
        }
        
        int s = ms / 1000;
        int m = s / 60;
        
        s %= 60;
        
        return String.format("%02d:%02d", m, s);
    }
}

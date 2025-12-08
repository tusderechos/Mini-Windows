/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI.util;

/**
 *
 * @author Hp
 */
public class VolumeControl {
    
    private static volatile double Volumen = 0.8;

    public static double getVolumen() {
        return Volumen;
    }

    public static void setVolumen(double vol) {
        if (vol < 0) {
            vol = 0;
        }
        if (vol > 1) {
            vol = 1;
        }
        Volumen = vol;
    }
    
    public static void Mute() {
        Volumen = 0.0;
    }
    
    public static void Max() {
        Volumen = 1.0;
    }
}

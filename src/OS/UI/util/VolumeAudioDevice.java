/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI.util;

/**
 *
 * @author Hp
 */

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.JavaSoundAudioDevice;

public class VolumeAudioDevice extends JavaSoundAudioDevice {
    
    private final int channels;
    private long frameswritten = 0;
    
    public VolumeAudioDevice(int channels) {
        this.channels = (channels <= 0) ? 2 : channels;
    }

    public long getFrameswritten() {
        return frameswritten;
    }
    
    @Override
    protected void writeImpl(short[] samples, int offs, int len) throws JavaLayerException {
        //Copiar y escalar para no modificar el arreglo original
        short[] salida = new short[len];
        double vol = VolumeControl.getVolumen();
        
        if (vol < 0) {
            vol = 0;
        }
        if (vol > 1) {
            vol = 1;
        }
        
        for (int i = 0; i < len; i++) {
            int s = samples[offs + i];
            int escalado = (int) Math.round(s * vol);
            
            if (escalado > 32767) {
                escalado = 32767;
            }
            if (escalado < -32768) {
                escalado = -32768;
            }
            
            salida[i] = (short) escalado;
        }
        
        //Enviamos al dispositivo real
        frameswritten += len / (1152L * channels);
        super.writeImpl(salida, 0, len);
    }
    
    public void reset() {
        frameswritten = 0;
    }
}

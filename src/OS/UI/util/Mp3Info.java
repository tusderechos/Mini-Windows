/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI.util;

/**
 *
 * @author Hp
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;

public class Mp3Info {
    
    public final int TotalFrames;
    public final int Channels;
    public final int DurationMs;
    public final double msPerFrame;
    
    public Mp3Info(int TotalFrames, double msPerFrame, int Channels, int DurationMs) {
        this.TotalFrames = TotalFrames;
        this.msPerFrame = msPerFrame;
        this.Channels = Channels;
        this.DurationMs = DurationMs;
    }
    
    public static Mp3Info LeerInfo(File archivo) throws IOException, BitstreamException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(archivo));
        Bitstream bs = new Bitstream(bis);
        
        int frames = 0;
        Header primero = null;
        int mstotal = 0;
        int channels = 2;
        
        //Lectura rapida
        while (true) {
            Header h = bs.readFrame();
            
            if (h == null) {
                break;
            }
            
            if (primero == null) {
                primero = h;
                channels = (h.mode() == 3) ? 1 : 2;
            }
            
            mstotal += (int) Math.round(h.ms_per_frame());
            frames++;
            bs.closeFrame();
        }
        
        bs.close();
        
        if (primero == null) {
            throw new IllegalStateException("Archivo MP3 vacio o invalido");
        }
        
        double ms = (primero != null) ? primero.ms_per_frame() : 26.0;
        
        return new Mp3Info(frames, ms, channels, mstotal);
    }
}

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

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class Mp3PlayerThread extends Thread {
    
    private final File Archivo;
    private final Mp3Info Info;
    private final int FrameInicio;
    
    private volatile int FrameActual;
    private volatile boolean Detener = false;
    private volatile boolean Pausa = false;
    
    private AdvancedPlayer Player;
    private VolumeAudioDevice Dispositivo;
    
    private volatile long PlayToken = 0;

    public Mp3PlayerThread(File Archivo, int FrameInicio, Mp3Info Info) {
        this.Archivo = Archivo;
        this.Info = Info;
        this.FrameInicio = FrameInicio;
        setName("MP3-Player");
    }
    
    public boolean estaPausado() {
        return Pausa;
    }
    
    public int getFrameActual() {
        return FrameActual;
    }
    
    public int getFrameEstimadoVivo() {
        if (Dispositivo == null) {
            return FrameActual;
        }
        
        long avanzados = Dispositivo.getFrameswritten();
        long frames = FrameActual + avanzados;
        
        if (frames < 0) {
            frames = 0;
        }
        
        if (Info != null && frames > Info.TotalFrames) {
            frames = Info.TotalFrames;
        }
        
        return (int) frames;
    }
    
    public void Pausar() {
        Pausa = true;
        PlayToken++;
        
        if (Player != null) {
            Player.stop();
        }
    }
    
    public void Reanudar() {
        if (!Pausa) {
            return;
        }
        
        Pausa = false;
        
        new Thread(() -> ReproducirDesde(FrameActual), "MP3-Resume").start();
    }
    
    public void Detener() {
        Detener = true;
        PlayToken++;
        
        if (Player != null) {
            Player.stop();
        }
    }
    
    @Override
    public void run() {
        ReproducirDesde(FrameInicio);
    }
    
    public void SaltarA(int framedestino) {
        if (Info != null) {
            if (framedestino < 0) {
                framedestino = 0;
            }
            
            if (framedestino > Info.TotalFrames) {
                framedestino = Info.TotalFrames;
            }
        }
        
        final int destino = framedestino;
        
        Pausa = false;
        Detener = false;
        
        PlayToken++;
        
        if (Player != null) {
            Player.stop();
        }
        
        new Thread(() -> ReproducirDesde(destino), "MP3-Seek").start();
    }
    
    private void ReproducirDesde(int desdeframe) {
        final long token = PlayToken;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(Archivo));
            Dispositivo = new VolumeAudioDevice(Info.Channels);
            Dispositivo.reset();
            
            Player = new AdvancedPlayer(bis, Dispositivo);
            
            final int inicio = desdeframe;
            FrameActual = desdeframe; //Base para el calculo en vivo
            
            Player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent e) {
                    if (token != PlayToken) {
                        return;
                    }
                    
                    FrameActual = inicio + e.getFrame();
                }
            });
            
            Player.play(desdeframe, Integer.MAX_VALUE);
            
        } catch (Exception ignorar) {
        } finally {
            Player = null;
            Dispositivo = null;
        }
    }
}

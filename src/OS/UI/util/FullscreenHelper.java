/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI.util;

/**
 *
 * @author Hp
 */

import javax.swing.*;
import java.awt.*;

public class FullscreenHelper {
    
    private static final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static boolean fullscreen = false;
    
    public static void enter(JFrame frame) {
        if (fullscreen) {
            return;
        }
        
        frame.dispose();
        frame.setUndecorated(true);
        frame.setResizable(false);
        
        device.setFullScreenWindow(frame);
        fullscreen = true;
    }
    
    public static void exit(JFrame frame) {
        if (!fullscreen) {
            return;
        }
        
        device.setFullScreenWindow(null);
        
        frame.dispose();
        frame.setUndecorated(false);
        frame.setResizable(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        
        fullscreen = false;
    }
    
    public static void toggle(JFrame frame) {
        if (fullscreen) {
            exit(frame);
        } else {
            enter(frame);
        }
    }
    
    public static boolean isFullscreen() {
        return fullscreen;
    }
}

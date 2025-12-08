/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI.util;

/**
 *
 * @author Hp
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;

public final class NeonSliderUI extends BasicSliderUI {
    
    public NeonSliderUI(JSlider b) {
        super(b);
    }
    
    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int y = trackRect.y = trackRect.height/2 - 2;
        int x = trackRect.x + 6;
        int w = trackRect.width - 12;
        
        //Base
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.fillRoundRect(x, y, w, 4, 8, 8);
        
        //Progreso
        double frac = (slider.getValue() - slider.getMinimum()) * 1.0 / (slider.getMaximum() - slider.getMinimum());
        int pw = (int) Math.abs(w * frac);
        g2d.setColor(new Color(92, 160, 255));
        g2d.fillRoundRect(x, y, Math.max(6, pw), 4, 8, 8);
        
        //Brillo sutil
        g2d.setComposite(AlphaComposite.SrcOver.derive(0.35f));
        g2d.setColor(new Color(92, 160, 255));
        g2d.fillRoundRect(x, y - 2, Math.max(6, pw), 8, 10, 10);
        g2d.dispose();
    }
    
    @Override
    protected Dimension getThumbSize() {
        return new Dimension(14, 14);
    }
    
    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Rectangle rec = thumbRect;
        
        g2d.setColor(new Color(230, 235, 245));
        g2d.fillOval(rec.x, rec.y, rec.width, rec.height);
        
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.drawOval(rec.x, rec.y, rec.width, rec.height);
        g2d.dispose();
    }
}

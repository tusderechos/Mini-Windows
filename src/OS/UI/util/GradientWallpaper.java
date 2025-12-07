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

public class GradientWallpaper extends JPanel {
    
    private Color color1 = new Color(30, 30, 36);
    private Color color2 = new Color(18, 18, 22);
    
    private transient Image BackgroundImg; //transient es un modificador que se usa con serializacion, que le indica a la jvm que este atributo especifico no deberia ser incluido en el proceso de serializacion
    
    private final Color HighlightTop = new Color(255, 255, 255, 22);
    private final Color SombraBottom = new Color(0, 0, 0, 120);
    private final Color OverlayEnImg = new Color(0, 0, 0, 60);
    
    public GradientWallpaper() {
        setOpaque(true);
    }
    
    public void setGradient(Color top, Color botton) {
        color1 = top;
        color2 = botton;
        repaint();
    }
    
    public void setBackGroundImg(Image img) {
        BackgroundImg = img;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final int w = getWidth();
        final int h = getHeight();
        
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        if (BackgroundImg != null) {
            //Rellenar manteniendo proporcion
            double iw = BackgroundImg.getWidth(this);
            double ih = BackgroundImg.getHeight(this);
            
            if (iw > 0 && ih > 0) {
                double s = Math.max(w / iw, h / ih);
                int dw = (int) Math.round(iw * s);
                int dh = (int) Math.round(ih * s);
                
                int dx = (w - dw) / 2;
                int dy = (h - dh) / 2;
                
                g2d.drawImage(BackgroundImg, dx, dy, dw, dh, this);
            }
            
            //Velo para mejorar contraste del texto
            if (OverlayEnImg != null && OverlayEnImg.getAlpha() > 0) {
                g2d.setColor(OverlayEnImg);
                g2d.fillRect(0, 0, w, h);
            }
        } else {
            //Gradiente vertical
            g2d.setPaint(new GradientPaint(0, 0, color1, 0, h, color2));
            g2d.fillRect(0, 0, w, h);
        }
        
        //Highlight arriba y una pequena sombra abajo
        g2d.setColor(HighlightTop);
        g2d.drawLine(0, 0, w, 0);
        
        g2d.setColor(SombraBottom);
        g2d.drawLine(0, h - 1, w, h - 1);
        
        g2d.dispose();
    }
}

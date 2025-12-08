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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class IconTile extends JPanel {
    
    private final Color BG = TemaOscuro.CARD;
    private final Color BG_HOVER = new Color(Math.min(255, BG.getRed() + 10), Math.min(255, BG.getGreen() + 10), Math.min(255, BG.getBlue() + 10));
    
    public IconTile(String titulo, String iconpath, Runnable action) {
        setOpaque(true);
        setBackground(BG);
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(TemaOscuro.LINEA), new EmptyBorder(16, 18, 14, 18)));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setLayout(new BorderLayout(0, 8));
        setPreferredSize(new Dimension(180, 150));
        
        JLabel icon = new JLabel(LoadIcon(iconpath), SwingConstants.CENTER);
        JLabel texto = new JLabel(titulo, SwingConstants.CENTER);
        texto.setForeground(TemaOscuro.TEXTO);
        texto.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        add(icon, BorderLayout.CENTER);
        add(texto, BorderLayout.SOUTH);
        
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(BG_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(BG);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(BG_HOVER);
                action.run();
            }
        };
        
        addMouseListener(ma);
        icon.addMouseListener(ma);
        texto.addMouseListener(ma);
    }
    
    private Icon LoadIcon(String path) {
        try {
            URL url = getClass().getClassLoader().getResource(path);
            
            if (url != null) {
                Image img = new ImageIcon(url).getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception ignorar) {
        }
        
        BufferedImage bi = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.fillRoundRect(4, 4, 56, 56, 16, 16);
        g2d.dispose();
        
        return new ImageIcon(bi);
    }
}

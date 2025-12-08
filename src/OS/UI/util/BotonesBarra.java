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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class BotonesBarra extends JButton {
    
    private final Color BG = new Color(0, 0, 0, 0);
    private final Color Hover = new Color(255, 255, 255, 18);
    private final Color Press = new Color(255, 255, 255, 28);
    
    public BotonesBarra(String tooltip, String iconpath, Runnable action) {
        setToolTipText(tooltip);
        setFocusPainted(false);
        setFocusable(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setBorder(new EmptyBorder(6, 8, 6, 8));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setForeground(TemaOscuro.TEXTO);
        setBackground(BG);
        setIcon(load(iconpath, 22));
        setText(null);
        setIconTextGap(0);
        
        addActionListener(e -> action.run());
        
        getModel().addChangeListener(e -> {
            ButtonModel modelo = (ButtonModel) e.getSource();
            if (modelo.isPressed()) {
                setBackground(Press);
            } else if (modelo.isRollover()) {
                setBackground(Hover);
            } else {
                setBackground(BG);
            }
            repaint();
        });
    }
    
    private Icon load(String path, int tamano) {
        try {
            URL url = getClass().getClassLoader().getResource(path);
            
            if (url == null) {
                return null;
            }
            
            Image img = new ImageIcon(url).getImage().getScaledInstance(tamano, tamano, Image.SCALE_SMOOTH);
            
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getBackground());
        g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);
        g2d.dispose();
        super.paintComponent(g);
    }
    
    @Override
    public void updateUI() {
        super.updateUI();
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
    }
}
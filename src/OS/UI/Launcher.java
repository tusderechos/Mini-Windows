/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI;

/**
 *
 * @author Hp
 */

import javax.swing.*;
import OS.Core.SistemaOperativo;

public class Launcher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaOperativo so = new SistemaOperativo();
            new Login(so).setVisible(true);
        });
    }
}

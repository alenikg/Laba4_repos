
package main;

import javax.swing.SwingUtilities;

import dao.DBUtils;
import view.MainFrame;

/**
 *
 * @author elenagoncarova
 */
public class Laba4_MagicShop {

    public static void main(String[] args) {
        DBUtils.initializeDatabase();
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}


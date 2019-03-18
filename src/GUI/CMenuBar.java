package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


/**
 * this class made menu bar of JProxy application
 *
 * @author Ali ArjomandBigdeli
 * @since 3.18.2018
 */
public class CMenuBar {
    private JMenuItem[] menuItems;
    private JMenuItem aboutItem;

    public CMenuBar(JFrame jFrame) {
        JMenu fileMenu = new JMenu("Actions"); // create file menu
        fileMenu.setMnemonic(KeyEvent.VK_D);
        menuItems = new JMenuItem[4];
        menuItems[0] = new JMenuItem("Run/Pause", KeyEvent.VK_R);
        menuItems[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        menuItems[1] = new JMenuItem("Add Category", KeyEvent.VK_C);
        menuItems[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        menuItems[2] = new JMenuItem("Add Domain", KeyEvent.VK_D);
        menuItems[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        menuItems[3] = new JMenuItem("Exit", KeyEvent.VK_E);
        menuItems[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));

        for (int i = 0; i < 4; i++) {
            menuItems[i].setActionCommand(menuItems[i].getText());
            fileMenu.add(menuItems[i]);
        }

        JMenu helpMenu = new JMenu("Help"); // create file menu
        helpMenu.setMnemonic(KeyEvent.VK_H);
        aboutItem = new JMenuItem("About", KeyEvent.VK_B);
        aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
        aboutItem.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(jFrame, "Author: Ali ArjomandBigdeli\n" +
                            "Student number: 9423008\nSpring 2019"
                    , "About", JOptionPane.INFORMATION_MESSAGE);
            StringSelection stringSelection = new StringSelection("");
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });
        helpMenu.add(aboutItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        jFrame.setJMenuBar(menuBar);
    }


    public void setActionListener(JProxy.CListenerHandler listener) {
        for (int i = 0; i < 4; i++) {
            menuItems[i].addActionListener(listener);
        }
        aboutItem.addActionListener(listener);
    }
}

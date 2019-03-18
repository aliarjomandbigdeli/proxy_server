package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * this runs main frame of JProxy application
 *
 * @author Ali ArjomandBigdeli
 * @since 3.18.2018
 */
public class JProxy {
    private static JProxy INSTANCE = null;
    private JFrame mainFrame;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JLabel statusLabel;
    private JToolBar categoryToolBar;   //Category tool bar
    private JToolBar dToolBar;  //url domain tool bar

    private JProxy(String title) {
//        Utils.setCategoryList(new ArrayList<>());
//        Utils.setQueueList(new ArrayList<>());
//        Utils.setCompletedList(new ArrayList<>());
//        Utils.setRemovedList(new ArrayList<>());
//        Utils.setQueueListQ(new LinkedList<>());
//        Utils.setWaitedDownloadsQ(new LinkedList<>());

//        Utils.setFilterList(new ArrayList<>());

//        readInfo();

//        Utils.setShowDList(Utils.getCategoryList());

        setLookAndFeel();

        mainFrame = new JFrame(title);
        mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        ImageIcon iconImg = new ImageIcon("icons/Icon.png");
        mainFrame.setIconImage(iconImg.getImage());

        CListenerHandler listenerHandler = new CListenerHandler();

        CMenuBar cMenuBar = new CMenuBar(mainFrame);
        cMenuBar.setActionListener(listenerHandler);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainFrame.setContentPane(mainPanel);

        //left panel
        leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(new Color(50, 54, 63));
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(new ImageIcon("icons/logo.png"));
        logoLabel.setSize(new Dimension(180, 180));
        leftPanel.add(logoLabel, BorderLayout.NORTH);
        //add category toolbar
        initCategoryToolBar(listenerHandler);

        //right panel
        rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(231, 239, 251));
        initTopToolBar(listenerHandler);

        initScrollPanel();

        mainPanel.add(rightPanel, BorderLayout.CENTER);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        initSystemTray();
    }

    public static JProxy getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new JProxy("JProxy");
        }
        return INSTANCE;
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    /**
     * locate top tool bar in main Frame
     *
     * @param listenerHandler
     */
    private void initTopToolBar(CListenerHandler listenerHandler) {
        int numOfBtn = 4;
        JToolBar topToolBar = new JToolBar();
        JButton[] toolbarBtn = new JButton[numOfBtn];
        ImageIcon[] imageIcon = new ImageIcon[numOfBtn];
        for (int i = 0; i < numOfBtn; i++) {
            toolbarBtn[i] = new JButton();
        }
        imageIcon[0] = new ImageIcon("icons/Play.png");
        toolbarBtn[0].setToolTipText("Run/Pause");
        imageIcon[1] = new ImageIcon("icons/addCat.png");
        toolbarBtn[1].setToolTipText("Add Category");
        imageIcon[2] = new ImageIcon("icons/addDomain.png");
        toolbarBtn[2].setToolTipText("Add Domain");
        imageIcon[3] = new ImageIcon("icons/exit.png");
        toolbarBtn[3].setToolTipText("Exit");

        statusLabel = new JLabel("<html> Status : OFF<BR> Port : <BR> IP : 127.0.0.1<BR>Select checkboxes to allow traffic</html>");
        statusLabel.setForeground(Color.GRAY);

        for (int i = 0; i < numOfBtn; i++) {
            toolbarBtn[i].setIcon(imageIcon[i]);
            toolbarBtn[i].setActionCommand(toolbarBtn[i].getToolTipText());
            toolbarBtn[i].addActionListener(listenerHandler);
            toolbarBtn[i].setBackground(new Color(208, 223, 248));
            if (i == 3) {
                topToolBar.addSeparator();
                topToolBar.add(statusLabel);
                topToolBar.addSeparator();
            }
            topToolBar.add(toolbarBtn[i]);
        }

        topToolBar.setBackground(new Color(208, 223, 248));

        rightPanel.add(topToolBar, BorderLayout.NORTH);
    }

    private void initCategoryToolBar(CListenerHandler listenerHandler) {
        categoryToolBar = new JToolBar(1);
        categoryToolBar.setBackground(new Color(231, 239, 251));
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(new Color(231, 239, 251));

        progressPanel.add(categoryToolBar, BorderLayout.NORTH);

        JPanel scrollPanel = new JPanel(new BorderLayout());
        scrollPanel.add(new JScrollPane(progressPanel), BorderLayout.CENTER); //should add to CENTER
        leftPanel.add(scrollPanel, BorderLayout.CENTER);
        categoryToolBar.setBackground(new Color(50, 54, 63));

        categoryToolBar.removeAll();
//        for (DItemPanel dItemPanel : Utils.getShowDList()) {
//            categoryToolBar.addSeparator(new Dimension(0, 5));
//            categoryToolBar.add(dItemPanel);
//        }
        leftPanel.validate();
        leftPanel.repaint();
    }

    private void initSystemTray() {
        TrayIcon trayIcon;
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            Image image = Toolkit.getDefaultToolkit().getImage("Icon.png");
            MenuItem exitItem = new MenuItem("Exit");
            // create a action listener to listen for default action executed on the tray icon
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // execute default action of the application
                    mainFrame.setVisible(true);
                    if (e.getSource().equals(exitItem)) {
//                        saveInfo();
                        System.exit(0);
                    }

                }
            };
            // create a popup menu
            PopupMenu popup = new PopupMenu();
            // create menu item for the default action

            //exitItem = new MenuItem("Exit");
            exitItem.addActionListener(listener);
            popup.add(exitItem);

            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "JProxy", popup);
            // set the TrayIcon properties
            trayIcon.addActionListener(listener);
            // ...
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
            // ...
        } else {
            // disable tray option in your application or
            // perform other actions
            System.out.println("unable to load system tray");
        }
    }

    private void initScrollPanel() {
        dToolBar = new JToolBar(1);
        dToolBar.setBackground(new Color(231, 239, 251));
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(new Color(231, 239, 251));

        progressPanel.add(dToolBar, BorderLayout.NORTH);

        JPanel scrollPanel = new JPanel(new BorderLayout());
        scrollPanel.add(new JScrollPane(progressPanel), BorderLayout.CENTER); //should add to CENTER
        rightPanel.add(scrollPanel, BorderLayout.CENTER);

        updateDownloadList();
    }

    public void showGUI() {
        System.out.println(mainFrame.getPreferredSize());
        mainFrame.setMinimumSize(new Dimension(480 + 250, 370 + 50));
        mainFrame.setPreferredSize(new Dimension(480 + 295, 370 + 150));
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public void updateDownloadList() {
        dToolBar.removeAll();
//        for (DItemPanel dItemPanel : Utils.getShowDList()) {
//            dToolBar.addSeparator(new Dimension(0, 5));
//            dToolBar.add(dItemPanel);
//        }
        rightPanel.validate();
        rightPanel.repaint();
    }

//    private void readInfo() {
//        File file1 = new File(Utils.getdInfoPath() + "list.jdm");
//        if (file1.exists()) {
//            ArrayList<DItemInfo> processingData;
//            processingData = Utils.fileReader(file1);
//            for (DItemInfo dItemInfo : processingData) {
//                DItemPanel dItemPanel = new DItemPanel(dItemInfo);
//                if (Utils.getCategoryList().size() > 0)
//                    dItemPanel.setIndex(Utils.getCategoryList().size());
//                Utils.getCategoryList().add(dItemPanel);
////                setDItemMouseListener(dItemPanel);
//            }
//        }
//
//        File file5 = new File(Utils.getdInfoPath() + "filter.jdm");
//        if (file5.exists()) {
//            Utils.setFilterList(Utils.fileReader(file5));
//            StringBuilder str = new StringBuilder();
//            for (int i = 0; i < Utils.getFilterList().size(); i++) {
//                if (i % 3 == 2 || Utils.getFilterList().get(i).length() > 30)
//                    str.append(Utils.getFilterList().get(i)).append("\n");
//                else
//                    str.append(Utils.getFilterList().get(i)).append(" ");
//            }
//            Utils.setFilterSitesString(str.toString());
//        }
//
//    }

//    private void saveInfo() {
//        ArrayList<DItemInfo> processingData = new ArrayList<>();
//        for (DItemPanel dItemPanel : Utils.getCategoryList()) {
//            processingData.add(dItemPanel.getDItemInfo());
//        }
//        Utils.fileWriter("list.jdm", processingData);
//
//        Utils.fileWriter("settings.jdm", SettingsField.getINSTANCE());
//
//        Utils.fileWriter("filter.jdm", Utils.getFilterList());
//    }

    public class CListenerHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Exit":
//                    saveInfo();
                    System.exit(0);
//                case "Add":
//                    new AddFrame();
//                    break;
                case "Run/Pause":
                    // TODO: complete
                    break;
                case "Add Category":
                    // TODO: complete
                    break;
                case "Add Domain":
                    new AddLinkFrame();
                    break;
            }
        }

    }

}


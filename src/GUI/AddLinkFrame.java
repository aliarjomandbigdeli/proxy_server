package GUI;

import Logic.Backend;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * this class opens Add new url domain frame of JProxy application
 *
 * @author Ali ArjomandBigdeli
 * @since 3.18.2018
 */
public class AddLinkFrame extends JFrame implements ActionListener {
    private JComboBox<String> categoryCombo;
    private JPanel mainPanel;
    private JTextField linkTxtFld;
    private JButton cancelBtn;
    private JButton okBtn;
    private String linkStr;
    private Backend backend;

    public AddLinkFrame(Backend backend) {
        super("Add new URL domain");
        this.backend = backend;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        ImageIcon iconImg = new ImageIcon("icons/Icon.png");
        setIconImage(iconImg.getImage());

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(5, 20, 5, 10));
        setContentPane(mainPanel);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        JPanel downPanel = new JPanel(new BorderLayout(10, 10));

        JPanel textsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        //textPanel components:1
        JPanel linkAddPanel = new JPanel(new FlowLayout());
        linkTxtFld = new JTextField("http://");
        linkTxtFld.setPreferredSize(new Dimension(390, linkTxtFld.getPreferredSize().height));
        JLabel linkAddLabel = new JLabel();
        linkAddLabel.setIcon(new ImageIcon("icons/linkIcon.png"));
        linkAddPanel.add(linkAddLabel);
        linkAddPanel.add(linkTxtFld);
        //textPanel components:2
        FlowLayout catFlowLayout = new FlowLayout();
        catFlowLayout.setAlignment(FlowLayout.LEFT);
        JPanel categoryPanel = new JPanel(catFlowLayout);
        categoryCombo = new JComboBox();
        categoryCombo.setPreferredSize(new Dimension(100, categoryCombo.getPreferredSize().height));

        for (int i = 0; i < backend.categories.size(); ++i)
            categoryCombo.addItem(((CategoryItem) (backend.categories.get(i))).getCategoryName());

        JLabel categoryLabel = new JLabel();
        categoryLabel.setIcon(new ImageIcon("icons/category.png"));
        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryCombo);


        textsPanel.add(linkAddPanel);
        textsPanel.add(categoryPanel);

        //topPanel Center components:
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);


        topPanel.add(textsPanel, BorderLayout.NORTH);


        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        JPanel downBtnPanel = new JPanel(flowLayout);
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(this);
        okBtn = new JButton("OK");
        okBtn.addActionListener(this);
        downBtnPanel.add(cancelBtn);
        downBtnPanel.add(okBtn);

        downPanel.add(downBtnPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(downPanel, BorderLayout.SOUTH);

        //show
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelBtn)) {
            dispose();
        } else if (e.getSource().equals(okBtn)) {
//            linkStr = linkTxtFld.getText();
            checkEnteredURL();
            dispose();
        }
    }

    public void checkEnteredURL() {
        boolean hasErr = false;
        String text = linkTxtFld.getText();
        if (text.equals(""))
            hasErr = true;
        if (!hasErr) {
            if (text.contains("http://")) {
                if (text.length() <= 7)
                    hasErr = true;
            } else {
                hasErr = true;
            }
        }
        if (hasErr) {
            JOptionPane.showMessageDialog(null, "invalid url", "alert", JOptionPane.ERROR_MESSAGE);
        } else {
            backend.addNewUrl(text, categoryCombo.getSelectedIndex());
            this.dispose();
        }
    }
}


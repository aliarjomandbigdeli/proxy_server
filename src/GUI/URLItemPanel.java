package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * this class designs gui of every url domain item panel
 *
 * @author Ali ArjomandBigdeli
 * @since 3.18.2018
 */
public class URLItemPanel extends ListItem {
    public JCheckBox selectChk;
    private JTextField urlText;
    private int textWidth;
    public JButton deleteBTN;
    private String UrlAddress;
    private String rawUrl; //this url does not have http://

    public URLItemPanel(Dimension dim, String urlAdd) {
        super(dim);
        setLayout(new BorderLayout(5, 5));
        textWidth = (int)(dim.width * 0.9);
        UrlAddress = urlAdd;
        initComponent();
        setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black));
    }

    private void initComponent() {
        //init component
        selectChk = new JCheckBox();
        urlText = new JTextField(UrlAddress);
        deleteBTN = new JButton(new ImageIcon("./icons/remove.png"));

        JPanel elementsPanel = new JPanel(new BorderLayout(5, 5));
        JPanel linkTxtPanel = new JPanel(new BorderLayout(5, 5));


        deleteBTN.setFocusPainted(false);
        //add to layout
        linkTxtPanel.add(urlText, BorderLayout.NORTH);

        elementsPanel.add(selectChk, BorderLayout.WEST);
        elementsPanel.add(linkTxtPanel, BorderLayout.CENTER);
        elementsPanel.add(deleteBTN, BorderLayout.EAST);

        add(elementsPanel, BorderLayout.NORTH);

        //define size
        deleteBTN.setPreferredSize(new Dimension(24, 24));
        urlText.setPreferredSize(new Dimension(textWidth, urlText.getPreferredSize().height));

        deleteBTN.setContentAreaFilled(false);
        deleteBTN.setBorderPainted(false);
        deleteBTN.setOpaque(false);
        deleteBTN.addMouseListener(this);

    }

    public void setUrlText(String txt) {
        urlText.setText(txt);
    }

    public String getUrlAddress() {
        return urlText.getText();
    }

    public void setRawUrl(String inpUrl) {
        rawUrl = inpUrl;
    }

    public String getRawUrl() {
        return rawUrl;
    }

}


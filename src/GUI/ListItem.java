package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * this class designs gui of every item panel
 *
 * @author Ali ArjomandBigdeli
 * @since 3.18.2018
 */
public class ListItem extends JPanel implements MouseListener {

    protected Dimension panelDim;
    protected SpringLayout sp;

    public ListItem(){
        this(new Dimension(100,50));
    }
    public ListItem(Dimension dim){
        super();
        panelDim = dim;
        sp = new SpringLayout();
        addMouseListener(this);
        setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black));
    }

    private void setupComponent(){}

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

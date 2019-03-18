package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ListItem extends JPanel implements MouseListener {

    protected Dimension panelDim;
    protected SpringLayout sp;

    public ListItem(){
        this(new Dimension(100,50));
    }
    public ListItem(Dimension dim){
        super();
        this.setPreferredSize(dim);
        panelDim = dim;
        sp = new SpringLayout();
        setLayout(sp);
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

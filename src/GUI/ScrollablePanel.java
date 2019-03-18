package GUI;

import Models.Backend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class ScrollablePanel extends JPanel implements MouseListener , ActionListener {
    protected JScrollPane pane;
    public ListItemPanel itemsList;
    protected SpringLayout sp;
    protected Dimension screenSize;
    public Backend back;

    public ScrollablePanel(){
        this(new Dimension(100,100), null);
    }

    public ScrollablePanel(Dimension dim, Backend back){
        super(new BorderLayout());
        screenSize = dim;
        setPreferredSize(dim);
        this.back = back;
        itemsList = new ListItemPanel(dim);
        add(itemsList,BorderLayout.CENTER);
        pane = new JScrollPane(itemsList);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(pane,BorderLayout.CENTER);
        sp = itemsList.sp;
    }

    public void setSelected(MouseEvent e, int catid){

    }
    public void setCheckBoxStatus(ActionEvent e){

    }
    public void deleteUrl(MouseEvent e){

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        setSelected(e, -1);
        deleteUrl(e);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        setCheckBoxStatus(e);
    }
}

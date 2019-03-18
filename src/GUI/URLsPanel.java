package GUI;

import Models.Backend;

import java.awt.*;
import java.awt.event.MouseEvent;


public class URLsPanel extends ScrollablePanel {
    public URLsPanel(Dimension dim, Backend back) {
        super(dim, back);
        this.setBackground(Color.CYAN);
        //addTestComponent();
    }

    public void deleteUrl(MouseEvent e){
        for (int i = 0; i < back.urls.get(back.currentSelectedCategory).size(); ++i){
            if(e.getSource().equals(
                    ((urlItem)back.urls.get(back.currentSelectedCategory).get(i)).deleteBTN)){
                back.deleteURL(back.currentSelectedCategory, i);
                break;
            }
        }
    }
}

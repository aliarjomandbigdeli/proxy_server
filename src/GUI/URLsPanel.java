package GUI;

import Logic.Backend;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * this class designs gui of whole url domain list items
 *
 * @author Ali ArjomandBigdeli
 * @since 3.18.2018
 */
public class URLsPanel extends ScrollablePanel {
    public URLsPanel(Dimension dim, Backend back) {
        super(dim, back);
        this.setBackground(Color.CYAN);
    }

    public void deleteUrl(MouseEvent e){
        for (int i = 0; i < back.urls.get(back.currentSelectedCategory).size(); ++i){
            if(e.getSource().equals(
                    ((URLItemPanel)back.urls.get(back.currentSelectedCategory).get(i)).deleteBTN)){
                back.deleteURL(back.currentSelectedCategory, i);
                break;
            }
        }
    }
}

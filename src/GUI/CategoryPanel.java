package GUI;

import Logic.Backend;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * this class designs gui of whole category list items
 *
 * @author Ali ArjomandBigdeli
 * @since 3.18.2018
 */
public class CategoryPanel extends ScrollablePanel {

    public CategoryPanel(Dimension dim, Backend back) {
        super(dim, back);
        this.setBackground(Color.BLUE);
        //addTestComponent();
    }

    public void setSelected(MouseEvent e, int catid) {
        if (e == null) {
            setBackgroundColorOfSelected(catid);
        } else {
            for (int i = 0; i < back.categories.size(); ++i) {
                if (e.getSource().equals(back.categories.get(i))) {
                    setBackgroundColorOfSelected(i);
                    break;
                }
            }
        }

    }

    private void setBackgroundColorOfSelected(int id) {
        back.currentSelectedCategory = id;
        for (int i = 0; i < back.categories.size(); ++i) {
            if (i == id) {
//                back.categories.get(i).setBackground(new Color(249, 132, 132));
                back.categories.get(i).setBackground(Color.gray);
//                ((CategoryItem) (back.categories.get(i))).selectChk.setBackground(new Color(249, 132, 132));
                ((CategoryItem) (back.categories.get(i))).selectChk.setBackground(Color.gray);
                back.ui.urlsPanel.itemsList.updateListView(back.urls.get(i));
            } else {
                back.categories.get(i).setBackground(new Color(240, 240, 240));
                ((CategoryItem) (back.categories.get(i))).selectChk.setBackground(new Color(240, 240, 240));
            }
        }
    }

    public void setCheckBoxStatus(ActionEvent e) {
        for (int i = 0; i < back.categories.size(); ++i) {
            if (((CategoryItem) back.categories.get(i)).selectChk.equals(e.getSource())) {
                if (((CategoryItem) back.categories.get(i)).selectChk.isSelected()) {
                    for (int j = 0; j < back.urls.get(i).size(); ++j)
                        ((URLItemPanel) back.urls.get(i).get(j)).selectChk.setSelected(true);
                } else {
                    for (int j = 0; j < back.urls.get(i).size(); ++j)
                        ((URLItemPanel) back.urls.get(i).get(j)).selectChk.setSelected(false);
                }
                break;
            }
        }
    }

}

package GUI;

import Models.Backend;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;


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
//                ((categoryItem) (back.categories.get(i))).selectChk.setBackground(new Color(249, 132, 132));
                ((categoryItem) (back.categories.get(i))).selectChk.setBackground(Color.gray);
                back.ui.urlsPanel.itemsList.updateListView(back.urls.get(i));
            } else {
                back.categories.get(i).setBackground(new Color(240, 240, 240));
                ((categoryItem) (back.categories.get(i))).selectChk.setBackground(new Color(240, 240, 240));
            }
        }
    }

    public void setCheckBoxStatus(ActionEvent e) {
        for (int i = 0; i < back.categories.size(); ++i) {
            if (((categoryItem) back.categories.get(i)).selectChk.equals(e.getSource())) {
                if (((categoryItem) back.categories.get(i)).selectChk.isSelected()) {
                    for (int j = 0; j < back.urls.get(i).size(); ++j)
                        ((urlItem) back.urls.get(i).get(j)).selectChk.setSelected(true);
                } else {
                    for (int j = 0; j < back.urls.get(i).size(); ++j)
                        ((urlItem) back.urls.get(i).get(j)).selectChk.setSelected(false);
                }
                break;
            }
        }
    }

//    private void addTestComponent(){
//        ArrayList<ListItem> mm = new ArrayList<>();
//        for (int i = 0; i < 100; i++){
//            ListItem li = new categoryItem(new Dimension(screenSize.width, itemH));
//
//            itemsList.add(li);
//            if(i == 0)
//                sp.putConstraint(SpringLayout.NORTH, li, 2, SpringLayout.NORTH, itemsList);
//            else
//                sp.putConstraint(SpringLayout.NORTH, li, 2, SpringLayout.SOUTH, mm.get(mm.size() - 1));
//            sp.putConstraint(SpringLayout.WEST, li, 0, SpringLayout.WEST, itemsList);
//            ((categoryItem)li).setLabelText(Integer.toString(i));
//            mm.add(li);
//        }
//        if(mm.size() * itemH > screenSize.height){
//            itemsList.setPreferredSize(new Dimension(screenSize.width, mm.size() * (itemH + 2) + 5));
//        }
//    }

}

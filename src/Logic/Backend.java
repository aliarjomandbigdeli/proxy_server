package Logic;

import GUI.CategoryItem;
import GUI.JProxy;
import GUI.ListItem;
import GUI.URLItemPanel;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * this class handles logic of the program
 *
 * @author Ali ArjomandBigdeli
 * @since 3.18.2018
 */
public class Backend {
    private static ArrayList<String> blockedList;
    public static ArrayList<ListItem> categories;
    public static ArrayList<ArrayList<ListItem>> urls;
    private Dimension screenDimention;
    private int itemH;
    private int categoryWidth;
    private int urlWidth;
    public JProxy ui;
    public int currentSelectedCategory;
    public Runnable r;

    public Backend(Dimension screenD, int urlW, int categoryW, JProxy gui) {
        categories = new ArrayList<>();
        urls = new ArrayList<>();
        blockedList = new ArrayList<>();
        ui = gui;
        screenDimention = screenD;
        itemH = 50;
        currentSelectedCategory = -1;
        categoryWidth = categoryW;
        urlWidth = urlW;
    }

    /**
     * add new category
     *
     * @param categoryName
     */
    public void addCategory(String categoryName) {
        categories.add(new CategoryItem(new Dimension(categoryWidth, itemH), categoryName));
        categories.get(categories.size() - 1).addMouseListener(ui.categoriesPanel);
        ((CategoryItem) (categories.get(categories.size() - 1))).selectChk.addActionListener(ui.categoriesPanel);
        ArrayList<ListItem> tmpUrl = new ArrayList<>();
        urls.add(tmpUrl);
        ui.categoriesPanel.itemsList.updateListView(categories);
    }

    /**
     * add new url to specified category
     *
     * @param url
     * @param categoryID
     */
    public void addNewUrl(String url, int categoryID) {
        ListItem tmpURL = new URLItemPanel(new Dimension(urlWidth, itemH), url);
        urls.get(categoryID).add(tmpURL);
        ui.urlsPanel.itemsList.updateListView(urls.get(categoryID));
        ((URLItemPanel) tmpURL).deleteBTN.addMouseListener(ui.urlsPanel);
        ((URLItemPanel) tmpURL).setRawUrl(prepareBlockedURL(url));
        ((URLItemPanel) tmpURL).selectChk.setEnabled(false);
        if (((CategoryItem) (categories.get(categoryID))).selectChk.isSelected()) {
            ((URLItemPanel) tmpURL).selectChk.setSelected(true);
        } else {
            blockedList.add(((URLItemPanel) tmpURL).getRawUrl());
        }
        ui.categoriesPanel.setSelected(null, categoryID);
    }

    /**
     * delete url from specified category
     *
     * @param catID
     * @param UrlID
     */
    public void deleteURL(int catID, int UrlID) {
        blockedList.remove(((URLItemPanel) urls.get(catID).remove(UrlID)).getRawUrl());
        urls.get(catID).remove(UrlID);
        ui.urlsPanel.itemsList.updateListView(urls.get(catID));
    }

    /**
     * load saved categories and all urls belong to it
     */
    public void loadDataFromFile() {
        Path p = Paths.get("./AppData");
        if (Files.exists(p)) {
            final File folder = new File("./AppData");
            for (final File fileEntry : folder.listFiles()) {
                //create category
                addCategory(fileEntry.getName().substring(0, fileEntry.getName().lastIndexOf('.')));
                int catID = categories.size() - 1;
                //add urls
                try (BufferedReader br = new BufferedReader(new FileReader(fileEntry.getAbsolutePath()))) {
                    for (String line; (line = br.readLine()) != null; ) {
                        addNewUrl(line, catID);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * creat a text file for each category and save all urls belong to it line by line
     *
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void saveDataToFile() throws FileNotFoundException, UnsupportedEncodingException {
        final File folder = new File("./AppData");
        for (final File fileEntry : folder.listFiles())
            fileEntry.delete();

        for (int i = 0; i < categories.size(); i++) {
            PrintWriter writer = new PrintWriter("./AppData/" + ((CategoryItem) categories.get(i)).getCategoryName() + ".txt", "UTF-8");
            for (int j = 0; j < urls.get(i).size(); j++)
                writer.println(((URLItemPanel) urls.get(i).get(j)).getUrlAddress());
            writer.close();
        }
    }

    public static boolean checkValidityOfURL(String address) {
        //first we remove http://www. from url is exist
        String temp = prepareBlockedURL(address);

        //check url among all urls
        for (int i = 0; i < urls.size(); i++) {
            for (int j = 0; j < urls.get(i).size(); j++) {
                String urlStr = ((URLItemPanel) urls.get(i).get(j)).getRawUrl();
                if (temp.contains(urlStr) || urlStr.contains(temp)) {
                    if (((URLItemPanel) urls.get(i).get(j)).selectChk.isSelected())
                        return false;
                    else
                        return true;
                }
            }
        }
        return false;
    }

    public void startProxy() {
        r = new SocketListener(this);
        Thread t1 = new Thread(r);
        t1.start();
    }

    public void stopProxy() throws IOException {
        ((SocketListener) r).stopServer();
    }

    private static String prepareBlockedURL(String inp) {
        String subInput = inp.substring(inp.indexOf('/') + 2);
        //check if sub string has www. we will trim it
        if (subInput.length() < 4) { // sub string has not www. and it's url is shorter than 4 character
            return subInput;
        } else {
            if (subInput.substring(0, 4).equals("www."))
                return subInput.substring(4);
            else
                return subInput;
        }
    }

    public void makeBlockedList() {
        blockedList.clear();
        for (int i = 0; i < categories.size(); i++) {
            if (!((CategoryItem) categories.get(i)).selectChk.isSelected()) {
                for (int j = 0; j < urls.get(i).size(); j++) {
                    if (!((URLItemPanel) urls.get(i).get(j)).selectChk.isSelected()) {
                        blockedList.add(((URLItemPanel) urls.get(i).get(j)).getRawUrl());
                    }
                }
            }
        }
    }
}

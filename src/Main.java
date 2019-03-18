import GUI.JProxy;

/**
 * this class is used to run GUI of this proxy
 *
 * @author Ali ArjomandBigdeli
 * @since 3.16.2019
 */

public class Main {

    public static void main(String[] args) {
        JProxy jProxy = JProxy.getINSTANCE();
        jProxy.showGUI();
    }
}

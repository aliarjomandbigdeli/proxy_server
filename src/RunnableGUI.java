import GUI.JProxy;

/**
 * this class allows us to run gui in a different thread
 *
 * @author Ali ArjomandBigdeli
 * @since 3.18.2018
 */
public class RunnableGUI {
    public RunnableGUI() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JProxy jProxy = JProxy.getINSTANCE();
                jProxy.showGUI();
            }
        });
    }
}



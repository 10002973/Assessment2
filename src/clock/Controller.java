package clock;

import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 * This class manages the user input, it listens for actions and then acts accordingly.
 * @author Heather Taylor-Stanley 10002973
 * @version "%I%, %G%"
 * 
 */
public class Controller {
    
    ActionListener listener;
    Timer timer;
    
    Model model;
    View view;
    
    /**
     * Checks for user actions and either updates the software or calls the saveAlarms method.
     * 
     * @param m     Contains model object.
     * @param v     Contains view object.
     */
    public Controller(Model m, View v) {
        model = m;
        view = v;
        
        listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.update();
            }
        };
        
        //When the frame is being closed, call saveAlarms to ask if user would like to save their alarms
        view.frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    view.saveAlarms();
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
             }
        });
        
        timer = new Timer(100, listener);
        timer.start();
    }
}
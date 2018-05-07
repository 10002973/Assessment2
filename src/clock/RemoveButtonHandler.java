/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;

/**
 * This class handles the remove alarm button, when the remove button or menu item is pressed this class calls the deleteAlarm function. 
 * @author Heather Taylor-Stanley 10002973
 * @version "%I%"
 * @date 07/05/2018
 * 
 */
public class RemoveButtonHandler implements ActionListener{
       
    Model model;
    View view;
    
    /**
     * RemoveButtonHandler constructor, initialises the model and view variables.
     * @param m     Contains the passed model object.
     * @param v     Contains the passed view object.
     */
    public RemoveButtonHandler(Model m, View v) {
        model = m;
        view = v;
    }
    
    /**
     * Calls the deleteAlarm method when the delete menu item or button is pressed.
     * @param event     Contains passed ActionEvent object.
     */
    public void actionPerformed(ActionEvent event) {
        try {
            view.deleteAlarm();
        } catch (QueueOverflowException | QueueUnderflowException | ParseException ex) {
            Logger.getLogger(RemoveButtonHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

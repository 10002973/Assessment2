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
 *
 * @author Heather Taylor-Stanley 10002973
 * 
 * This class handles the edit button. When the edit button or menu item is pressed, this class calls the editAlarm function. 
 */
public class EditButtonHandler implements ActionListener{
       
    Model model;
    View view;
    
    /**
     *
     * @param m
     * @param v
     */
    public EditButtonHandler(Model m, View v) {
        model = m;
        view = v;
    }
    
    /**
     * Calls the editAlarm method when the edit button or menu item is pressed.
     *
     * @param event
     */
    public void actionPerformed(ActionEvent event) {
        try {
            //Calls editAlarm method
            view.editAlarm();
        } catch (QueueOverflowException | QueueUnderflowException | ParseException ex) {
            Logger.getLogger(EditButtonHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;

/**
 * @author Heather Taylor-Stanley 10002973
 * 
 * This class handles the set alarm button. When the set alarm button or menu item is pressed, this class calls the setAlarm function. 
 */
public class SetButtonHandler implements ActionListener{
        
    Model model;
    View view;
    
    /**
     *
     * @param m
     * @param v
     */
    public SetButtonHandler(Model m, View v) {
        model = m;
        view = v;
    }
    
    /**
     * Calls the setAlarm method when the set alarm button or menu item is pressed.
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            //Set the format of the date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            //Get todays date
            Date today = Calendar.getInstance().getTime();
            //Format todays date
            String dates = (formatter.format(today));
            today = formatter.parse(dates);
            //Call the set alarm method, passing in the variables.
            //These variables set the default hour/min/sec in the dialogue popup to 0, set the default date to today, and set the
            //'selected' variable to 0, showing that this is a new alarm and not an alarm edit.
            view.setAlarm(0,0,0,today,-1);
        } catch (ParseException | QueueOverflowException | QueueUnderflowException ex) {
            Logger.getLogger(SetButtonHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

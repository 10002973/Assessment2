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
 * @author Heather
 */
public class RemoveButtonHandler implements ActionListener{
       
    Model model;
    View view;
    
    public RemoveButtonHandler(Model m, View v) {
        model = m;
        view = v;
    }
    
    public void actionPerformed(ActionEvent event) {
        try {
            view.deleteAlarm();
        } catch (QueueOverflowException | QueueUnderflowException | ParseException ex) {
            Logger.getLogger(RemoveButtonHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.awt.event.ActionEvent;

/**
 *
 * @author Heather
 */
public class EditButtonHandler {
       
    Model model;
    View view;
    
    public EditButtonHandler(Model m, View v) {
        model = m;
        view = v;
    }
    
    public void actionPerformed(ActionEvent event) {
        //Calls editAlarm method
        view.editAlarm();
    } 
}

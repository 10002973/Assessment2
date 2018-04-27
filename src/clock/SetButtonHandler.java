/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Heather
 */
public class SetButtonHandler {
        
    Model model;
    View view;
    
    public SetButtonHandler(Model m, View v) {
        model = m;
        view = v;
    }
    
    public void actionPerformed(ActionEvent event) throws ParseException {
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
            view.setAlarm(0,0,0,today,0);
    }
}

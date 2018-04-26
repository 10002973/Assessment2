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
            //Call the set alarm method, passing in the variables
            view.setAlarm(0,0,0,today,0);
    }
}

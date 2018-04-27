package clock;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import queuemanager.PriorityItem;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;
import queuemanager.SortedArrayPriorityQueue;

public class View implements Observer {
    //Set up variables
    ClockPanel panel;
    Alarm alarm;
    SortedArrayPriorityQueue sorted;   
    JLabel label = new JLabel();
    Container pane;
    JFrame frame = new JFrame();
    iCalendar ical;
    
    public View(Model model) {
        //Set up instances
        panel = new ClockPanel(model);
        alarm = new Alarm(model, this);
        sorted = new SortedArrayPriorityQueue(); 
        ical = new iCalendar(model, this);
        //frame.setContentPane(panel);
        frame.setTitle("Java Clock");
        //Stop the program from automatically closing
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        
        // Start of border layout code
        
        // I've just put a single button in each of the border positions:
        // PAGE_START (i.e. top), PAGE_END (bottom), LINE_START (left) and
        // LINE_END (right). You can omit any of these, or replace the button
        // with something else like a label or a menu bar. Or maybe you can
        // figure out how to pack more than one thing into one of those
        // positions. This is the very simplest border layout possible, just
        // to help you get started.
        
        pane = frame.getContentPane();
         
        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);
        
        //Add button to set alarm
        JButton setButton = new JButton("Set alarm");
        setButton.addActionListener(new SetButtonHandler(model,this));
        pane.add(setButton, BorderLayout.LINE_START);
        
        //Add label for the alarm details
        label.setText("No alarm set");
        pane.add(label, BorderLayout.PAGE_END);
        
        //Add button for editing alarms
        JButton editButton = new JButton("Edit alarm");
        editButton.addActionListener(new EditButtonHandler(model,this));
        pane.add(editButton, BorderLayout.LINE_END);
        
        // End of borderlayout code
                
        //Menu bar
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuSet;
        JMenuItem menuEdit;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the menu.
        menu = new JMenu("Alarm");
        menu.setMnemonic('A');
        menu.getAccessibleContext().setAccessibleDescription("View available actions for the alarm");
        menuBar.add(menu);

        //Add menu option to set an alarm
        menuSet = new JMenuItem("Set an alarm", 'S');
        menuSet.getAccessibleContext().setAccessibleDescription("Set a new alarm");
        menu.add(menuSet);
        menuSet.addActionListener(new SetButtonHandler(model,this));
        
        //Add menu option to edit an alarm
        menuEdit = new JMenuItem("Edit an alarm", 'E');
        menuEdit.getAccessibleContext().setAccessibleDescription("Edit an existing alarm");
        menu.add(menuEdit);
        menuEdit.addActionListener(new EditButtonHandler(model,this));

        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
        //Call the load method
        load();
    }
    
    //Shows a dialogue popup which enables the user to set an alarm.
    public void setAlarm(int h, int m, int s, Date d, long selected) throws QueueOverflowException, QueueUnderflowException {  
        //Create the JSpinners
        JSpinner hourSpinner = new JSpinner();
        JSpinner minuteSpinner = new JSpinner();
        JSpinner secondSpinner = new JSpinner();
        
        //Set the JSpinners to SpinnerNumberModels. The initial numbers are set to h,m,s which are set to the
        //hours/minutes/seconds of the alarm being edited. If it is a new alarm then these are set to 0.
        //They are set to minimum 0, with the maximum numbers set to 23 or 59, the maximum number of hours or mins/seconds.
        //Increments by 1.
        hourSpinner.setModel(new SpinnerNumberModel(h,0,23,1));
        minuteSpinner.setModel(new SpinnerNumberModel(m,0,59,1));
        secondSpinner.setModel(new SpinnerNumberModel(s,0,59,1));
        
        //Get the current date/time
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        //Remove a day from the calendar, so today's date is selectable in the spinner
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        //Set yesterday as the today variable
        Date today = calendar.getTime();
        //Add 2 years to the calendar - this is the maximum selectable date in the spinner
        calendar.add(Calendar.YEAR, 2);
        Date endDate = calendar.getTime();

        //Create a new Spinner for the date, and set it to the above variables.
        JSpinner dateSpinner = new JSpinner();
        dateSpinner.setModel(new SpinnerDateModel(d, today, endDate, 1));
        //Format the date spinner
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        
        //Set the time spinners to have two digits
        hourSpinner.setEditor(new JSpinner.NumberEditor(hourSpinner, "00"));
        minuteSpinner.setEditor(new JSpinner.NumberEditor(minuteSpinner, "00"));
        secondSpinner.setEditor(new JSpinner.NumberEditor(secondSpinner, "00"));
        
        //Set the content of the spinner
        Object[] content = {"Date: ", dateSpinner,
                            "Hour: ", hourSpinner,
                            "Minutes: ", minuteSpinner,
                            "Seconds: ", secondSpinner};
        
        //Set the button labels on the spinner
        Object[] options = {"Submit", "Cancel"};
        //Create the set alarm spinner
        int optionPane = JOptionPane.showOptionDialog(panel, content,"Set alarm",
                         JOptionPane.OK_CANCEL_OPTION,
                         JOptionPane.QUESTION_MESSAGE,
                         null, options,null);
        
        //If the user presses submit...
        if(optionPane == JOptionPane.OK_OPTION){
            try {
                //Get the submitted data
                hourSpinner.commitEdit();
                minuteSpinner.commitEdit();
                secondSpinner.commitEdit();
                dateSpinner.commitEdit();
                
                //Set the variables
                int hour = (Integer) hourSpinner.getValue();
                int minute = (Integer) minuteSpinner.getValue();
                int second = (Integer) secondSpinner.getValue();
                Object date = dateSpinner.getValue();
                int priority = 0;
                Object alarmTime = hour + ":" + minute + ":" + second;

                //If the alarm is successfully inserted, add the label to the panel showing the alarm time
                if (alarm.add(alarmTime, priority, date, false) == true) {
                    pane.add(label, BorderLayout.PAGE_END);
                    //If this is not a new alarm and is actually an alarm edit, then remove the old alarm
                    if (selected !=0){
                        alarm.remove(selected);
                    }
                }
            }
            catch (java.text.ParseException e) {
            }

        }
    }
    
    //Checks what the soonest alarm is and sets the label on the panel to display it.
    public void checkAlarm() {
        //If there are no alarms in the array then display the message.
       if (sorted.head() == null){
           label.setText("No alarm set");
       }
       else {
            //Split the time into hours/minutes/seconds
            String[] time = sorted.head().toString().split(":");
            String h = time[0];
            String m = time[1];
            String s = time[2];
       
            //If the hours/mins/seconds are below 10, place a 0 in front of the number. This is a purely cosmetic change to improve formatting.
            if (Integer.parseInt(time[0]) < 10){
                h = "0" + h;
            }
            
            if (Integer.parseInt(time[1]) < 10){
                m = "0" + m;
            }
            
            if (Integer.parseInt(time[2]) < 10){
                s = "0" + s;
            } 
            
            // Set the label to display the date and time of the soonest alarm           
            label.setText("Next Alarm Set to " + time[3] + "  " + h + ":" + m + ":" + s); 
       }
    }
    
    //This creates a dialogue enabling the user to select an alarm to edit. The details are then passed
    //to the setAlarm function.
    public void editAlarm() throws QueueOverflowException, QueueUnderflowException, ParseException {
        Object[] list = new Object[sorted.tailIndex+1];
        //For each item in the queue, split it into the date and time, and add these to the list array.
        for(int i = 0; i <SortedArrayPriorityQueue.tailIndex+1; i++){
            String[] time = ((PriorityItem)sorted.storage[i]).getItem().toString().split(":");
            String h = time[0];
            String m = time[1];
            String s = time[2];
            String date = time[3];
            list[i] = date + "  " + h + ":" + m + ":" + s;
        }
        //Create the dialogue popup
        int selected = JOptionPane.showOptionDialog(
                    panel,
                    "Select an alarm to edit: ",
                    "Edit alarm",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    list,
                    null);

        // Split the selected alarm down into its time and date
                String[] date = list[selected].toString().split("  ");
                String[] selectedTime = date[1].split(":");
                int hour = Integer.parseInt(selectedTime[0]);
                int min = Integer.parseInt(selectedTime[1]);
                int sec = Integer.parseInt(selectedTime[2]);
                String alDate = date[0];
                //Format the date
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date d = formatter.parse(alDate);
                //Send the variables to setAlarm
                setAlarm(hour,min,sec,d,((PriorityItem)sorted.storage[selected]).getPriority());
                //Call checkAlarm to check when the new soonest alarm is
                checkAlarm();
    }    
    
    //When the time changes this method is called. It updates the clock, and checks to see if the time now matches the alarm time.
    public void update(Observable o, Object arg) {
        try {
            panel.repaint();
            alarm.alert();
        } catch (QueueUnderflowException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Alerts the user that the selected time is in the past
    public void alarmError() throws QueueUnderflowException {
        JOptionPane.showMessageDialog(panel,"Select a future time","Error!",JOptionPane.OK_CANCEL_OPTION);
    }
    
    //Alerts the user that they cannot save any more alarms
    public void alarmFull() throws QueueUnderflowException {
        JOptionPane.showMessageDialog(panel,"You may only save up to 8 alarms!","Error!",JOptionPane.OK_CANCEL_OPTION);
    }
    
    
    //This method shows a popup informing the user that their alarm time has been met
    public void alarmAlert() throws QueueUnderflowException {
        JOptionPane.showMessageDialog(panel,"Your alarm is ringing!","Alert!",JOptionPane.OK_CANCEL_OPTION);
        //The alarm is removed from the queue
        alarm.remove(((PriorityItem)sorted.storage[0]).getPriority());
        //Checks to see when the next alarm is
        checkAlarm();
    }
}

package clock;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;
import queuemanager.PriorityItem;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;
import queuemanager.SortedArrayPriorityQueue;
import static queuemanager.SortedArrayPriorityQueue.storage;

/**
 * This class controls the interface of the program, It controls what the user sees including buttons and dialog boxes. 
 * {@literal Dialog popups in this section are based on code by Marilena (2017) Java Swing – JOptionPane showOptionDialog example [online]. Available from <https://www.mkyong.com/swing/java-swing-joptionpane-showoptiondialog-example/> [27 April 2018]}
 * @author Heather Taylor-Stanley 10002973
 * @version "%I%, %G%"
 */
public class View implements Observer {
    //Set up variables
    ClockPanel panel;
    Alarm alarm;
    SortedArrayPriorityQueue sorted;   
    JLabel label = new JLabel();
    Container pane;
    JFrame frame = new JFrame();
    iCalendar ical;
    
    /**
     * This method sets up the panel, including the buttons and menu.
     * 
     * @param model         Contains passed model object.
     * @throws IOException          Throws exception if input output exception occurs.
     * @throws ParseException       Throws exception if parse exception occurs.
     * @throws QueueOverflowException       Throws exception if queue overflow occurs.
     * @throws QueueUnderflowException      Throws exception if queue underflow occurs.
     * 
     */
    public View(Model model) throws IOException, ParseException, QueueOverflowException, QueueUnderflowException {
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
        
        //Add button to remove alarm
        JButton removeButton = new JButton("Remove alarm");
        removeButton.addActionListener(new RemoveButtonHandler(model,this));
        pane.add(removeButton, BorderLayout.PAGE_START);
        
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
        
        //Add menu option to delete an alarm
        menuEdit = new JMenuItem("Delete an alarm", 'D');
        menuEdit.getAccessibleContext().setAccessibleDescription("Delete an alarm");
        menu.add(menuEdit);
        menuEdit.addActionListener(new RemoveButtonHandler(model,this));

        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
        //Call the load method
        load();
    }

    /**
     * This method creates a dialog which enables the user to set an alarm.
     * 
     * {@literal This section is based on code by Java2s (n.d) Create SpinnerDateModel for Date value and set start end date value in Java [online]. Available from <http://www.java2s.com/Tutorials/Java/Swing/JSpinner/Create_SpinnerDateModel_for_Date_value_and_set_start_end_date_value_in_Java.htm> [26 April 2018]}
     *
     * @param h     Contains the hour of the alarm.
     * @param m     Contains the minute of the alarm.
     * @param s     Contains the second of the alarm.
     * @param d     Contains the date of the alarm.
     * @param selected      Contains index of alarm if this is an edit.
     * @throws QueueOverflowException       Throws exception if queue overflow occurs.
     * @throws QueueUnderflowException      Throws exception if queue underflow occurs.
     * 
     */
    public void setAlarm(int h, int m, int s, Date d, int selected) throws QueueOverflowException, QueueUnderflowException {  
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
                String hour = hourSpinner.getValue().toString();
                String minute = minuteSpinner.getValue().toString();
                String second = secondSpinner.getValue().toString();
                Object date = dateSpinner.getValue();
                int priority = 0;
                
                
                //If the hours/mins/seconds are below 10, place a 0 in front of the number. This is a purely cosmetic change to improve formatting.
                if (Integer.parseInt(hour) < 10 && !"00".equals(hour)){
                    hour = "0" + hour;
                }

                if (Integer.parseInt(minute) < 10 && !"00".equals(minute)){
                    minute = "0" + minute;
                }

                if (Integer.parseInt(second) < 10 && !"00".equals(second)){
                    second = "0" + second;
                } 

                Object alarmTime = hour + ":" + minute + ":" + second;
                //If the alarm is successfully inserted, add the label to the panel showing the alarm time
                if (alarm.add(alarmTime, priority, date, false) == true) {
                    pane.add(label, BorderLayout.PAGE_END);
                    //If this is not a new alarm and is actually an alarm edit, then remove the old alarm
                    if (selected >= 0){
                        alarm.remove(selected);
                    }
                }
            }
            catch (java.text.ParseException e) {
            }

        }
    }
    
    /**
     * This method checks what the soonest alarm is and sets the label on the panel to display it.
     */
    public void checkAlarm() {
        int alarmNo = 0;
        //If there are no alarms in the array or if the last item in the array has already passed then display label.
       if (sorted.head() == null || ((PriorityItem)sorted.storage[sorted.tailIndex]).getPriority() == 0){
           label.setText("No alarm set");
       }
       else{
           String[] time;
           //If the head item has already passed, loop through to find an alarm which has not already passed.
           if (((PriorityItem)sorted.storage[0]).getPriority() == 0){
                int count = 1;
                while(((PriorityItem)sorted.storage[count]).getPriority() ==0 && count <sorted.tailIndex+1){
                    count++;
                }
                time = ((PriorityItem)storage[count]).getItem().toString().split(":");
                alarmNo = sorted.tailIndex + 1 - count;
            }
           else{
                //Split the soonest alarm into hours/minutes/seconds
                time = sorted.head().toString().split(":");
                alarmNo = sorted.tailIndex + 1;
            }
            String h = time[0];
            String m = time[1];
            String s = time[2];
       
            // Set the label to display the date and time of the soonest alarm           
            label.setText("Next Alarm Set to " + time[3] + "  " + h + ":" + m + ":" + s + "\n Upcoming alarms: " + alarmNo); 
       }
    }
    


    /**
     * This creates a dialogue enabling the user to select an alarm to edit. The details are then passed to the setAlarm function.
     * 
     * @throws QueueOverflowException       Throws exception if queue overflow occurs.
     * @throws QueueUnderflowException      Throws exception if queue underflow occurs.
     * @throws ParseException               Throws exception if parse exception occurs.
     * 
     */
    public void editAlarm() throws QueueOverflowException, QueueUnderflowException, ParseException {
        Object[] list = new Object[sorted.tailIndex+1];
        String message = "";
        //For each item in the queue, split it into the date and time, and add these to the list array.
        for(int i = 0; i <SortedArrayPriorityQueue.tailIndex+1; i++){
            String[] time = ((PriorityItem)sorted.storage[i]).getItem().toString().split(":");
            String h = time[0];
            String m = time[1];
            String s = time[2];
            String date = time[3];
            list[i] = date + "  " + h + ":" + m + ":" + s;
        }
        
        //Set the dialog message
        if(sorted.tailIndex+1 > 0){
            message = "Select an alarm to edit: ";
        }
        else {
            message = "There are no alarms set!";    
        }
        //Create the dialog popup
        int selected = JOptionPane.showOptionDialog(
                    panel,
                    message,
                    "Edit alarm",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    list,
                    null);
        if (selected != JOptionPane.CLOSED_OPTION) {
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
                
                //Get today's date
                Date today = Calendar.getInstance().getTime();
                String dates = (formatter.format(today));
                today = formatter.parse(dates);

                //Get difference between the dates  
                Date alarmDate = formatter.parse(alDate);
                long diff = alarmDate.getTime() - today.getTime();
                
                //If the passed date is before the current date, then change it to today's date
                if (diff <=0){
                    d = today;
                }

                //Send the variables to setAlarm
                setAlarm(hour,min,sec,d,selected);
                //Call checkAlarm to check when the new soonest alarm is
                checkAlarm();
        }
    }    
    
    /**
     * This method creates a dialog box, which allows the user to select an alarm to delete.
     * 
     * @throws QueueOverflowException       Throws exception if queue overflow occurs.
     * @throws QueueUnderflowException      Throws exception if queue underflow occurs.
     * @throws ParseException               Throws exception if parse exception occurs.
     */
    public void deleteAlarm() throws QueueOverflowException, QueueUnderflowException, ParseException {
        Object[] list = new Object[sorted.tailIndex+1];
        String message = "";
        //For each item in the queue, split it into the date and time, and add these to the list array.
        for(int i = 0; i <SortedArrayPriorityQueue.tailIndex+1; i++){
            String[] time = ((PriorityItem)sorted.storage[i]).getItem().toString().split(":");
            String h = time[0];
            String m = time[1];
            String s = time[2];
            String date = time[3];
            list[i] = date + "  " + h + ":" + m + ":" + s;
        }
        
        //Set the dialog message
        if(sorted.tailIndex+1 > 0){
            message = "Select an alarm to delete: ";
        }
        else {
            message = "There are no alarms set!";    
        }
        //Create the dialogue popup
        int selected = JOptionPane.showOptionDialog(
                    panel,
                    message,
                    "Delete alarm",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    list,
                    null);
        if (selected != JOptionPane.CLOSED_OPTION) {
            //Send the selected alarm to remove
            sorted.remove(selected);
            //Call checkAlarm to check when the new soonest alarm is
            checkAlarm();
        }
    } 

    /**
     * When the time changes this method is called, it updates the clock and checks to see if the time now matches the alarm time.
     *
     * @param o     Contains passed observable object.
     * @param arg   Contains passed argument object.
     */
    public void update(Observable o, Object arg) {
        try {
            panel.repaint();
            alarm.alert();
        } catch (QueueUnderflowException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Alerts the user that the selected time is in the past.
     *
     * @throws QueueUnderflowException      Throws exception if queue underflow occurs.
     */
    public void alarmError() throws QueueUnderflowException {
        JOptionPane.showMessageDialog(panel,"Select a future time","Error!",JOptionPane.OK_CANCEL_OPTION);
    }

    /**
     * Alerts the user that they cannot save any more alarms.
     *
     * @throws QueueUnderflowException      Throws exception if queue underflow occurs.
     */
    public void alarmFull() throws QueueUnderflowException {
        JOptionPane.showMessageDialog(panel,"You may only save up to 8 alarms!","Error!",JOptionPane.OK_CANCEL_OPTION);
    }
    

    /**
     * This method shows a dialog informing the user that their alarm time has been met.
     *
     * @param id        Contains passed alarm index.
     * @throws QueueUnderflowException      Throws exception if queue underflow occurs.
     */
    public void alarmAlert(int id) throws QueueUnderflowException {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(panel,"Your alarm is ringing!","Alert!",JOptionPane.OK_CANCEL_OPTION);
        //The alarm is removed from the queue
        alarm.remove(id);
        //Checks to see when the next alarm is
        checkAlarm();
    }

    /**
     * Shows dialog on program close enabling user to save the alarms.
     * 
     * {@literal This section is based on code by Java2s (n.d) Demonstration of File dialog boxes : File Chooser « Swing JFC « Java [online]. Available from <http://www.java2s.com/Code/Java/Swing-JFC/DemonstrationofFiledialogboxes.htm> [27 April 2018]}
     *
     * @throws IOException      Throws exception if input output exception occurs.
     */
    public void saveAlarms() throws IOException {
        String file = new String();
        String loc = new String();
        //Set button labels
        Object[] options = {"Save", "Exit without saving"};
        //Set popup dialogue
        int exitPane = JOptionPane.showOptionDialog(panel,"Would you like to save your alarms?","Save?",JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.QUESTION_MESSAGE, null, options,null);
        
        //If user presses save
        if(exitPane == JOptionPane.OK_OPTION){
            //Create filechooser
            JFileChooser fc = new JFileChooser();
            //Set filter to ical
            FileNameExtensionFilter filter = new FileNameExtensionFilter("ics", "ICS", "ical", "ICAL", "icalendar");
            fc.setFileFilter(filter);
            //Set initial directory to user home
            fc.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fc.showSaveDialog(panel);
            //If user selects a location then get the selected location and file name
            if (result == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile().getName();
                loc = fc.getCurrentDirectory().toString();
            }
            //Write save file
            ical.write(file,loc);
            System.out.println("Saved!");
            //Close program
            frame.dispose();
            //NOTE: The system exit line will crash the ViewTest file
            System.exit(0);
        }
        //If user selects to exit without saving, close program
        else {
            frame.dispose();
            System.exit(0);
        }
    }

    /**
     * This method creates a dialog box, enabling the user to load in saved alarms on startup.
     * 
     * {@literal This section is based on code by CodeJava (2015) Show simple open file dialog using JFileChooser [online]. Available from <http://www.codejava.net/java-se/swing/show-simple-open-file-dialog-using-jfilechooser> [27 April 2018]}
     *
     * @throws IOException      Throws exception if input output exception occurs.
     * @throws FileNotFoundException    Throws exception if file is not found.
     * @throws ParseException           Throws exception if parse exception occurs.
     * @throws QueueOverflowException   Throws exception if queue overflow exception occurs.
     * @throws QueueUnderflowException  Throws exception if queue underflow exception occurs.
     */
    public void load() throws IOException, FileNotFoundException, ParseException, QueueOverflowException, QueueUnderflowException{
        Object[] options = {"Load", "Exit"};
        //Shows dialog
        int exitPane = JOptionPane.showOptionDialog(panel,"Would you like to load your saved alarms?","Load?",JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.QUESTION_MESSAGE, null, options,null);
        //If user does want to load saved file
        if(exitPane == JOptionPane.OK_OPTION){
            //Setup new file chooser
            JFileChooser fc = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("ics", "ICS", "ical", "ICAL", "icalendar");
            fc.setFileFilter(filter);
            fc.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fc.showOpenDialog(panel);
            if (result == JFileChooser.APPROVE_OPTION) {
                //If a file is selected, get the path and pass to read function in iCalendar class
                File selected = fc.getSelectedFile();
                ical.read(selected.getAbsolutePath());
                System.out.println("Successfully loaded file!");
            }
        }
    }
}

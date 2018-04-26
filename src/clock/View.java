package clock;

import java.awt.*;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
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
    
    public void update(Observable o, Object arg) {
        panel.repaint();
    }
}

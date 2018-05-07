/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import queuemanager.PriorityItem;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;
import queuemanager.SortedArrayPriorityQueue;
import static queuemanager.SortedArrayPriorityQueue.storage;

/**
 * This class controls the alarm, including all of the alarm functions such as adding and removing alarms. It uses the methods in SortedArrayPriorityQueue.
 * @author Heather Taylor-Stanley 10002973
 * @version "%I%, %G%"
 */
public class Alarm {
    //Set variables
    SortedArrayPriorityQueue sorted = new SortedArrayPriorityQueue(8);
    Object[] storage;
    int capacity;
    static int tailIndex;
    Object head;
    Model model;
    View view;
    int hour;
    int minute;
    int second;
    
    /**
     * Alarm constructor, initialises the model and view variables.
     * @param m     Passed model object.
     * @param v     Passed view object.
     */
    public Alarm(Model m, View v){
        model = m;
        view = v;
    }

    /**
     * Checks the time of the alarm, and calculates the priority based on the difference between the current time and the alarm time, then sends the alarm to SortedArrayPriorityQueue to be added to the priority queue.
     * This section is based on code by MySampleCode (n.d) Java calculate difference between two dates [online]. Available from <http://www.mysamplecode.com/2012/06/java-calculate-days-difference.html> [26 April 2018]
     * 
     * @param alarm         Contains the passed alarm time.
     * @param priority      Contains the priority of the alarm.
     * @param alarmCal      Contains the date of the passed alarm.
     * @param loaded        Contains true or false depending on if the alarm has been loaded from file.
     * @return              Returns true or false depending if the alarm is successfully added to the queue.
     * @throws ParseException           Throws parse exception if it occurs.
     * @throws QueueUnderflowException  Throws exception if queue underflow occurs.
     * @throws QueueOverflowException   Throws exception if queue overflow occurs.
     * 
     */
    public Boolean add(Object alarm, int priority, Object alarmCal, boolean loaded) throws ParseException, QueueUnderflowException, QueueOverflowException {
        //Set the variables
        long pos = 0;
        long days = 0;
        long timeDiff = 0;
        
        //Split the alarm object to get the hours/minutes/seconds
        String[] concat = alarm.toString().split(":");
        //Set a time format
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        //Get today's date
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");   
        Date today = Calendar.getInstance().getTime();
        String dates = (formatter.format(today));
        today = formatter.parse(dates);
        
        
        //Get difference between the dates
        String alDate = formatter.format(alarmCal);  
        Date alarmDate = formatter.parse(alDate);
        
        //Get difference between the alarm time and todays time
        long diff = alarmDate.getTime() - today.getTime();

        //If the difference in time is positive then the alarm is in the future.
        //Get the difference in days between the alarms, and multiply that number by the seconds in a day (86400).
        //Note: This will add 86400 even if the alarm is the next day but not necessarily 24 hours away.
        //However, due to the calculation for the priority, it will still output the alarms in the correct order.
        if (diff > 0){
             days = (diff / (24 * 60 * 60 * 1000));
             pos = (days * 86400);
        } 
        //System.out.println(days);
        //Get both the current and alarm times in seconds
        int time = ((model.fullhour * 3600)  + (model.minute * 60) + model.second);
        int alTime = ((Integer.parseInt(concat[0]) * 3600) + (Integer.parseInt(concat[1]) * 60) + Integer.parseInt(concat[2]));             
        timeDiff = alTime - time;
        
        //If the alarm date is before the current date then set the priority to 0
        if (diff <0){
            pos = 0;
        }
        //If the alarm is today and the time is in the future, then set the priority to the difference between the alarm time and the current time
        else if(days == 0 && alTime - time > 0){
            pos += alTime - time;
        }
        //If the alarm time is on a future day and further ahead than the current time, add the difference between the alarm time and the current time to the priority.
        else if (alTime - time > 0){
             pos += alTime - time;
        }        
        //If the alarm is on a future day but the time is before the current time, then add the alarm time to the priority.
        else if (days > 0 && alTime - time < 0){       
            pos += alTime;
        }
        
        //If adding another alarm would go over capacity, call the alarmFull method and return false
        if (sorted.tailIndex + 1 == sorted.capacity){
            view.alarmFull();
            return false;
        }
        else {
            //Send to checkDate() to ensure the alarm is in the future. If so, send the alarm to 
            //the priority queue to be added.
            if (checkDate(days, timeDiff, loaded) == true){
                alarm = alarm + ":" + alDate;
                sorted.add(alarm, pos);  
                //Call the checkAlarm method
                view.checkAlarm();
                System.out.println(sorted.toString());
                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
     * Check if the alarm is in the future, calls alarmError to show an error message if not.
     * 
     * @param days      Contains number of days between today and alarm date.
     * @param timeDiff  Contains difference in time between now and alarm.
     * @param loaded    Contains true or false depending on if the alarm has been loaded from file.
     * @return          Returns true or false depending on if the alarm is in the future or has passed.
     * @throws QueueUnderflowException     Throws exception if queue underflow occurs.
     * 
     */
    public Boolean checkDate(long days, long timeDiff, boolean loaded) throws QueueUnderflowException{
        if (days == 0 && timeDiff <= 0 && loaded == false){
            view.alarmError();
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Call SortedArrayPriorityQueue to remove an alarm from the queue
     * 
     * @param index     Contains queue index of the alarm.
     * @throws QueueUnderflowException      Throws exception if queue underflow occurs.
     * 
     */
    public void remove(int index) throws QueueUnderflowException {
        sorted.remove(index);
    }
    
    /**
     * Call SortedArrayPriorityQueue to get the next alarm.
     * 
     * @return      Returns the head alarm of the queue.
     * @throws QueueUnderflowException      Throws exception if queue underflow occurs.
     * 
     */
    public Object head() throws QueueUnderflowException{
        return sorted.head();
    }

    /**
     * Call SortedArrayPriorityQueue to print all alarms in the queue
     * @return      Returns the alarms in the queue in a string format.
     * 
     */
    @Override
    public String toString() {
        String result = sorted.toString();
        return result;
    }
    
    /**
     * If the current time matches the time for the next alarm then call alarmAlert to show an alert.
     * @throws QueueUnderflowException      Throws exception if queue underflow occurs.
     * 
     */
    public void alert() throws QueueUnderflowException {
        String[] nextAlarm;
        int id = 0;
        //If there are no alarms in the queue then set variable to null
        if (head() == null){
            nextAlarm = null;
        }
        //If every alarm in the queue has already passed then set variable to null
        else if (((PriorityItem)sorted.storage[0]).getPriority() == 0 && ((PriorityItem)sorted.storage[sorted.tailIndex]).getPriority() ==0){
            nextAlarm = null;
        }
        //If the head alarm has already passed, then go through each item in the queue until an upcoming alarm is found
        else if (((PriorityItem)sorted.storage[0]).getPriority() == 0){
            int count = 0;
            while(((PriorityItem)sorted.storage[count]).getPriority() ==0 && count <sorted.tailIndex){
                count++;
                id = count;
            }
            //Set the variable to the upcoming alarm
            nextAlarm = ((PriorityItem)sorted.storage[count]).getItem().toString().split(":");
        }  
        //Otherwise get the head alarm data
        else {
            nextAlarm = head().toString().split(":");
        }  
        //If the next alarm is not null and the alarm time matches the current time then call alarmAlert.
        if (nextAlarm != null && (Integer.parseInt(nextAlarm[0]) == model.fullhour)
                && (Integer.parseInt(nextAlarm[1]) == model.minute)
                && (Integer.parseInt(nextAlarm[2]) == model.second)){        
            view.alarmAlert(id);
        }
    }
}

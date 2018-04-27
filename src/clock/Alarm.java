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
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;
import queuemanager.SortedArrayPriorityQueue;

/**
 *
 * @author Heather
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
    
    
    public Alarm(Model m, View v){
        model = m;
        view = v;
    }
    
    //This section is based on code by MySampleCode (n.d) Java calculate difference between two dates [online]. Available from <http://www.mysamplecode.com/2012/06/java-calculate-days-difference.html> [26 April 2018]
    //Checks the time of the alarm, and calculates the priority based on the difference between the current time and the alarm time.
    //Then sends the alarm to SortedArrayPriorityQueue to be added to the priority queue.
    public Boolean add(Object alarm, int priority, Object alarmCal, boolean loaded) throws ParseException, QueueUnderflowException, QueueOverflowException {
        //Set the variables
        long pos = 0;
        long days = 0;
        long timeDiff = 0;
        
        //Split the alarm object to get the hours/minutes/seconds
        String[] concat = alarm.toString().split(":");
        //Set a time format
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        //System.out.println(alarmCal);
//
//        //Create a string containing the current time.
//        String currTime = (model.hour + ":" + model.minute + ":" + model.second);
//        //Format the current Time
//        Date now = format.parse(currTime);
//        Date alarmSet = format.parse(alarm.toString());

        //Get today's date
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");   
        Date today = Calendar.getInstance().getTime();
        String dates = (formatter.format(today));
        today = formatter.parse(dates);
        
        
        //Get difference between the dates
        String alDate = formatter.format(alarmCal);  
        Date alarmDate = formatter.parse(alDate);
//        System.out.println(alDate);
//        System.out.println(alarmDate.getTime());
//        System.out.println(today.getTime());
        
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
        
        //If the alarm time is larger than the current time then the alarm is occuring after the current time and so the time is taken
        //away from the alarm time
        if (alTime - time > 0){
             pos += alTime - time;
             timeDiff = alTime - time;
        }       
        //Otherwise add the alarm time to the pos variable
        else {       
            pos += alTime;
        }
//        System.out.println(sorted.tailIndex);
//        System.out.println(sorted.capacity);
        
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
                //System.out.println(sorted.toString());
                return true;
            }
            else {
                return false;
            }
        }
    }
    
    //Check if the alarm is in the future. If not, call alarmError to show an error message.
    public Boolean checkDate(long days, long timeDiff, boolean loaded) throws QueueUnderflowException{
        if (days == 0 && timeDiff <= 0 && loaded == false){
            view.alarmError();
            return false;
        }
        else {
            return true;
        }
    }
    
    //Call SortedArrayPriorityQueue to remove an alarm from the queue
    public void remove(long alarm) throws QueueUnderflowException {
        sorted.remove(alarm);
    }
    
    //Call SortedArrayPriorityQueue to get the next alarm
    public Object head() throws QueueUnderflowException{
        return sorted.head();
    }
    
    //Call SortedArrayPriorityQueue to print all alarms in the queue
    @Override
    public String toString() {
        String result = sorted.toString();
        return result;
    }
    
    //If the current time matches the time for the next alarm then call alarmAlert to show an alert.
    public void alert() throws QueueUnderflowException {
        if(head() != null){
            String[] nextAlarm = head().toString().split(":");
            if ((Integer.parseInt(nextAlarm[0]) == model.fullhour)
                    && (Integer.parseInt(nextAlarm[1]) == model.minute)
                    && (Integer.parseInt(nextAlarm[2]) == model.second)){        
                view.alarmAlert();
            }
        }
    }
}

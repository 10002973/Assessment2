/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import queuemanager.SortedArrayPriorityQueue;

/**
 *
 * @author Heather
 */
public class iCalendar {
    Calendar calendar;
    Alarm alarm;
    Model model;
    View view;
    SortedArrayPriorityQueue sorted = new SortedArrayPriorityQueue();
    
    //Set variables
    private String version =    "VERSION:2.0\r\n";
    private String prodid =     "PRODID:-//Heather Taylor-Stanley/Assessment2//EN\r\n";
    private String calBegin =   "BEGIN:VCALENDAR\r\n";
    private String calEnd =     "END:VCALENDAR\r\n";
    private String eventBegin = "BEGIN:VALARM\r\n";
    private String eventEnd =   "END:VALARM\r\n";
    private String veventBegin = "BEGIN:VEVENT\r\n";
    private String veventEnd =   "END:VEVENT\r\n";
    private String action = "ACTION:DISPLAY\r\n";
    private String desc = "DESCRIPTION:ALARM\r\n";
    String uid = "UID:10002973@uhi.ac.uk";
    String dtStamp = "DTSTAMP:";
    String dtStart = "DTSTART:";
    String dtEnd = "DTEND:";
    
    StringBuilder builder = new StringBuilder();
    File file = new File(builder.toString());
    BufferedWriter bw = null;
    FileWriter fw;
    
    public iCalendar(Model m, View v){
        model = m;
        view = v;
        alarm = new Alarm(m,v);
    }
    
    //Code in this section is based on code by Lofy, J (2015) Writing .ics iCal file using java [online]. Available from <https://stackoverflow.com/questions/31238492/writing-ics-ical-file-using-java> [27 April 2018]
    //Writes alarms to a new file
    public void write(String name, String location) throws IOException {
        //Set ics extension for iCalendar file
        String path = location + "/" + name + ".ics";
        this.fw = new FileWriter(path);
        bw = new BufferedWriter(fw);
        //For each item in the queue, add to the file
        for (int i = 0; i < sorted.tailIndex + 1; i++){
            try {
                Object item = sorted.storage[i];
                //Get the time and date of the alarm
                String[] alarmData = item.toString().split(":");
                String hour = alarmData[0];
                String min = alarmData[1];
                String sec = alarmData[2];
                String date[] = alarmData[3].split("/");
                String day = date[0];
                String month = date[1];
                String year[] = date[2].split(",");
                String alarmYear = year[0];
                hour = hour.substring(1);
                
                //If the hour/min/second is below 10, add a 0 in front
                if (Integer.parseInt(hour) < 10){
                    hour = "0" + hour;
                }

                if (Integer.parseInt(min) < 10){
                    min = "0" + min;
                }

                if (Integer.parseInt(sec) < 10){
                    sec = "0" + sec;
                } 
                
                String dateTime = alarmYear + month + day + "T" + hour + min + sec + "Z\r\n";
                String trigger = "TRIGGER;VALUE=DATE-TIME:" + dateTime;
                //Write the variables to the file
                bw.write(calBegin);
                bw.write(version);
                bw.write(prodid);
                bw.write(veventBegin);
                bw.write(uid + dateTime);
                bw.write(dtStamp + dateTime);
                bw.write(dtStart + dateTime);
                bw.write(dtEnd + dateTime);
                bw.write(eventBegin);
                bw.write(action);
                bw.write(trigger);
                bw.write(desc);
                bw.write(eventEnd);
                bw.write(veventEnd);
                bw.write(calEnd);
                System.out.println("Done");
            } catch (IOException ex) {
                Logger.getLogger(iCalendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        bw.close();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;
import queuemanager.SortedArrayPriorityQueue;

/**
 * This class controls the loading and saving of alarms in an iCalendar format.
 * @author Heather Taylor-Stanley
 * @version "%I%, %G%"
 * 
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
    
    /**
     * iCalendar constructor, initialises the model, view, and alarm variables.
     * @param m     Contains the passed model object.
     * @param v     Contains the passed view object.
     */
    public iCalendar(Model m, View v){
        model = m;
        view = v;
        alarm = new Alarm(m,v);
    }

    /**
     * Writes alarms to an iCalendar file, using the passed location and file name.
     * 
     * {@literal Code in this section is based on code by Lofy, J (2015) Writing .ics iCal file using java [online]. Available from <https://stackoverflow.com/questions/31238492/writing-ics-ical-file-using-java> [27 April 2018]}
     *
     * @param name      Contains name of the file.
     * @param location  Contains location of the file.
     * @throws IOException  Throws exception if input output exception occurs.
     */
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

    /**
     * Reads a saved alarm file, and adds the alarms to the program
     * @param f     Contains file.
     * @throws FileNotFoundException    Throws exception if file is not found.
     * @throws IOException      Throws exception if input output exception occurs.
     * @throws ParseException           Throws exception if parse exception occurs.
     * @throws QueueOverflowException   Throws exception if queue overflow exception occurs.
     * @throws QueueUnderflowException  Throws exception if queue underflow exception occurs.
     */
    public void read(String f) throws FileNotFoundException, IOException, ParseException, QueueOverflowException, QueueUnderflowException {
        File file = new File(f);
        int i = 0;
        ArrayList alarms = new ArrayList();
        Scanner in = new Scanner(file);
        //While there are still lines in the file, loop through them. If a line contains the word 'trigger' add it to the alarms arraylist
        while(in.hasNext()){
            String line = in.nextLine();
            if (line.contains("TRIGGER")){
                alarms.add(line); 
                i++;
            }
        }
        //For each item in the list, split it into the date and time
        for(int x = 0; x <i; x++){
            String[] time = alarms.get(x).toString().split(":");
            String year = time[1].substring(0,4);
            String month = time[1].substring(5,6);
            String day = time[1].substring(6,8);
            String hour = time[1].substring(9,11);
            String minute = time[1].substring(11,13);
            String second = time[1].substring(13,15);
            String fullTime = hour + ":" + minute + ":" + second;
            String fullDate = day + "/" + month + "/" + year;

            System.out.println(fullTime);
            System.out.println(fullDate);
            //Format the date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date d = formatter.parse(fullDate);
            //Send the alarm to be added to the program
            alarm.add(fullTime, 0, d, true);
        }
    }
}

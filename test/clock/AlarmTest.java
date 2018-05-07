/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;

/**
 * This class runs tests on the Alarm class.
 * @author Heather Taylor-Stanley 10002973
 * @version "%I%, %G%"
 */
public class AlarmTest {
    
    Alarm instance;
    Model model;
    View view;
    Calendar calendar = Calendar.getInstance();
    Date now = calendar.getTime();
    
    /**
     * Constructor of class AlarmTest.
     * @throws IOException      Throws exception if input output exception occurs.
     * @throws ParseException           Throws exception if parse exception occurs.
     * @throws QueueOverflowException   Throws exception if queue overflow exception occurs.
     * @throws QueueUnderflowException  Throws exception if queue underflow exception occurs.
     */
    public AlarmTest() throws IOException, ParseException, QueueOverflowException, QueueUnderflowException {
        model = new Model();
        this.view = new View(model);
        instance = new Alarm(model,view);
    }

    /**
     * Test adding alarm to head position.
     * @throws java.lang.Exception  Throws exception.
     */
    @Test
    public void testAddHead() throws Exception {
        System.out.println("add");
        Object alarm = "22:12:12";
        int priority = 0;
        boolean loaded = false;
        Boolean expResult = true;
        Boolean result = instance.add(alarm, priority, now, loaded);
        assertEquals(expResult, result);
    }
    
    /**
     * Test adding alarm to middle of queue.
     * @throws java.lang.Exception  Throws exception.
     */
    @Test
    public void testAddMiddle() throws Exception {
        instance.add("16:15:00", 0, now, false);
        instance.add("23:15:00", 0, now, false);
        System.out.println("add middle");
        Object alarm = "22:12:12";
        int priority = 0;
        boolean loaded = false;
        instance.add(alarm, priority, now, loaded);
        String expResult = "(16:15:00:28/04/2018, 3566), (22:12:12:28/04/2018, 24998), (23:15:00:28/04/2018, 28766)";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
    /**
     * Test adding alarm to end of queue.
     * @throws java.lang.Exception  Throws exception.
     */
    @Test
    public void testAddEnd() throws Exception {
        instance.add("16:15:00", 0, now, false);
        instance.add("21:15:00", 0, now, false);
        System.out.println("add end");
        Object alarm = "23:15:00";
        int priority = 0;
        boolean loaded = false;
        instance.add(alarm, priority, now, loaded);
        String expResult = "(16:15:00:28/04/2018, 3564), (21:15:00:28/04/2018, 21564), (23:15:00:28/04/2018, 28764)";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of checkDate method, of class Alarm.
     * @throws java.lang.Exception  Throws exception.
     */
    @Test
    public void testCheckDate() throws Exception {
        System.out.println("checkDate");
        long days = 0;
        long timeDiff = 0;
        boolean loaded = false;
        Boolean expResult = false;
        Boolean result = instance.checkDate(days, timeDiff, loaded);
        assertEquals(expResult, result);
    }

    /**
     * Test of remove method, of class Alarm.
     * @throws java.lang.Exception  Throws exception.
     */
    @Test
    public void testRemove() throws Exception {
        instance.add("22:12:12", 0, now, false);
        System.out.println("remove");
        int alarm = 0;
        instance.remove(alarm);
    }

    /**
     * Test of head method, of class Alarm.
     * @throws java.lang.Exception  Throws exception.
     */
    @Test
    public void testHead() throws Exception {
        instance.add("22:12:12", 0, now, false);
        System.out.println("head");
        Object expResult = "22:12:12:27/04/2018";
        Object result = instance.head();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Alarm.
     * @throws queuemanager.QueueOverflowException              Throws exception if queue overflow exception occurs.
     * @throws java.text.ParseException ParseException          Throws exception if parse exception occurs.
     * @throws queuemanager.QueueUnderflowException             QueueUnderflowException  Throws exception if queue underflow exception occurs.
     */
    @Test
    public void testToString() throws QueueOverflowException, ParseException, QueueUnderflowException {
        instance.add("22:12:12", 0, now, false);
        instance.add("23:12:12", 0, now, false);
        System.out.println("toString");
        //NOTE: Getting correct result is nigh on impossible due to the priority being calculated and thus changing every time
        String expResult = "(22:12:12:27/04/2018, 20946), (23:12:12:27/04/2018, 24546)";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of alert method, of class Alarm.
     * @throws java.lang.Exception  Throws exception.
     */
    @Test
    public void testAlert() throws Exception {
        System.out.println("alert");
        instance.alert();
    }
    
}

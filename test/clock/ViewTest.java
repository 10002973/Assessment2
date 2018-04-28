/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;

/**
 * This class runs test on the view class.
 * @author Heather Taylor-Stanley 10002973
 */
public class ViewTest {
    View instance;
    Model model;
    
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    //Get todays date
    Date today = new Date();
    //Format todays date
    String dates = new String();
    
    public ViewTest() throws IOException, FileNotFoundException, ParseException, QueueOverflowException, QueueUnderflowException {
        model = new Model();
        instance = new View(model);
    }
      
    @Before
    public void setUp() throws ParseException, QueueOverflowException, QueueUnderflowException {
        today = Calendar.getInstance().getTime();
        //Format todays date
        String date = (formatter.format(today));
        today = formatter.parse(date);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of update method, of class View.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        Observable o = null;
        Object arg = null;
        instance.update(o, arg);
    }
    
    /**
     * Test of load method, of class View.
     * @throws java.lang.Exception
     */
    @Test
    public void testLoad() throws Exception {
        System.out.println("load");
        instance.load();
    }

    /**
     * Test of setAlarm method, of class View.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetAlarm() throws Exception {
        System.out.println("setAlarm");
        int h = 22;
        int m = 12;
        int s = 12;
        Date d = today;
        int selected = 0;
        instance.setAlarm(h, m, s, d, selected);

    }

    /**
     * Test of checkAlarm method, of class View.
     */
    @Test
    public void testCheckAlarm() {
        System.out.println("checkAlarm");
        instance.checkAlarm();
    }

    /**
     * Test of editAlarm method, of class View.
     * @throws java.lang.Exception
     */
    @Test
    public void testEditAlarm() throws Exception {
        instance.setAlarm(22, 12, 12, today, 0);
        System.out.println("editAlarm");
        instance.editAlarm();
    }

    /**
     * Test of alarmAlert method, of class View.
     * @throws java.lang.Exception
     */
    @Test
    public void testAlarmAlert() throws Exception {
        instance.setAlarm(22, 12, 12, today, 0);
        System.out.println("alarmAlert");
        instance.alarmAlert(0);
    }

    /**
     * Test of alarmError method, of class View.
     * @throws java.lang.Exception
     */
    @Test
    public void testAlarmError() throws Exception {
        System.out.println("alarmError");
        instance.alarmError();
    }
    
    /**
     * Test of deleteAlarm method, of class View.
     * @throws java.lang.Exception
     */
    @Test
    public void testDeleteAlarm() throws Exception {
        instance.setAlarm(22, 12, 12, today, 0);
        System.out.println("deleteAlarm");
        instance.deleteAlarm();
    }

    /**
     * Test of alarmFull method, of class View.
     * @throws java.lang.Exception
     */
    @Test
    public void testAlarmFull() throws Exception {
        System.out.println("alarmFull");
        instance.alarmFull();
    }

    /**
     * Test of saveAlarms method, of class View.
     * NOTE: Test will crash due to System exit line in saveAlarms method.
     * @throws java.lang.Exception
     */
    @Test
    public void testSaveAlarms() throws Exception {
        instance.setAlarm(22, 12, 12, today, 0);
        System.out.println("saveAlarms");
        instance.saveAlarms();
    }
}

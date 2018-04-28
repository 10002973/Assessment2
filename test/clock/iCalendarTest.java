/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;

/**
 * This class runs tests on the iCalendar class
 * @author Heather Taylor-Stanley
 */
public class iCalendarTest {
    //Set variables
    Model model = new Model();
    View view;
    iCalendar instance;
    
    //Initialise variables
    public iCalendarTest() throws IOException, QueueOverflowException, QueueUnderflowException, FileNotFoundException, ParseException {
        this.view = new View(model);
        instance = new iCalendar(model, view);
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of write method, of class iCalendar.
     * @throws java.lang.Exception
     */
    @Test
    public void testWrite() throws Exception {
        System.out.println("write");
        String name = "test";
        String location = "C:/Users/Heather/Documents";
        instance.write(name, location);
    }

    /**
     * Test of read method, of class iCalendar.
     * @throws java.lang.Exception
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("read");
        String f = "C:/Users/Heather/Documents/test.ics";
        instance.read(f);
    }
    
}

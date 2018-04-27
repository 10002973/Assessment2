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
 *
 * @author Heather
 */
public class iCalendarTest {
    
    Model model = new Model();
    View view;
    iCalendar instance;
    
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
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("read");
        String f = "C:/Users/Heather/Documents/test.ics";
        instance.read(f);
    }
    
}

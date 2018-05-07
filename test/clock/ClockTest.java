/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the clock class.
 * @author Heather Taylor-Stanley
 * @version "%I%, %G%"
 */
public class ClockTest {
    
    /**
     *  Constructor of class ClockTest.
     */
    public ClockTest() {
    }

    /**
     * Test of main method, of class Clock.
     * @throws java.lang.Exception  Throws exception.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        Clock.main(args);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Heather
 */
public class ModelTest {
    
    public ModelTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of update method, of class Model.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        Model instance = new Model();
        instance.update();
    }
    
}

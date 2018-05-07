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
 * This class runs tests on the model class.
 * @author Heather Taylor-Stanley 10002973
 * @version "%I%"
 * @date 07/05/2018
 */
public class ModelTest {
    
    /**
     * Constructor of class ModelTest.
     */
    public ModelTest() {
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

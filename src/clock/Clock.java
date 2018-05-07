package clock;

import java.io.IOException;
import java.text.ParseException;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;

/**
 * This class is the main class for the program.
 * @author Heather Taylor-Stanley 10002973
 * @version "%I%"
 * @date 07/05/2018
 * 
 */
public class Clock {
    
    /**
     * The main method for the software, initialises the clock.
     * @param args  Passed arguments.
     * @throws QueueOverflowException       Throws exception if queue overflow occurs.
     * @throws QueueUnderflowException      Throws exception if queue underflow occurs.
     * @throws IOException                  Throws exception if input output error occurs.
     * @throws ParseException               Throws exception if parse error occurs.
     */
    public static void main(String[] args) throws QueueOverflowException, QueueUnderflowException, IOException, ParseException {
        Model model = new Model();
        View view = new View(model);
        model.addObserver(view);
        Controller controller = new Controller(model, view);
    }
}

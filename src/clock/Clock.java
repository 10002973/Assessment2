package clock;

import java.io.IOException;
import java.text.ParseException;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;

/**
 *
 * @author Heather Taylor-Stanley 10002973
 * 
 * 
 */
public class Clock {
    
    /**
     *
     * @param args
     * @throws QueueOverflowException
     * @throws QueueUnderflowException
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args) throws QueueOverflowException, QueueUnderflowException, IOException, ParseException {
        Model model = new Model();
        View view = new View(model);
        model.addObserver(view);
        Controller controller = new Controller(model, view);
    }
}

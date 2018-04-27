package clock;

import java.io.IOException;
import java.text.ParseException;
import queuemanager.QueueOverflowException;
import queuemanager.QueueUnderflowException;

public class Clock {
    
    public static void main(String[] args) throws QueueOverflowException, QueueUnderflowException, IOException, ParseException {
        Model model = new Model();
        View view = new View(model);
        model.addObserver(view);
        Controller controller = new Controller(model, view);
    }
}

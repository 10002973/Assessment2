package clock;

import java.util.Calendar;
import java.util.Observable;
//import java.util.GregorianCalendar;

/**
 * This class controls the data of the software, it holds the current date and time, and updates the program when the time changes.
 * @author Heather Taylor-Stanley 10002973
 * @version "%I%, %G%"
 * 
 */
public class Model extends Observable {
    
    int hour = 0;
    int minute = 0;
    int second = 0;
    int fullhour = 0;
    
    int oldSecond = 0;
    
    /**
     *  Model constructor.
     */
    public Model() {
        update();
    }
    
    /**
     * Gets the current time and date, and if the time changes it updates the program.
     * 
     */
    public void update() {
        Calendar date = Calendar.getInstance();
        hour = date.get(Calendar.HOUR);
        //Get hour in 24 hour format for alarm priority queue calculation
        fullhour = date.get(Calendar.HOUR_OF_DAY);
        minute = date.get(Calendar.MINUTE);
        oldSecond = second;
        second = date.get(Calendar.SECOND);
        if (oldSecond != second) {
            setChanged();
            notifyObservers();
        }
    }
}
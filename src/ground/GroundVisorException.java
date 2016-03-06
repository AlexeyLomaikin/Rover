package ground;

/**
 * Created by AlexL on 06.03.2016.
 */
public class GroundVisorException extends RuntimeException {

    public GroundVisorException(){
        super("Invalid cell coordinates");
    }

}

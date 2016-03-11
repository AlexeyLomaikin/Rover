package rover;

import enums.Direction;

/**
 * Created by AlexL on 27.02.2016.
 */
public interface Turnable {

    /**
     Execute turn command
     @param direction to turn
     @throws NullPointerException if Turnable is null or direction is null
     */

    void turnTo(Direction direction) throws NullPointerException;
}

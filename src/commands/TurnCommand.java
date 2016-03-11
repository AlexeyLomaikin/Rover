package commands;

import enums.Direction;
import rover.Turnable;

import java.util.ArrayList;

/**
 * Created by AlexL on 11.03.2016.
 */
public class TurnCommand implements RoverCommand{
    private Turnable turnable;
    private Direction direction;

    /**
     @throws NullPointerException if turnable is null or direction is null
     */
    public TurnCommand(Turnable turnable, Direction direction) throws NullPointerException{
        if (turnable == null || direction == null)
            throw new NullPointerException("Invalid args");
        else {
            this.turnable = turnable;
            this.direction = direction;
        }
    }


    @Override
    public void execute() {
        this.turnable.turnTo(this.direction);
    }
}

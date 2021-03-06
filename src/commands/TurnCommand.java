package commands;

import enums.Direction;
import rover.Turnable;

/**
 * Created by AlexL on 11.03.2016.
 */
public class TurnCommand implements RoverCommand{
    private Turnable turnable;
    private Direction direction;

    /**
     @throws NullPointerException if turnable is null or direction is null
     */
    public TurnCommand(Turnable turnable, Direction direction) throws IllegalArgumentException{
        if (turnable == null || direction == null)
            throw new IllegalArgumentException("turnable and direction can't be null");
        else {
            this.turnable = turnable;
            this.direction = direction;
        }
    }

    @Override
    public String toString(){
        return "TurnCommand(direction = " + direction.name() + ")";
    }

    @Override
    public void execute() {
        this.turnable.turnTo(this.direction);
    }
}

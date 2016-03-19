package commands;

import rover.Moveable;

/**
 * Created by AlexL on 11.03.2016.
 */
public class MoveCommand implements RoverCommand {
    private Moveable moveable;
    private int x,y;

    /**
     @throws NullPointerException if moveable is null
     */
    public MoveCommand(Moveable moveable, int x, int y) throws NullPointerException{
        if (moveable == null)
            throw new NullPointerException("Invalid args: moveable is null");
        else {
            this.moveable = moveable;
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void execute(){
        this.moveable.move(this.x, this.y);
    }
}

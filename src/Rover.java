import enums.Direction;
import ground.GroundVisor;
import ground.GroundVisorException;

/**
 * Created by AlexL on 27.02.2016.
 */
public class Rover implements Moveable, Turnable{
    private Direction direction = Direction.EAST;
    private int x;
    private int y;
    private GroundVisor visor;

    public Rover(){
        this.visor = new GroundVisor();
    }

    public Rover(GroundVisor visor){
       this.visor = (visor != null?visor:new GroundVisor());
    }

    public GroundVisor getVisor(){
        return this.visor;
    }

    @Override
    public void move(int x, int y) throws GroundVisorException{
        if ( !this.visor.hasObstacles(x, y) ) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void turnTo(Direction direction){
        this.direction = direction;
    }
}

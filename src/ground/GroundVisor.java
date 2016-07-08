package ground;
import enums.CellState;

/**
 * Created by AlexL on 06.03.2016.
 */
public class GroundVisor {
    private Ground ground;

    public GroundVisor(){
        this.ground = new Ground(1,1);
    }

    public GroundVisor(Ground ground){
        this.ground = (ground != null ? ground : new Ground(1,1));
    }

    public Ground getGround(){
        return this.ground;
    }

    public void setGround(Ground ground){
        if (ground != null)
            this.ground = ground;
    }

    public boolean hasObstacles(int x, int y) throws GroundVisorException {
        if ( x >= ground.getWidth()|| y >= ground.getLength() )
            throw new GroundVisorException();
        else if ( x < 0 || y < 0)
            throw new GroundVisorException();
        else
            return ground.getCell(x,y).getState() != CellState.FREE;
    }
}

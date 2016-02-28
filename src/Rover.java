/**
 * Created by AlexL on 27.02.2016.
 */
public class Rover implements Moveable, Turnable{
    private Direction direction;
    private int x;
    private int y;

    @Override
    public void move(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void turnTo(Direction direction){
        this.direction = direction;
    }
}

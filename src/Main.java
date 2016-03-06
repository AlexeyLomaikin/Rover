import ground.Ground;

/**
 * Created by AlexL on 06.03.2016.
 */
public class Main {
    public static void main(String[] args){
        Rover rover = new Rover();
        rover.getVisor().setGround(new Ground(5,5));
        rover.move(4,4);
    }
}

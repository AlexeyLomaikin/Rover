package ground;

import enums.CellState;

/**
 * Created by AlexL on 06.03.2016.
 */
public class GroundCell {
    private CellState state = CellState.FREE;
    private int x;
    private int y;

    public GroundCell(int x, int y){
        this.x = x;
        this.y = y;
    }

    public GroundCell(int x, int y, CellState state){
        this(x, y);
        this.state = state;
    }

    public CellState getState(){
        return this.state;
    }
}

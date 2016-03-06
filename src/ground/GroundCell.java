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
        this.state = (state != null ? state : CellState.FREE);
    }

    public CellState getState(){
        return this.state;
    }
    public void setState(CellState state){
        if (state != null)
            this.state = state;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
}

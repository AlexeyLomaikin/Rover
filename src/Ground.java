/**
 * Created by AlexL on 06.03.2016.
 */
public class Ground {
    private GroundCell[][] landscape;
    private int length;
    private int width;

    public Ground(int length, int width){
        this.length = length;
        this.width = width;
        this.landscape = new GroundCell[width][length];
        for(int i = 0; i < this.width; i++)
            for(int j = 0; j < this.length; j++)
                this.landscape[i][j] = new GroundCell(i, j);
    }
}

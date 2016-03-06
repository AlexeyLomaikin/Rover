package ground;

/**
 * Created by AlexL on 06.03.2016.
 */
public class Ground {
    private GroundCell[][] landscape;
    private int length;
    private int width;

    public Ground(int length, int width){
        if (length == 0 || length <0)
            this.length = 1;
        else
            this.length = length;
        if (width == 0 || width <0)
            this.width = 1;
        else
            this.width = width;
        this.landscape = new GroundCell[this.width][this.length];
        for(int i = 0; i < this.width; i++)
            for(int j = 0; j < this.length; j++)
                this.landscape[i][j] = new GroundCell(i, j);
    }

    public int getLength(){
        return  this.length;
    }

    public int getWidth(){
        return this.width;
    }

    public GroundCell[][] getLandscape(){
        return this.landscape;
    }

    public GroundCell getCell(int x, int y) throws ArrayIndexOutOfBoundsException{
        return this.landscape[x][y];
    }

}

package rover;

import commands.*;
import enums.Direction;
import ground.GroundVisor;
import ground.GroundVisorException;
import parsers.TextRoverCommandParser;
import parsers.XMLRoverCommandParser;

import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by AlexL on 27.02.2016.
 */
public class Rover implements Moveable, Turnable, ProgramFileAware {
    private Direction direction = Direction.EAST;
    private int x;
    private int y;
    private TextRoverCommandParser textParser;
    private XMLRoverCommandParser xmlParser;
    private GroundVisor visor;
    private LinkedList<RoverCommand> commands = new LinkedList<>();
    private static Logger logger = Logger.getLogger(Rover.class.getName());

    public Rover(){
        this.visor = new GroundVisor();
        this.textParser = new TextRoverCommandParser(this);
        this.xmlParser = new XMLRoverCommandParser(this);
        xmlParser.setFile("./config.xml");
    }

    public Rover(GroundVisor visor){
        this.visor = (visor != null ? visor : new GroundVisor());
        this.textParser = new TextRoverCommandParser(this);
        this.xmlParser = new XMLRoverCommandParser(this);
    }

    public GroundVisor getVisor(){
        return this.visor;
    }

    public void setVisor(GroundVisor visor){
        if (visor != null)
            this.visor = visor;
    }

    public TextRoverCommandParser getTextParser(){
        return this.textParser;
    }

    public XMLRoverCommandParser getXMLParser(){
        return this.xmlParser;
    }

    public LinkedList<RoverCommand> getCommandList(){
        return this.commands;
    }

    @Override
    public void move(int x, int y) {
        try {
            if (!this.visor.hasObstacles(x, y)) {
                this.x = x;
                this.y = y;
                Rover.logger.info("Rover moved to cell: " + x + " " + y);
            }
            else
                Rover.logger.info("Rover can't move to occupied cell: " + x + " " + y);
        } catch (GroundVisorException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void turnTo(Direction direction){
        if (direction == null)
            Rover.logger.info("Rover can't turn : direction is null");
        else {
            this.direction = direction;
            Rover.logger.info("Rover turned to " + direction);
        }
    }

    @Override
    public void executeProgramFile(String filename){
        new ImportCommand(this, filename).execute();
        while ( this.commands.size() != 0) {
            RoverCommand command = this.commands.pollFirst();
            new LoggingCommand(command).execute();
        }
    }
}

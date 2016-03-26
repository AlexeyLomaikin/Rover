package rover;

import commands.*;
import enums.Direction;
import ground.GroundVisor;
import ground.GroundVisorException;
import parsers.TextRoverCommandParser;
import parsers.XMLRoverCommandParser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by AlexL on 27.02.2016.
 */
public class Rover implements Moveable, Turnable, ProgramFileAware {
    private Direction direction = Direction.EAST;
    private int x;
    private int y;

    private LinkedList<RoverCommand> commands = new LinkedList<>();
    private TextRoverCommandParser textParser;
    private XMLRoverCommandParser xmlParser;

    /*
    for check cyclic import
    String - filename
    Integer - number of commands in this file + commands in it child files
    */
    private HashMap<String, Integer> executableFilesInfo = new HashMap<>();
    private RoverCommand lastExecutedCommand;

    private GroundVisor visor;

    private static Logger logger = Logger.getLogger(Rover.class.getName());

    public Rover(){
        this.visor = new GroundVisor();
        this.textParser = new TextRoverCommandParser(this);
        this.xmlParser = new XMLRoverCommandParser(this);
    }

    public Rover(GroundVisor visor){
        this.visor = (visor != null ? visor : new GroundVisor());
        this.textParser = new TextRoverCommandParser(this);
        this.xmlParser = new XMLRoverCommandParser(this);
    }

    public GroundVisor getVisor(){
        return this.visor;
    }

    public HashMap<String, Integer> getExecuteFilesInfo() {
        return this.executableFilesInfo;
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
        this.lastExecutedCommand = null;
        new LoggingCommand(new ImportCommand(this, filename)).execute();
        while ( this.commands.size() != 0) {
            RoverCommand command = this.commands.pollFirst();
            new LoggingCommand(command).execute();
            this.lastExecutedCommand = command;

            //decrement number of commands in all files
            this.updateExecuteFilesInfo(-1);
        }
    }

    /*
    update info about commands associated with the each file
    diff       < 0  number of executed commands
               > 0  number off added commands
    if file have no more command(num <=0) - delete it from map
    */
    public void updateExecuteFilesInfo(int diff) {
        Iterator<Map.Entry<String, Integer>> iterator = this.executableFilesInfo.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();

            //if last command was import
            // don't decrement num of commands for imported file
            if ( lastExecutedCommand instanceof ImportCommand && diff < 0){
                String lastFileName = ((ImportCommand)lastExecutedCommand).getFilename();
                if ( lastFileName.equals(entry.getKey()))
                    continue;
            }

            int newNum = entry.getValue() + diff;
            if ( newNum <= 0 )
                iterator.remove();
            else
                entry.setValue(newNum);
        }
    }
}

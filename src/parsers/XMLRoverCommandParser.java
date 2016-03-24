package parsers;

import commands.RoverCommand;
import rover.Rover;

import java.io.File;

/**
 * Created by AlexL on 24.03.2016.
 */
public class XMLRoverCommandParser implements CommandParser {

    private Rover rover;
    private String filename;

    public XMLRoverCommandParser(Rover rover) throws NullPointerException{
        if (rover == null)
            throw new NullPointerException("Invalid args: rover is null");
        this.rover = rover;
    }

    public void setFile(String filename){
        this.filename = filename;
        File docFile = new File("Doctors.xml");
    }

    @Override
    public RoverCommand readNextCommand(){
        if ( filename == null )
            throw new RoverCommandParserException("File not set");
        else if ( !filename.matches("^.+\\.xml$") )
            throw new RoverCommandParserException("Invalid filename");
        else return null;
    }
}

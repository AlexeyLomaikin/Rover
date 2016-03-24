package parsers;

import commands.RoverCommand;
import org.w3c.dom.Document;
import rover.Rover;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by AlexL on 24.03.2016.
 */
public class XMLRoverCommandParser implements CommandParser {

    private Rover rover;
    private String filename;

    public XMLRoverCommandParser(Rover rover) throws IllegalArgumentException{
        if (rover == null)
            throw new IllegalArgumentException("rover can't be null");
        this.rover = rover;
    }

    public void setFile(String filename){
        this.filename = filename;
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

package parsers;

import commands.ImportCommand;
import commands.MoveCommand;
import commands.RoverCommand;
import commands.TurnCommand;
import enums.Direction;
import parsers.RoverCommandParserException;
import rover.Rover;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by AlexL on 11.03.2016.
 */
public class TextRoverCommandParser implements CommandParser {
    private Rover rover;
    private BufferedReader reader;

    public TextRoverCommandParser(Rover rover) throws NullPointerException{
        if (rover == null)
            throw new NullPointerException("Invalid args: rover is null");
        else
            this.rover = rover;
    }

    public void setReader(BufferedReader reader){
        this.reader = reader;
    }

    @Override
    public RoverCommand readNextCommand() throws RoverCommandParserException {
        if (reader == null)
            throw new RoverCommandParserException("Reader not set");

        String command = null;
        try{
            command = reader.readLine();
        }catch (IOException ex){
            ex.printStackTrace();
        }

        if (command != null) {
            if ( command.split(" ").length < 2)
                throw new RoverCommandParserException("Incorrect command");

            if ( command.toLowerCase().startsWith("move") ) {
                try{
                    String[] params = command.split(" ");
                    int x = Integer.parseInt(params[1]);
                    int y = Integer.parseInt(params[2]);
                    return new MoveCommand(this.rover, x, y);
                }catch( NumberFormatException | ArrayIndexOutOfBoundsException ex ){
                    throw new RoverCommandParserException("Incorrect format of move command");
                }
            }
            else if( command.toLowerCase().startsWith("turn") ) {
                try {
                    String[] params = command.split(" ");
                    Direction direction = Direction.valueOf(params[1]);
                    return new TurnCommand(this.rover, direction);
                } catch ( IllegalArgumentException | ArrayIndexOutOfBoundsException e ) {
                    throw new RoverCommandParserException("Incorrect format of turn command");
                }
            }
            else if( command.toLowerCase().startsWith("import") ){
                try{
                    String filename = command.substring(7);
                    return new ImportCommand(this.rover, filename);
                }catch( IndexOutOfBoundsException ex ){
                    throw new RoverCommandParserException("Incorrect format of import command");
                }
            }
            else
                throw new RoverCommandParserException("Unknown command '" + command + "'");
        }
        else
            return null;
    }
}
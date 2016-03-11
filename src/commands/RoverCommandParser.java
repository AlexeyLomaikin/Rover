package commands;

import enums.Direction;
import rover.Rover;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by AlexL on 11.03.2016.
 */
public class RoverCommandParser {
    private Rover rover;
    private BufferedReader reader;

    public RoverCommandParser(Rover rover) throws NullPointerException{
        if (rover == null)
            throw new NullPointerException("Invalid args: rover is null");
        else {
            this.rover = rover;
        }
    }

    public RoverCommandParser(Rover rover, BufferedReader reader) throws NullPointerException{
        if (rover == null || reader == null)
            throw new NullPointerException("Invalid args");
        else {
            this.rover = rover;
            this.reader = reader;
        }
    }

    public void setReader(BufferedReader reader){
        this.reader = reader;
    }

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
            String[] params = command.split(" ");
            if ( params.length < 2)
                throw new RoverCommandParserException("Incorrect command");

            if ( params[0].equals("move") && params.length == 3) {
                try{
                    int x = Integer.parseInt(params[1]);
                    int y = Integer.parseInt(params[2]);
                    return new MoveCommand(this.rover, x, y);
                }catch(NumberFormatException ex){
                    throw new RoverCommandParserException("Incorrect format of move command");
                }
            }
            else if( params[0].equals("turn") ) {
                try {
                    Direction direction = Direction.valueOf(params[1]);
                    return new TurnCommand(this.rover, direction);
                } catch (IllegalArgumentException e) {
                    throw new RoverCommandParserException("Incorrect format of turn command");
                }
            }
            else
                throw new RoverCommandParserException("Unknown command");
        }
        else
            return null;
    }
}
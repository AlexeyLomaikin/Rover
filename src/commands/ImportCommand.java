package commands;

import parsers.RoverCommandParserException;
import rover.Rover;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Created by AlexL on 19.03.2016.
 */
public class ImportCommand implements RoverCommand {

    private Rover rover;
    private String filename;
    private static Logger logger = Logger.getLogger(ImportCommand.class.getName());

    public ImportCommand(Rover rover, String filename) throws IllegalArgumentException {
        if (rover == null || filename == null)
            throw new IllegalArgumentException("rover and filename can't be null");
        this.rover = rover;
        this.filename = filename;
    }
    @Override
    public String toString(){
        return "ImportCommand(filename = " + filename + ")";
    }

    public String getFilename(){
        return this.filename;
    }

    @Override
    public void execute() {
        try (FileInputStream fis = new FileInputStream(this.filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))
        ) {

            this.rover.getTextParser().setReader(reader);

            RoverCommand command = null;
            //Start index to add commands from another file
            int i = 0;

            do {
                try {
                    command = this.rover.getTextParser().readNextCommand();

                    if ( command == null )
                        break;
                    //file "example1" can't import file "example1"
                    else if ( command instanceof ImportCommand){
                        ImportCommand curCommand = (ImportCommand)command;
                        if ( curCommand.getFilename().equals(this.filename) ) {
                            ImportCommand.logger.info("Can't execute command: " + curCommand.toString() +"" +
                                    "\nCause: cycle in import\n");
                            continue;
                        }
                    }

                    this.rover.getCommandList().add(i, command);
                    //Add next command
                    i++;
                } catch (RoverCommandParserException parsEx) {
                    parsEx.printStackTrace();
                }
            } while (command != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

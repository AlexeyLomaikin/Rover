package commands;

import parsers.CommandParser;
import parsers.RoverCommandParserException;
import parsers.TextRoverCommandParser;
import parsers.XMLRoverCommandParser;
import rover.Rover;

import java.io.*;
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
        if (!filename.matches("^.+\\.(txt|xml)$"))
            throw new IllegalArgumentException("Incorrect file format");

        try{
            String fullFileName = new File(filename).getCanonicalPath();
            this.rover = rover;
            this.filename = fullFileName;
        }catch (IOException e){
            throw new IllegalArgumentException("Invalid file name");
        }
    }

    @Override
    public String toString() {
        return "ImportCommand(filename = " + filename + ")";
    }

    public String getFilename() {
        return this.filename;
    }

    @Override
    public void execute() throws IllegalArgumentException {
        if (this.filename.endsWith(".txt"))
            try (FileInputStream fis = new FileInputStream(this.filename);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(fis))
            ) {
                TextRoverCommandParser parser = this.rover.getTextParser();
                parser.setReader(reader);
                this.importCommands(parser);
            } catch (IOException e) {
                e.printStackTrace();
            }
        else if (this.filename.endsWith(".xml")) {
            XMLRoverCommandParser parser = this.rover.getXMLParser();
            parser.setFile(this.filename);
            this.importCommands(parser);
        }
    }

    private void importCommands(CommandParser parser) throws IllegalArgumentException {
        if (parser == null)
            throw new IllegalArgumentException("parser can't be null");

        //put new importing filename in map
        this.rover.getExecuteFilesInfo().put(this.filename, 0);

        RoverCommand command = null;
        //Start index to add commands from another file
        int i = 0;

        do {
            try {
                command = parser.readNextCommand();

                if ( command == null )
                    break;
                if ( command instanceof ImportCommand ){
                    String filename = ((ImportCommand)command).getFilename();
                    //cyclic import
                    if ( this.rover.getExecuteFilesInfo().containsKey(filename) ) {
                        ImportCommand.logger.info("Error: cyclic import " + this.filename + " -> " + filename + "\n");
                        continue;
                    }
                }
                //Add next command

                this.rover.getCommandList().add(i, command);
                i++;
            } catch (RoverCommandParserException parsEx) {
                parsEx.printStackTrace();
            }
        } while (command != null);

        //update number of commands in this file + commands in it parent files
        this.rover.updateExecuteFilesInfo(i);
    }

}

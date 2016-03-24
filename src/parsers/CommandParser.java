package parsers;

import commands.RoverCommand;

/**
 * Created by AlexL on 24.03.2016.
 */
public interface CommandParser {
    RoverCommand readNextCommand() throws RoverCommandParserException;
}

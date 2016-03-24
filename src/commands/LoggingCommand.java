package commands;

import java.util.logging.Logger;

/**
 * Created by AlexL on 19.03.2016.
 */
public class LoggingCommand implements RoverCommand{

    private RoverCommand command;
    private static Logger logger = Logger.getLogger(LoggingCommand.class.getName());

    public LoggingCommand(RoverCommand command) throws NullPointerException{
        if ( command == null )
            throw new NullPointerException("command is null");
        this.command = command;
    }

    @Override
    public void execute(){
        this.command.execute();
        LoggingCommand.logger.info(command.toString() + " successfully executed!\n");
    }
}

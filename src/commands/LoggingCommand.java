package commands;

import java.util.logging.Logger;

/**
 * Created by AlexL on 19.03.2016.
 */
public class LoggingCommand implements RoverCommand{

    private RoverCommand command;

    public LoggingCommand(RoverCommand command) throws NullPointerException{
        if ( command == null )
            throw new NullPointerException("command is null");
        this.command = command;
    }

    @Override
    public void execute(){
        this.command.execute();
        if(command instanceof MoveCommand)
            Logger.getLogger(LoggingCommand.class.getName()).info("MoveCommand successfully executed!");
        else if(command instanceof TurnCommand)
            Logger.getLogger(LoggingCommand.class.getName()).info("TurnCommand successfully executed!");

    }
}

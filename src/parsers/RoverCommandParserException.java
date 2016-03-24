package parsers;

/**
 * Created by AlexL on 11.03.2016.
 */
public class RoverCommandParserException extends RuntimeException {

    public RoverCommandParserException(){
        super();
    }
    public RoverCommandParserException(String message){
        super(message);
    }
}

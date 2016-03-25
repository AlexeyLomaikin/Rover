package parsers;

import commands.ImportCommand;
import commands.MoveCommand;
import commands.RoverCommand;
import commands.TurnCommand;
import enums.Direction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import rover.Rover;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.LinkedList;

/**
 * Created by AlexL on 24.03.2016.
 */
public class XMLRoverCommandParser implements CommandParser {

    private Rover rover;
    private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private LinkedList<Node> commandNodes = new LinkedList<>();

    public XMLRoverCommandParser(Rover rover) throws IllegalArgumentException{
        if (rover == null)
            throw new IllegalArgumentException("rover can't be null");
        this.rover = rover;
    }

    public void setFile(String filename) throws RoverCommandParserException, IllegalArgumentException{
        if ( filename == null )
            throw new IllegalArgumentException("Filename can't be null");
        else if ( !filename.matches("^.+\\.xml$") )
            throw new IllegalArgumentException("Invalid file format");
        try{
            File file = new File(filename);
            if( !file.exists() || file.length() == 0 )
                throw new RoverCommandParserException("File don't exist or empty");
            parseFile(file);
            readNextCommand();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseFile(File file) throws Exception{
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(file);
        NodeList commandsNodes = document.getElementsByTagName("commands");
        if ( commandsNodes.getLength() != 1)
            throw new RoverCommandParserException("Not valid xml!");
        else {
            NodeList commandNodes = commandsNodes.item(0).getChildNodes();
            for ( int i = 0; i < commandNodes.getLength(); i++ ) {
                Node commandNode = commandNodes.item(i);
                if ( commandNode.getNodeType() == Node.ELEMENT_NODE ) {
                    String tag = ((Element)commandNode).getTagName();
                    if ( !tag.equals("command") )
                        throw new RoverCommandParserException("Not valid xml!");
                    this.commandNodes.add(commandNode);
                }
            }
        }
    }

    @Override
    public RoverCommand readNextCommand(){
        if ( this.commandNodes.size() == 0 )
            return null;

        Node commandNode = this.commandNodes.pollFirst();

        if ( commandNode.getNodeType() != Node.ELEMENT_NODE )
            throw new RoverCommandParserException("Invalid xml!");
        else{
            Element el = (Element) commandNode;
            String command = el.getTagName();
            if ( command.toLowerCase().startsWith("move") ) {
                try{
                    NodeList params = el.getChildNodes();
                    int x = Integer.parseInt(params.item(0).getTextContent());
                    int y = Integer.parseInt(params.item(1).getTextContent());
                    return new MoveCommand(this.rover, x, y);
                }catch( NumberFormatException | ArrayIndexOutOfBoundsException ex ){
                    throw new RoverCommandParserException("Incorrect format of move command");
                }
            }
            else if( command.toLowerCase().startsWith("turn") ) {
                try {
                    NodeList params = el.getChildNodes();
                    Direction direction = Direction.valueOf(params.item(0).getTextContent());
                    return new TurnCommand(this.rover, direction);
                } catch ( IllegalArgumentException | ArrayIndexOutOfBoundsException e ) {
                    throw new RoverCommandParserException("Incorrect format of turn command");
                }
            }
            else if( command.toLowerCase().startsWith("import") ){
                try{
                    NodeList params = el.getChildNodes();
                    String filename = params.item(0).getTextContent();
                    return new ImportCommand(this.rover, filename);
                }catch( IndexOutOfBoundsException ex ){
                    throw new RoverCommandParserException("Incorrect format of import command");
                }
            }
            else
                throw new RoverCommandParserException("Unknown command '" + command + "'");
        }
    }
}

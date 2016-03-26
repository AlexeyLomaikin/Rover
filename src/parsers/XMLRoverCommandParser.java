package parsers;

import commands.ImportCommand;
import commands.MoveCommand;
import commands.RoverCommand;
import commands.TurnCommand;
import enums.Direction;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import rover.Rover;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    public void setFile(String filename) throws IllegalArgumentException{
        if ( filename == null )
            throw new IllegalArgumentException("Filename can't be null");
        else if ( !filename.matches("^.+\\.xml$") )
            throw new IllegalArgumentException("Invalid file format");

        File file = new File(filename);
        if( !file.exists() || file.length() == 0 )
            throw new IllegalArgumentException("File doesn't exist or empty");

        parseFile(file);
    }

    private void parseFile(File file) throws  RoverCommandParserException {
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(file);

            NodeList commandsNodes = document.getElementsByTagName("commands");
            if (commandsNodes.getLength() != 1)
                throw new RoverCommandParserException("Not valid xml: have no root node 'commands'!");
            else {
                NodeList commandNodes = commandsNodes.item(0).getChildNodes();
                for (int i = 0; i < commandNodes.getLength(); i++) {
                    Node commandNode = commandNodes.item(i);
                    if ( commandNode.getNodeType() == Node.ELEMENT_NODE ) {
                        String tag = ((Element) commandNode).getTagName();
                        if (!tag.equals("command"))
                            throw new RoverCommandParserException("Not valid xml: not 'command' tag in 'commands' children!");
                        this.commandNodes.add(commandNode);
                    }
                }
            }
        }catch ( ParserConfigurationException | SAXException | IOException e ){
            e.printStackTrace();
            throw new RoverCommandParserException( e.getClass().getName() + " in parseFile method");
        }
    }

    @Override
    public RoverCommand readNextCommand() throws RoverCommandParserException{
        if ( this.commandNodes.size() == 0 )
            return null;

        Node commandNode = this.commandNodes.pollFirst();
        NamedNodeMap attributes = commandNode.getAttributes();
        if ( attributes == null )
            throw new RoverCommandParserException("Not valid xml: have no commandType attribute");

        String commandType = attributes.getNamedItem("type").getNodeValue();
        if ( commandType == null )
            throw new RoverCommandParserException("Not valid xml: have no commandType attribute");

        if ( commandType.toLowerCase().equals("move") ) {
            try{
                List<Node> params = this.parseParams(commandNode);
                int x = Integer.parseInt(params.get(0).getTextContent());
                int y = Integer.parseInt(params.get(1).getTextContent());
                return new MoveCommand(this.rover, x, y);
            }catch( ArrayIndexOutOfBoundsException | IllegalArgumentException ex ){
                throw new RoverCommandParserException("Incorrect format of move command");
            }
        }
        else if( commandType.toLowerCase().equals("turn") ) {
            try {
                List<Node> params = this.parseParams(commandNode);
                Direction direction = Direction.valueOf(params.get(0).getTextContent());
                return new TurnCommand(this.rover, direction);
            } catch ( ArrayIndexOutOfBoundsException | IllegalArgumentException e ) {
                throw new RoverCommandParserException("Incorrect format of turn command");
            }
        }
        else if( commandType.toLowerCase().equals("import") ){
            try{
                List<Node> params = this.parseParams(commandNode);
                String filename = params.get(0).getTextContent();
                return new ImportCommand(this.rover, filename);
            }catch( ArrayIndexOutOfBoundsException | IllegalArgumentException e ){
                throw new RoverCommandParserException("Incorrect format of import command");
            }
        }
        else
            throw new RoverCommandParserException("Unknown commandType '" + commandType + "'");
    }

    private List<Node> parseParams(Node command){
        List<Node> params = new ArrayList<>();
        NodeList paramsNodes = command.getChildNodes();
        for ( int i = 0; i < paramsNodes.getLength(); i++ )
            if ( paramsNodes.item(i).getNodeType() == Node.ELEMENT_NODE )
                params.add(paramsNodes.item(i));
        return params;
    }
}

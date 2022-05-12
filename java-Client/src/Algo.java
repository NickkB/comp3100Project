import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import ServerEvents.JOBNEvent;

public class Algo{

    Utilities utilities;
    ClientAction clientAction;
    ServerInfo servers;
    JOBNEvent job;

    public Algo() throws IOException, ParserConfigurationException, SAXException{
        utilities = new Utilities();
        clientAction = new ClientAction();
        utilities.initConnection();
        servers = new ServerInfo();    
        

        boolean stopLoop = true; 
        while(stopLoop){
            String[] serverInput = utilities.readServerOutput();
            switch(serverInput[0]){
                case "JOBN":
                    job = new JOBNEvent(serverInput[1], serverInput[2], serverInput[3], serverInput[4], serverInput[5], serverInput[6]);
                    clientAction.sendGETSCapable(job.core, job.memory, job.disk, utilities.getOutputStream());
                    serverInput = utilities.readServerOutput();
                    break;
                case "OK":
                    clientAction.sendREDY(utilities.getOutputStream());
                    break;
                case "NONE":
                    clientAction.sendQUIT(utilities.getOutputStream());
                    break;
                case "QUIT":
                    utilities.closeConnection();
                    stopLoop = false;
                    break;
                case "JCPL":
                    clientAction.sendREDY(utilities.getOutputStream());
                default:
                    break;
            }
        } 
    }
}

import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class LargestRoundRobbin{

    Utilities utilities;
    ClientAction clientAction;
    ServerInfo servers;
    JOBNEvent job;

    public LargestRoundRobbin() throws IOException, ParserConfigurationException, SAXException{
        utilities = new Utilities();
        clientAction = new ClientAction();
        servers = new ServerInfo();    
        
        utilities.initConnection();
        boolean stopLoop = true; 
        while(stopLoop){
            String[] serverInput = utilities.readServerOutput();
            switch(serverInput[0]){
                case "JOBN":
                    job = new JOBNEvent(serverInput[1], serverInput[2], serverInput[3], serverInput[4], serverInput[5], serverInput[6]);
                    clientAction.sendGETSCapable(job.core, job.memory, job.disk, utilities.getOutputStream());
                    serverInput = utilities.readServerOutput();
                    getNextServer(serverInput);
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


    public void getNextServer(String[] input) throws IOException{

        
        servers.importServers(new DATAEvent(input[1], input[2]), clientAction, utilities);
        if(servers.getLastUsedServer() == null){
            servers.sortListByCoreCount();
            clientAction.sendSCHD(job.jobID, servers.getServer(0).getType().toString(), servers.getServer(0).getID().toString(), utilities.getOutputStream());
            servers.setLastUsedServer(servers.getServer(0));
        }
        else if(servers.getServerByID(servers.getLastUsedServer().getType(), (servers.getLastUsedServer().getID() + 1)) == null){
            clientAction.sendSCHD(job.jobID, servers.getLastUsedServer().getType(), "0", utilities.getOutputStream());
            servers.setLastUsedServer(servers.getServerByID(servers.getLastUsedServer().getType(), 0));
        }
        else{
            Integer x = servers.getLastUsedServer().getID(); 
            x = x + 1;
            clientAction.sendSCHD(job.jobID, servers.getLastUsedServer().getType(), x.toString(), utilities.getOutputStream());
            servers.setLastUsedServer(servers.getServerByID(servers.getLastUsedServer().getType(), x));
        }
        
        // servers.printArray();
    }

}









import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Algo{

    Utilities utilities;
    ClientAction clientAction;
    ServerInfo servers;
    Job job;

    public Algo() throws IOException, ParserConfigurationException, SAXException{
        utilities = new Utilities();
        clientAction = new ClientAction();
        utilities.initConnection();
        servers = new ServerInfo();    
        

        boolean stopLoop = false; 
        while(!stopLoop){
            String[] serverInput = utilities.readServerOutput();
            switch(serverInput[0]){
                case "JOBN":
                    job = new Job(serverInput[2], "-1", serverInput[1], "-1", serverInput[3], serverInput[4], serverInput[5], serverInput[6]);
                    clientAction.sendGETSCapable(job.core, job.memory, job.disk, utilities.getOutputStream());
                    serverInput = utilities.readServerOutput();
                    servers.updateServerStates(serverInput[1], clientAction, utilities);
                    servers.sortListByEstWaitTime();
                    clientAction.sendSCHD(job.jobID, servers.tempServersList.get(0).getType(), servers.tempServersList.get(0).getID().toString(), utilities.getOutputStream());
                    break;
                case "OK":
                    clientAction.sendREDY(utilities.getOutputStream());
                    break;
                case "NONE":
                    clientAction.sendQUIT(utilities.getOutputStream());
                    break;
                case "QUIT":
                    utilities.closeConnection();
                    stopLoop = true;
                    break;
                case "JCPL":
                    clientAction.sendLSTJ(serverInput[3], serverInput[4], utilities.getOutputStream());
                    String[] tempInput = utilities.readServerOutput(); 
                    if(tempInput[0].equals(".")){
                        tempInput = utilities.readServerOutput();
                    }
                    clientAction.sendOK(utilities.getOutputStream());
                    int dataEvent = Integer.parseInt(tempInput[1]);
                    if(dataEvent == 0 && !serverInput[4].equals("0")){
                        tempInput = utilities.readServerOutput();
                        clientAction.sendTERM(serverInput[3], serverInput[4], utilities.getOutputStream());
                    }
                    else {
                        for(int i = 0; i < dataEvent; i++){
                            tempInput = utilities.readServerOutput();
                        }
                    }
                    clientAction.sendOK(utilities.getOutputStream());
                    tempInput = utilities.readServerOutput();
                    clientAction.sendREDY(utilities.getOutputStream());
                default:
                    break;
            }
        } 
    }

    public void schdWaitTimeOptimal(){
        
        
         
    }
















}

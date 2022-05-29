import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class SuperFast{

    ServerInfo servers;
    Job job;
    JobList globalJobList;

    public SuperFast() throws IOException, ParserConfigurationException, SAXException, InterruptedException{

        Utilities.initConnection();  
        globalJobList = new JobList();
        servers = new ServerInfo(globalJobList);  

        boolean stopLoop = false; 
        while(!stopLoop){
            String[] serverInput = Utilities.readServerOutput();
            switch(serverInput[0]){
                case "JOBN":
                    job = new Job(serverInput[2], "-1", serverInput[1], "-1", serverInput[3], serverInput[4], serverInput[5], serverInput[6]);
                    servers.submitJob(job);
                    break;
                case "OK":
                    ClientAction.sendREDY();
                    break;
                case "NONE":
                    ClientAction.sendQUIT();
                    break;
                case "QUIT":
                    Utilities.closeConnection();
                    stopLoop = true;
                    break;
                case "JCPL":
                    Server tempServer = servers.getServerByID(serverInput[3], serverInput[4]);
                    globalJobList.removeJob(serverInput[2]);
                    if(!tempServer.hasJobs()){
                        globalJobList.validateJobLists(servers.getAllServers());
                        ArrayList<Job> waitingJobs = globalJobList.getWaitingJobList();
                        if(waitingJobs == null || waitingJobs.isEmpty()){                            
                        }
                        else{
                            for(Job elm: waitingJobs){
                                servers.updateServerStates(elm);
                                if(servers.serverIsCapable(tempServer) && elm.getAssignedServer().serversNotEqual(tempServer)){
                                    servers.migrateJob(elm, elm.getAssignedServer(), tempServer);
                                    break;
                                }
                            }
                        } 
                    }
                    ClientAction.sendREDY();
                default:
                    break;
            
                }   
        }
    }

}    
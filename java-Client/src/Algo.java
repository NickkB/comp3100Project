import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Algo{

    Utilities utilities;
    ClientAction clientAction;
    ServerInfo servers;
    Job job;
    ArrayList<Job> jobList;
    int jobTimeAvg;
    int jobCount; 

    public Algo() throws IOException, ParserConfigurationException, SAXException, InterruptedException{
        utilities = new Utilities();
        clientAction = new ClientAction();
        utilities.initConnection();
        servers = new ServerInfo();    
        jobList = new ArrayList<>();
        jobTimeAvg = 0; 

        boolean stopLoop = false; 
        while(!stopLoop){
            String[] serverInput = utilities.readServerOutput();
            switch(serverInput[0]){
                case "JOBN":
                    boolean jobSubmitted = false;
                    int sysTime = Integer.parseInt(serverInput[1]);
                    job = new Job(serverInput[2], "-1", serverInput[1], "-1", serverInput[3], serverInput[4], serverInput[5], serverInput[6]);
                    jobList.add(job);
                    clientAction.sendGETSCapable(job.core, job.memory, job.disk, utilities.getOutputStream());
                    serverInput = utilities.readServerOutput();
                    servers.updateServerStates(sysTime, serverInput[1], clientAction, utilities);
                    for(Server elm: servers.tempServersList){
                        if((elm.getCoresInt() >= Integer.parseInt(job.core) && jobStateCheck(elm.serverType, elm.serverID.toString(), utilities.getOutputStream())) ){ 
                            job.assignServer(elm.getType(), elm.getID().toString());
                            clientAction.sendSCHD(job.jobID, elm.getType(), elm.getID().toString(), utilities.getOutputStream());
                            
                            jobSubmitted = true; 
                            break;
                        }
                    }
                    if(!jobSubmitted){
                        Server tempServer = servers.getServerByBestFit(job.core);
                        if(!tempServer.getState().equals("inactive") && tempServer.cores - Integer.parseInt(job.core) >= 0 && jobStateCheck(tempServer.serverType, tempServer.serverID.toString(), utilities.getOutputStream())) {
                            job.assignServer(tempServer.getType(), tempServer.getID().toString());
                            clientAction.sendSCHD(job.jobID, tempServer.getType(), tempServer.getID().toString(), utilities.getOutputStream());  
                                                      
                            jobSubmitted = true; 
                        } 
                    }
                    if(!jobSubmitted){
                        Server tempServer = servers.getServerByBestFit(job.core);
                        clientAction.sendSCHD(job.jobID, tempServer.getType(), tempServer.getID().toString(), utilities.getOutputStream());
                        
                    }
                    
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
                    Job tempJob = null;
                    clientAction.sendLSTJ(serverInput[3], serverInput[4], utilities.getOutputStream());
                    String[] tempInput = utilities.readServerOutput(); 

                    clientAction.sendOK(utilities.getOutputStream());
                    int dataEvent = Integer.parseInt(tempInput[1]);

                    if(dataEvent == 0){
                        // if(Integer.parseInt(serverInput[4]) == 0){
                        // clientAction.sendTERM(serverInput[3], serverInput[4], utilities.getOutputStream());
                        // tempInput = utilities.readServerOutput();
                        // }
                        // else{
                            jobList.clear();
                            servers.updateServerJobStates(clientAction, utilities);
                            for(int i = 0; i < servers.servers.size(); i++){
                                jobList.addAll(servers.servers.get(i).jobList);
                            }
                            jobList.sort(Comparator.comparing(Job::getSubmitTime).reversed());   
                            for(Job elm: jobList){
                                if(elm.jobState.equals("1") && tempJob == null){
                                    clientAction.sendGETSCapable(elm.core, elm.memory, elm.disk, utilities.getOutputStream());
                                    tempInput = utilities.readServerOutput();
                                    clientAction.sendOK(utilities.getOutputStream());
                                    dataEvent = Integer.parseInt(tempInput[1]);
                                    for(int i = 0; i < dataEvent; i++){
    
                                        tempInput = utilities.readServerOutput();   
                                        if(tempInput[0].equals(serverInput[3]) && tempInput[1].equals(serverInput[4]) && tempJob == null){
                                            tempJob = elm;
                                            
                                        }
                                        
                                    }
                                    clientAction.sendOK(utilities.getOutputStream());
                                }
                            } 
                            if(tempJob != null){
                                clientAction.sendMIGJ(tempJob.jobID, tempJob.assignedServerType, tempJob.assignedServerID, serverInput[3], serverInput[4], utilities.getOutputStream());
                                tempInput = utilities.readServerOutput();

                            }
                            
                        //}

                    }
                    else {
                        for(int i = 0; i < dataEvent; i++){
                            tempInput = utilities.readServerOutput();
                        }
                        clientAction.sendOK(utilities.getOutputStream());
                    }
                    
                    clientAction.sendREDY(utilities.getOutputStream());
                default:
                    break;
            
                }   
        }



    }


    public boolean jobStateCheck(String serverType, String serverID, DataOutputStream outputStream) throws IOException{
        String[] tempInput; 
        
        clientAction.sendCNTJ(serverType, serverID, "2", outputStream);
        tempInput = utilities.readServerOutput();
        int runningCheck = Integer.parseInt(tempInput[0]);

        clientAction.sendCNTJ(serverType, serverID, "1", outputStream);
        tempInput = utilities.readServerOutput();
        int waitingCheck = Integer.parseInt(tempInput[0]);

        return (runningCheck != 0 && waitingCheck == 0 || runningCheck == 0 && waitingCheck != 0 || runningCheck == 0 && waitingCheck == 0);
    }
}    














                    // if(dataEvent == 0 && !serverInput[4].equals("0")){
                        
                    //     clientAction.sendTERM(serverInput[3], serverInput[4], utilities.getOutputStream());
                    //     tempInput = utilities.readServerOutput();
                    // }
                    // else if(dataEvent > 0){
                    //     for(int i = 0; i < dataEvent; i++){
                    //         tempInput = utilities.readServerOutput();
                    //     }
                    //     clientAction.sendOK(utilities.getOutputStream());
                    // }
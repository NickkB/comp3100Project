import java.io.*;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class SuperFast{



    ServerInfo servers;
    Job job;
    JobList jobList;


    public SuperFast() throws IOException, ParserConfigurationException, SAXException{


        Utilities.initConnection();
        jobList = new JobList();
        servers = new ServerInfo(jobList);    
        


        boolean stopLoop = false; 
        while(!stopLoop){
            String[] serverInput = Utilities.readServerOutput();
            switch(serverInput[0]){
                case "JOBN":
                    boolean jobSubmitted = false;
                    job = new Job(serverInput[2], "1", serverInput[1], "-1", serverInput[3], serverInput[4], serverInput[5], serverInput[6]);
                   
                    servers.updateServerStates(job);
                    for(Server elm: servers.tempServersList){
                        if((elm.getCoresInt() >= Integer.parseInt(job.core) && jobStateCheck(elm.serverType, elm.serverID.toString())) ){ 
                            ClientAction.sendSCHD(job.jobID, elm.getType(), elm.getID().toString());
                            jobList.addJob(job, elm);
                            jobSubmitted = true; 
                            break;
                        }
                    }
                    if(!jobSubmitted){
                        Server tempServer = servers.getServerByBestFit(job.core);
                        if(!tempServer.getState().equals("inactive") && tempServer.cores - Integer.parseInt(job.core) >= 0 && jobStateCheck(tempServer.serverType, tempServer.serverID.toString())) {
                            ClientAction.sendSCHD(job.jobID, tempServer.getType(), tempServer.getID().toString());  
                            jobList.addJob(job, tempServer);                             
                            jobSubmitted = true; 
                        } 
                    }
                    if(!jobSubmitted){
                        Server tempServer = servers.getServerByBestFit(job.core);
                        ClientAction.sendSCHD(job.jobID, tempServer.getType(), tempServer.getID().toString());
                        jobList.addJob(job, tempServer);
                        
                    }
                    
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
                    Job tempJob = null;
                    jobList.removeJob(serverInput[1]);
                    ClientAction.sendLSTJ(serverInput[3], serverInput[4]);
                    String[] tempInput = Utilities.readServerOutput(); 
                    ClientAction.sendOK();
                    int dataEvent = Integer.parseInt(tempInput[1]);

                    

                    if(dataEvent == 0){

                            servers.updateServerJobStates();


                            //jobList.sort(Comparator.comparing(Job::getSubmitTime).reversed());   
                            
                            for(Job elm: jobList.getJobList().keySet()){
                                if(elm.jobState.equals("1") && tempJob == null){
                                    ClientAction.sendGETSCapable(elm.core, elm.memory, elm.disk);
                                    tempInput = Utilities.readServerOutput();
                                    ClientAction.sendOK();
                                    dataEvent = Integer.parseInt(tempInput[1]);
                                    for(int i = 0; i < dataEvent; i++){
    
                                        tempInput = Utilities.readServerOutput();   
                                        if(tempInput[0].equals(serverInput[3]) && tempInput[1].equals(serverInput[4]) && tempJob == null){
                                            tempJob = elm;
                                            
                                        }
                                        
                                    }
                                    ClientAction.sendOK();
                                }
                            } 
                            if(tempJob != null){
                                ClientAction.sendMIGJ(tempJob.jobID, jobList.getJobList().get(tempJob).getType(), jobList.getJobList().get(tempJob).getID().toString(), serverInput[3], serverInput[4]);
                                tempInput = Utilities.readServerOutput();
                            }
                            else{
                                ClientAction.sendTERM(serverInput[3], serverInput[4]);
                                tempInput = Utilities.readServerOutput();
                            }
                    }
                    else {
                        for(int i = 0; i < dataEvent; i++){
                            tempInput = Utilities.readServerOutput();
                        }
                        ClientAction.sendOK();
                    }
                    
                    ClientAction.sendREDY();
                default:
                    break;
            
                }   
        }



    }


    public boolean jobStateCheck(String serverType, String serverID) throws IOException{
        String[] tempInput; 
        
        ClientAction.sendCNTJ(serverType, serverID, "2");
        tempInput = Utilities.readServerOutput();
        int runningCheck = Integer.parseInt(tempInput[0]);

        ClientAction.sendCNTJ(serverType, serverID, "1");
        tempInput = Utilities.readServerOutput();
        int waitingCheck = Integer.parseInt(tempInput[0]);

        return (runningCheck != 0 && waitingCheck == 0 || runningCheck == 0 && waitingCheck != 0 || runningCheck == 0 && waitingCheck == 0);
    }

    public void checkForJobMigration(){

    }

    public void updateJobList(){

    }
}    
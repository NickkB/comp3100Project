import java.io.*;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class SuperFast{

    ServerInfo servers;
    Job job;

    public SuperFast() throws IOException, ParserConfigurationException, SAXException{


        Utilities.initConnection();
        servers = new ServerInfo();    
        
        boolean stopLoop = false; 
        while(!stopLoop){
            String[] serverInput = Utilities.readServerOutput();

            switch(serverInput[0]){
                case "JOBN":
                    job = new Job(serverInput[2], "0", serverInput[1], "-1", serverInput[3], serverInput[4], serverInput[5], serverInput[6]);
                    
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
                case "JOBP":
                    servers.reSubmitJob(serverInput[2]);
                    break;
                case "JCPL":
                    servers.completeJob(serverInput[2]);
                    

                    ClientAction.sendLSTJ(serverInput[3], serverInput[4]);
                    String[] tempInput = Utilities.readServerOutput(); 

                    ClientAction.sendOK();
                    int dataEvent = Integer.parseInt(tempInput[1]);

                    if(dataEvent == 0){
                        servers.updateServerStates();
                        servers.getServerByID(serverInput[3], Integer.parseInt(serverInput[4])).balanceServer();
                    }
                    else {
                        for(int i = 0; i < dataEvent; i++){
                            tempInput = Utilities.readServerOutput();
                        }
                        ClientAction.sendOK();
                    }
                    // if(servers.balanceServers() && Integer.parseInt(serverInput[4]) % 2 == 0){
                    //     ClientAction.sendTERM(serverInput[3], serverInput[4]);
                    //     Utilities.readServerOutput();
                    // }

                    // Job tempJob = null;
                    // globalWaitingJobList.removeJob(serverInput[1]);
                    // ClientAction.sendLSTJ(serverInput[3], serverInput[4]);
                    // String[] tempInput = Utilities.readServerOutput(); 
                    // ClientAction.sendOK();
                    // int dataEvent = Integer.parseInt(tempInput[1]);

                    

                    // if(dataEvent == 0){

                    //         servers.updateServerStates();
                         
                    //         for(Job elm: globalWaitingJobList.getJobList().keySet()){
                    //             if(elm.jobState.equals("1") && tempJob == null){
                    //                 ClientAction.sendGETSCapable(elm.core, elm.memory, elm.disk);
                    //                 tempInput = Utilities.readServerOutput();
                    //                 ClientAction.sendOK();
                    //                 dataEvent = Integer.parseInt(tempInput[1]);
                    //                 for(int i = 0; i < dataEvent; i++){
    
                    //                     tempInput = Utilities.readServerOutput();   
                    //                     if(tempInput[0].equals(serverInput[3]) && tempInput[1].equals(serverInput[4]) && tempJob == null){
                    //                         tempJob = elm;
                                            
                    //                     }
                                        
                    //                 }
                    //                 ClientAction.sendOK();
                    //             }
                    //         } 
                    //         if(tempJob != null){
                    //             ClientAction.sendMIGJ(tempJob.jobID, globalWaitingJobList.getJobList().get(tempJob).getType(), globalWaitingJobList.getJobList().get(tempJob).getID().toString(), serverInput[3], serverInput[4]);
                    //             tempInput = Utilities.readServerOutput();
                    //         }
                    //         else{
                    //             ClientAction.sendTERM(serverInput[3], serverInput[4]);
                    //             tempInput = Utilities.readServerOutput();
                    //         }
                    // }
                    // else {
                    //     for(int i = 0; i < dataEvent; i++){
                    //         tempInput = Utilities.readServerOutput();
                    //     }
                    //     ClientAction.sendOK();
                    // }
                    
                    ClientAction.sendREDY();
                default:
                    break;
            
                }   
        }



    }




    public void checkForJobMigration(){

    }

    public void updateJobList(){

    }
}    
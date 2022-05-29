import java.io.IOException;
import java.util.ArrayList;

//Class for server object. 
public class Server {
    String serverType;
    Integer serverID;
    String state;
    Integer curStartTime;
    Integer cores;
    Integer memory;
    Integer disk;
    Integer bootupTime;
    float hourlyRate;
 
    int totalCores;
    int totalMemory;
    int totalDisk;
    
    private JobList globalJobList;

    public Server(String serverType, int serverID, String bootupTime, String hourlyRate, String cores, String memory, String disk, JobList globalJobList){
        this.serverType = serverType;
        this.serverID = serverID;
        this.hourlyRate = Float.parseFloat(hourlyRate);
        this.cores = Integer.parseInt(cores);
        this.totalCores = Integer.parseInt(cores);
        this.memory = Integer.parseInt(memory);
        this.totalMemory = Integer.parseInt(memory);
        this.disk = Integer.parseInt(disk);
        this.totalDisk = Integer.parseInt(disk);
        this.bootupTime = Integer.parseInt(bootupTime);
        this.globalJobList = globalJobList;
    }

    public Server(Server server){
        this.serverType = server.serverType;
        this.serverID = server.serverID;
        this.state = server.state;
        this.cores = server.cores;
        this.memory = server.memory;
        this.disk = server.disk;
    }

    public void updateServer(String state, String curStartTime, String cores, String memory, String disk){
        this.state = state;
        this.curStartTime = Integer.parseInt(curStartTime);
        this.cores = Integer.parseInt(cores);
        this.memory = Integer.parseInt(memory);

    }

    public void submitJob(Job job) throws IOException{
        if(checkForInstantRun(job)){
            job.jobState = "2"; 
            globalJobList.addJob(job, this);
            ClientAction.sendSCHD(job.jobID, this.serverType, this.serverID.toString());
        }
        else{
            job.jobState = "1";
            globalJobList.addJob(job, this);
            ClientAction.sendSCHD(job.jobID, this.serverType, this.serverID.toString());
        }
    }

    public void completeJob(Job job){
        cores += job.getCoreInt();
        memory += job.getMemoryInt();
        disk += job.getDiskInt();
    }

    public void addJob(Job job){
        cores -= job.getCoreInt();
        memory -= job.getMemoryInt();
        disk -= job.getDiskInt();
    }

    public boolean checkForInstantRun(Job job){
        return (cores >= job.getCoreInt() && memory >= job.getMemoryInt() && disk >= job.getDiskInt());
    }

    public boolean canRunJob(Job job){
        return (totalCores >= job.getCoreInt() && totalMemory >= job.getMemoryInt() && totalDisk >= job.getDiskInt());
    }

    public void balanceServer() throws IOException{
        //boolean serversKilled = false;
        // if(this.cores > 0  && !state.equals("inactive") || !state.equals("booting")){
        //     for(Job elm: globalJobList.getJobList().keySet()){
        //         if(globalJobList.getJobServer(elm) != null && checkForInstantRun(elm) && canRunJob(elm) && !globalJobList.getJobServer(elm).getType().equals(this.serverType) && globalJobList.getJobServer(elm).getID() != this.serverID  && elm.jobState.equals("1") && !globalJobList.getJobServer(elm).state.equals("booting")){
        //            // ClientAction.sendKILJ(globalJobList.getJobServer(elm).getType(), globalJobList.getJobServer(elm).getID().toString(), elm.jobID);  
                    Job job = globalJobList.getEarliestJob();
                    if(job != null){
                        ClientAction.sendMIGJ(job.jobID, globalJobList.getJobServer(job).getType(), globalJobList.getJobServer(job).getID().toString(), serverType, serverID.toString());                
                        Utilities.readServerOutput();
                    }

        //             // elm.jobState = "2";
        //             // globalJobList.moveJob(elm, this);
        //             // //serversKilled = true;
        //             // for(Job x: globalJobList.getJobList().keySet()){
              
        //             //     if(serversKilled && globalJobList.getJobServer(x).getType().equals(this.serverType) && globalJobList.getJobServer(x).getID() == this.serverID && x.jobState.equals("1")){
        //             //        // System.out.println(x.jobID + " " + globalJobList.getJobServer(x).serverType + " " + globalJobList.getJobServer(x).serverID + " " + this.serverType + " " + this.serverID);
        //             //         ClientAction.sendKILJ(this.serverType, this.serverID.toString(), x.jobID);
        //             //         Utilities.readServerOutput();
        //             //         serversKilled = true;
        //             //     }
        
        //             // }
        //         }


        //         }
        //     }

    }

    public boolean jobStateCheck() throws IOException{
        String[] tempInput; 
        
        // ClientAction.sendCNTJ(serverType, serverID.toString(), "2");
        // tempInput = Utilities.readServerOutput();
        // int runningCheck = Integer.parseInt(tempInput[0]);

        ClientAction.sendCNTJ(serverType, serverID.toString(), "1");
        tempInput = Utilities.readServerOutput();
        int waitingCheck = Integer.parseInt(tempInput[0]);

        return (waitingCheck == 0);
    }

    public String getType(){
        return serverType;
    }

    public Integer getID(){
        return serverID;
    }

    public String getState(){
        return state;
    }

    public Integer getCurStartTime(){
        return curStartTime;
    }

    public Integer getCoresInt(){
        return cores;
    }

    public Integer getMemory(){
        return memory;
    }

    public Integer getDisk(){
        return disk;
    }

}

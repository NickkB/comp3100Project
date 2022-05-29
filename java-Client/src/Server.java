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
    Integer waitingJobs;
    Integer runningJobs;
    Integer bootupTime;
    float hourlyRate;
    int estWaitTime;
     
    ArrayList<Job> jobList;
     

    public Server(String serverType, int serverID, String bootupTime, String hourlyRate, String cores, String memory, String disk){
        this.serverType = serverType;
        this.serverID = serverID;
        this.hourlyRate = Float.parseFloat(hourlyRate);
        this.cores = Integer.parseInt(cores);
        this.memory = Integer.parseInt(memory);
        this.disk = Integer.parseInt(disk);
        this.bootupTime = Integer.parseInt(bootupTime);
        jobList = new ArrayList<>();
    }

    public Server(Server server){
        this.serverType = server.serverType;
        this.serverID = server.serverID;
        this.state = server.state;
        this.cores = server.cores;
        this.memory = server.memory;
        this.disk = server.disk;
        this.waitingJobs = server.waitingJobs;
        this.runningJobs = server.runningJobs;
    }

    public void updateServer(int sysTime, String state, String curStartTime, String cores, String memory, String disk, String waitingJobs, String runningJobs){
        this.state = state;
        this.curStartTime = Integer.parseInt(curStartTime);
        this.cores = Integer.parseInt(cores);
        this.memory = Integer.parseInt(memory);
        this.disk = Integer.parseInt(disk);
        this.waitingJobs = Integer.parseInt(waitingJobs);
        this.runningJobs = Integer.parseInt(runningJobs);
    }

    public void addJob(Job job){
        job.assignedServerID = this.serverID.toString();
        job.assignedServerType = this.serverType;
        jobList.add(job);

    }

    public void updateJobListState() throws IOException{
        jobList = new ArrayList<>();

        ClientAction.sendLSTJ(this.serverType, this.serverID.toString());
        String[] tempInput = Utilities.readServerOutput(); 

        ClientAction.sendOK();
         
        int dataEvent = Integer.parseInt(tempInput[1]);

        for(int i = 0; i < dataEvent; i++){
            tempInput = Utilities.readServerOutput(); 
            addJob(new Job(tempInput[0], tempInput[1], tempInput[2], tempInput[3], tempInput[4], tempInput[5], tempInput[6], tempInput[7])); 
        }
        if(dataEvent > 0){
            ClientAction.sendOK();
        }
      
    }

    public int getEstimateWaitTime(){
        return estWaitTime;
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

    public Integer getWaitingJobs(){
        return waitingJobs;
    }

    public Integer getRunnningJobs(){
        return runningJobs;
    }
    
    @Override
    public String toString(){
        String serverInfo = serverType + " " + serverID + " " +  state + " " +   curStartTime + " " +   cores + " " +   memory + " " +  disk + " " + waitingJobs + " " + runningJobs;

        return serverInfo;
    }

}

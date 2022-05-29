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
     
    ArrayList<Job> jobList;
    JobList globalJobList;
     

    public Server(String serverType, int serverID, String bootupTime, String hourlyRate, String cores, String memory, String disk, JobList globalJobList){
        this.serverType = serverType;
        this.serverID = serverID;
        this.hourlyRate = Float.parseFloat(hourlyRate);
        this.cores = Integer.parseInt(cores);
        this.memory = Integer.parseInt(memory);
        this.disk = Integer.parseInt(disk);
        this.bootupTime = Integer.parseInt(bootupTime);
        this.globalJobList = globalJobList;
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

    public void updateServer(String state, String curStartTime, String cores, String memory, String disk, String waitingJobs, String runningJobs){
        this.state = state;
        this.curStartTime = Integer.parseInt(curStartTime);
        this.cores = Integer.parseInt(cores);
        this.memory = Integer.parseInt(memory);
        this.disk = Integer.parseInt(disk);
        this.waitingJobs = Integer.parseInt(waitingJobs);
        this.runningJobs = Integer.parseInt(runningJobs);
    }

    public void submitJob(Job job) throws IOException{
        globalJobList.addJob(job, this);
        jobList.add(job);
        ClientAction.sendSCHD(job.jobID, this.serverType, this.serverID.toString());
    }

    public boolean jobStateCheck() throws IOException{
        ClientAction.sendCNTJ(this.serverType, this.serverID.toString(), "1");
        String[] tempInput = Utilities.readServerOutput();
        return (Integer.parseInt(tempInput[0]) == 0);
    }

    public void updateJobListState() throws IOException{
        ClientAction.sendLSTJ(this.serverType, this.serverID.toString());
        String[] tempInput = Utilities.readServerOutput(); 
        ClientAction.sendOK();
        int dataEvent = Integer.parseInt(tempInput[1]);
        for(int i = 0; i < dataEvent; i++){
            tempInput = Utilities.readServerOutput(); 
            Job tempJob = new Job(tempInput[0], tempInput[1], tempInput[2], tempInput[3], tempInput[4], tempInput[5], tempInput[6], tempInput[7]);
            tempJob.assignServer(this);
            globalJobList.updateJob(tempJob);
        }
        if(dataEvent > 0){
            ClientAction.sendOK();
        }
    }

    public boolean serversNotEqual(Server server){
        return (!this.serverType.equals(server.getType()) || !this.serverID.equals(server.getID()));
    }

    public boolean hasJobs() throws IOException{
        updateJobListState();
        return jobList.isEmpty();
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
    
}

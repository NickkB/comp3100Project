import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
    
    CoreCounter coreCounter;
    
    JobList waitingJobs;
    JobList runningJobs; 

    public Server(String serverType, int serverID, String bootupTime, String hourlyRate, String cores, String memory, String disk){
        this.serverType = serverType;
        this.serverID = serverID;
        this.hourlyRate = Float.parseFloat(hourlyRate);
        this.cores = Integer.parseInt(cores);
        this.totalCores = Integer.parseInt(cores);
        this.memory = Integer.parseInt(memory);
        this.disk = Integer.parseInt(disk);
        this.bootupTime = Integer.parseInt(bootupTime);
        waitingJobs = new JobList();
        runningJobs = new JobList();
        coreCounter = new CoreCounter(totalCores);
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

    public void addJob(Job job){
        coreCounter.submitJob(job);
    }

    public void updateJobListState() throws IOException{
        
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

    public int getAvailableCores(){
        return this.coreCounter.getAvailableCores();
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


    
    class CoreCounter{
        
        ArrayList<Cores> jobBucket; 
        int totalCores; 

        public CoreCounter(int totalCores){
            this.totalCores = totalCores;
            jobBucket = new ArrayList<>();
            jobBucket.add(new Cores(totalCores));
        }

        public void submitJob(Job job){
            for(int i = 0; i < jobBucket.size(); i++){
                if(jobBucket.get(i).checkAvailableCores() - job.getCoreInt() >= 0){
                    jobBucket.get(i).assignCores(job);
                }
                else if(i == jobBucket.size() - 1){
                    jobBucket.add(new Cores(totalCores));
                }
            }
        }

        public void jobCompleted(Job job){
            jobBucket.get(0).releaseCores(job);
            if(jobBucket.get(0).checkAvailableCores() == totalCores){
                jobBucket.remove(0);
            }
        }

        public int getAvailableCores(){
            return this.jobBucket.get(0).checkAvailableCores();
        }
    }

    class Cores{
        int cores; 

        public Cores(int coreCount){
            cores = coreCount;
        }

        public int checkAvailableCores(){
            return cores;
        }

        public void assignCores(Job job){
            this.cores -= job.getCoreInt();
        }

        public void releaseCores(Job job){
            this.cores += job.getCoreInt();
        }
    }
}

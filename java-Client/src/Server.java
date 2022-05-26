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
    
    CoreCounter coreCounter;
    
    JobList waitingJobs;
    JobList runningJobs; 

    private JobList globalJobList;

    public Server(String serverType, int serverID, String bootupTime, String hourlyRate, String cores, String memory, String disk, JobList globalJobList){
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
            Job tempJob = globalJobList.findJob(tempInput[0]);
            tempJob.jobState = tempInput[1];
            if(tempJob.jobState.equals("1")){
                addJob(tempJob);
                waitingJobs.addJob(tempJob, this);
            }
            else{
                runningJobs.addJob(tempJob, this);
            }
             
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

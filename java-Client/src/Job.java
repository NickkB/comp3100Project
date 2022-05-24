public class Job {
    public String submitTime; 
    public String jobID;
    public String estRuntime;
    public String core;
    public String memory; 
    public String disk; 
    public String jobState; 
    public String startTime; 

    public Job(String jobID, String jobState, String submitTime, String startTime, String estRuntime, String core, String memory, String disk){
        this.submitTime = submitTime;
        this.jobID = jobID;
        this.estRuntime = estRuntime;
        this.core = core;
        this.memory = memory; 
        this.disk = disk; 
        this.jobState = jobState; 
        this.startTime = startTime; 
    }

    public int getEstRunTime(){
        return Integer.parseInt(estRuntime);
    }
}

public class Job {
    public String submitTime; 
    public String jobID;
    public String estRuntime;
    public String core;
    public String memory; 
    public String disk; 
    public String jobState; 
    public String startTime; 
    public Server assignedServer;

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

    public Job(Job job){
        this.submitTime = job.submitTime;
        this.jobID = job.jobID;
        this.estRuntime = job.estRuntime;
        this.core = job.core;
        this.memory = job.memory; 
        this.disk = job.disk; 
        this.jobState = job.jobState; 
        this.startTime = job.startTime; 
    }

    public void updateJob(String jobState, String submitTime, String startTime, String estRuntime, String core, String memory, String disk){
        this.submitTime = submitTime;
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

    public int getSubmitTime(){
        return Integer.parseInt(submitTime);
    }

    public Server getAssignedServer(){  
        return assignedServer;
    }

    public boolean isWaiting(){
        return (jobState.equals("1"));
    }

    public void assignServer(Server server){
        this.assignedServer = server;
    }
}

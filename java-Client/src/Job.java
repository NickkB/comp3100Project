public class Job {
    public String submitTime; 
    public String jobID;
    public String estRuntime;
    public String core;
    public String memory; 
    public String disk; 
    public String jobState; 
    public String startTime; 
    public String assignedServerType;
    public String assignedServerID;

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

    public int getEstRunTime(){
        return Integer.parseInt(estRuntime);
    }

    public int getSubmitTime(){
        return Integer.parseInt(submitTime);
    }

    public String[] getAssignedServer(){
        String[] temp = {assignedServerType, assignedServerID};
        return temp;
    }

    public void assignServer(String assignedServerType, String assignedServerID){
        this.assignedServerType = assignedServerType;
        this.assignedServerID = assignedServerID;
    }
}

package ServerEvents;
public class JOBNEvent {
    
    String submitTime; 
    String jobID;
    String estRuntime;
    String core;
    String memory; 
    String disk; 

    public JOBNEvent(String submitTime, String jobID, String estRuntime, String core, String memory, String disk){
        this.submitTime = submitTime;
        this.jobID = jobID;
        this.estRuntime = estRuntime;
        this.core = core;
        this.memory = memory; 
        this.disk = disk; 
    }

}

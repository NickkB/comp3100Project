import java.util.LinkedHashMap;

public class JobList {


    LinkedHashMap<Job, Server> jobList;
    

    public JobList(){
        jobList = new LinkedHashMap<Job, Server>();
    }

    public void addJob(Job job, Server assignedServer){
        jobList.put(job, assignedServer);
    }

    public void removeJob(String jobID){    
        jobList.remove(findJob(jobID));
    }

    public void removeJob(Job job){
        jobList.remove(job);
    }
    
    public LinkedHashMap<Job, Server> getJobList(){
        return jobList;
    }

    public Job findJob(String jobID){
        for(Job elm: jobList.keySet()){
            if(elm.jobID.equals(jobID)){
                return elm;
            }
        }
        return null;
    }

    public boolean isEmpty(){
        return jobList.isEmpty();
    }

}


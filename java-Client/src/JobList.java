import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class JobList {

    ArrayList<Job> jobList;
    
    public JobList(){
        jobList = new ArrayList<Job>();
    }

    public ArrayList<Job> getJobList(){
        return jobList;
    }

    public void addJob(Job job, Server assignedServer){
        job.assignServer(assignedServer);
        jobList.add(job);
    }

    public void removeJob(String jobID){  
        Job tempJob = getJob(jobID);
        jobList.remove(tempJob);
    }

    public void removeJob(Job job){
        jobList.remove(job);
    }
    
    public Job getJob(Job job){
        return jobList.get(jobList.indexOf(job));
    }

    public Job getJob(String jobID){
        for(Job elm: jobList){
            if(elm.jobID.equals(jobID)){
                return elm;
            }
        }
        return null;
    }

    public ArrayList<Job> getWaitingJobList(){
        ArrayList<Job> waitingJobs = new ArrayList<>();
        for(Job elm: jobList){
            if(elm.isWaiting()){
                waitingJobs.add(elm);
            }
        }
        waitingJobs.sort(Comparator.comparing(Job::getSubmitTime).reversed());
        return waitingJobs;
    }

    public void updateJob(Job job){
        jobList.add(job);
    }

    public void validateJobLists(ArrayList<Server> serverList) throws IOException{
        jobList.clear();
        for(Server elm: serverList){
            elm.updateJobListState();
        }
    }
}


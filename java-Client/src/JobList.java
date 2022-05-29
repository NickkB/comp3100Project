import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JobList {


    LinkedHashMap<Job, Server> jobList;
    

    
    public JobList(){
        jobList = new LinkedHashMap<Job, Server>();
    }

    public LinkedHashMap<Job, Server> getJobList(){
        return jobList;
    }

    public Server getJobServer(Job job){
        return jobList.get(job);
    }

    public void addJob(Job job, Server assignedServer){
        jobList.put(job, assignedServer);
    }

    public Job getEarliestJob(){
        Job earlistJob = null;
        for (Job elm : jobList.keySet()) {
            if(earlistJob == null && elm.jobState.equals("1")){
                earlistJob = elm;
            }
            else if(earlistJob == null && elm.jobState.equals("2")){
                continue;
            }   
            else if (earlistJob != null && Integer.parseInt(elm.submitTime) < Integer.parseInt(earlistJob.submitTime)){
                earlistJob = elm;
            }
        }
        return earlistJob;
    }
    public void moveJob(Job job, Server server){
        jobList.replace(job, server);
    }

    public void removeJob(String jobID){  
        Job tempJob = findJob(jobID);
        jobList.remove(tempJob);
          
    }

    public boolean checkWaitingJobs(){
        for (Job elm : jobList.keySet()) {
            if(elm.jobState.equals("1")){
                return true;
            }
        }
        return false;
    }
    public void removeJob(Job job){
        jobList.remove(job);
    }
    

    public Job findJob(Job job){
        for(Job elm: jobList.keySet()){
            if(elm.equals(job)){
                return elm;
            }
        }
        return null;
    }

    public Job findJob(String jobID){
        for(Job elm: jobList.keySet()){
            if(elm.jobID.equals(jobID)){
                return elm;
            }
        }
        return null;
    }

    public void validateJobLists(ArrayList<Server> serverList) throws IOException{

        for(Server elm: serverList){
            ClientAction.sendLSTJ(elm.serverType, elm.serverID.toString());
            String[] tempInput = Utilities.readServerOutput(); 
            ClientAction.sendOK();
            int dataEvent = Integer.parseInt(tempInput[1]);
            for(int i = 0; i < dataEvent; i++){
                tempInput = Utilities.readServerOutput();
                Job tempJob = findJob(tempInput[0]);
                tempJob.jobState = tempInput[1];
            }
            if(dataEvent > 0){
                ClientAction.sendOK();
            }
        }
        

    }

}


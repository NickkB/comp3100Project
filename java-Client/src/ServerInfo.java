import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ServerInfo {

    ArrayList<Server> servers;


    public ServerInfo(){
        servers = new ArrayList<>();
    }

    public void addServer(String serverType, String serverID, String state, String curStartTime, String cores, String memory, String disk, String waitingJobs, String runningJobs){
        servers.add(new Server(serverType, serverID, state, curStartTime, cores, memory, disk, waitingJobs, runningJobs));
    }

    public void sortListByCoreCount(){

        servers.sort(Comparator.comparing(Server::getCoresString).reversed());

    }

    public void printArray(){
        System.out.println(Arrays.deepToString(servers.toArray()));
    }



    static class Server {
        private String serverType;
        private Integer serverID;
        private String state;
        private Integer curStartTime;
        private Integer cores;
        private Integer memory;
        private Integer disk;
        private Integer waitingJobs;
        private Integer runningJobs;
    
        public Server(String serverType, String serverID, String state, String curStartTime, String cores, String memory, String disk, String waitingJobs, String runningJobs){
            this.serverType = serverType;
            this.serverID = Integer.parseInt(serverID);
            this.state = state;
            this.curStartTime = Integer.parseInt(curStartTime);
            this.cores = Integer.parseInt(cores);
            this.memory = Integer.parseInt(memory);
            this.disk = Integer.parseInt(disk);
            this.waitingJobs = Integer.parseInt(waitingJobs);;
            this.runningJobs = Integer.parseInt(runningJobs);;
        }
    
        public String getype(){
            return serverType;
        }
    
        public String getID(){
            return serverID.toString();
        }
    
        public String getState(){
            return state;
        }
    
        public String getCurStartTime(){
            return curStartTime.toString();
        }
    
        public int getCoresInt(){
            return cores;
        }

        public String getCoresString(){
            return cores.toString();
        }
    
        public String getMemory(){
            return memory.toString();
        }
    
        public String getDisk(){
            return disk.toString();
        }

        public String getWaitingJobs(){
            return waitingJobs.toString();
        }

        public String getRunnningJobs(){
            return runningJobs.toString();
        }
        
        @Override
        public String toString(){
            String serverInfo = serverType + " " + serverID + " " +  state + " " +   curStartTime + " " +   cores + " " +   memory + " " +  disk + " " + waitingJobs + " " + runningJobs;
    
            return serverInfo;
        }

    }
}
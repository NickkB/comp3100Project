import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ServerInfo {

    ArrayList<Server> servers;
    Server lastUsedServer;

    public ServerInfo(){
        servers = new ArrayList<>();
    }

    public void importServers(DATAEvent dataEvent, ClientAction clientAction, Utilities utilities) throws IOException{
        resetServerList();
        clientAction.sendOK(utilities.getOutputStream());
        for(int i = 0; i < dataEvent.getCount(); i++){
            String temp = (String)utilities.getInputReader().readLine();
            String[] tempInput = temp.split("\\s+");
            addServer(tempInput[0], tempInput[1], tempInput[2], tempInput[3], tempInput[4], tempInput[5], tempInput[6], tempInput[7], tempInput[8]);    
        }
        clientAction.sendOK(utilities.getOutputStream());
    }

    public void addServer(String serverType, String serverID, String state, String curStartTime, String cores, String memory, String disk, String waitingJobs, String runningJobs){
        servers.add(new Server(serverType, serverID, state, curStartTime, cores, memory, disk, waitingJobs, runningJobs));
    }

    public void resetServerList(){
        servers = new ArrayList<>(); 
    }

    public void sortListByCoreCount(){
        servers.sort(Comparator.comparing(Server::getCoresInt).reversed());
    }

    public void setLastUsedServer(Server server){
        lastUsedServer = new Server(server); 
    }

    public Server getLastUsedServer(){
        return lastUsedServer;
    }

    public Server getServer(int index){
        return servers.get(index);
    }

    public Server getServerByID(String serverType, int ID){
        for(Server elm: servers){
            if(elm.getType().equals(serverType) && elm.getID() == ID){
                return elm;
            }
        }
        return null;
    }

    public void printArray(){
        for(Server ent: servers){
            System.out.println(ent.toString());
        }
    }

    static class Server {
        String serverType;
        Integer serverID;
        String state;
        Integer curStartTime;
        Integer cores;
        Integer memory;
        Integer disk;
        Integer waitingJobs;
        Integer runningJobs;
    
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

        public Server(Server server){
            this.serverType = server.serverType;
            this.serverID = server.serverID;
            this.state = server.state;
            this.cores = server.cores;
            this.memory = server.memory;
            this.disk = server.disk;
            this.waitingJobs = server.waitingJobs;
            this.runningJobs = server.runningJobs;
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

        public Integer getWaitingJobs(){
            return waitingJobs;
        }

        public Integer getRunnningJobs(){
            return runningJobs;
        }
        
        @Override
        public String toString(){
            String serverInfo = serverType + " " + serverID + " " +  state + " " +   curStartTime + " " +   cores + " " +   memory + " " +  disk + " " + waitingJobs + " " + runningJobs;
    
            return serverInfo;
        }

    }
}
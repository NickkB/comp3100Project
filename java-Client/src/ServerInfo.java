import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ServerEvents.DATAEvent;

public class ServerInfo {

    ArrayList<Server> servers;

    public ServerInfo() throws ParserConfigurationException, SAXException, IOException{
        servers = new ArrayList<>();
        initServers();
    }

    public void updateServerStates(DATAEvent dataEvent, ClientAction clientAction, Utilities utilities) throws IOException{
        clientAction.sendOK(utilities.getOutputStream());
        for(int i = 0; i < dataEvent.getCount(); i++){
            String temp = (String)utilities.getInputReader().readLine();
            String[] tempInput = temp.split("\\s+");   
            getServerByID(tempInput[0], Integer.parseInt(tempInput[1])).updateServer(tempInput[2], tempInput[3], tempInput[4], tempInput[5], tempInput[6], tempInput[7], tempInput[8]);
        }
        clientAction.sendOK(utilities.getOutputStream());
    }

    public void sortListByCoreCount(){
        servers.sort(Comparator.comparing(Server::getCoresInt).reversed());
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

    //Gets inital server information from ds-system.xml 
    public void initServers() throws ParserConfigurationException, SAXException, IOException{

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("ds-system.xml"));
        
        NodeList nodeList = document.getElementsByTagName("server");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                String type = elem.getAttributes().getNamedItem("type").getNodeValue();
                String limit = elem.getAttributes().getNamedItem("limit").getNodeValue();
                String bootupTime = elem.getAttributes().getNamedItem("bootupTime").getNodeValue();
                String hourlyRate = elem.getAttributes().getNamedItem("hourlyRate").getNodeValue();
                String cores = elem.getAttributes().getNamedItem("cores").getNodeValue();
                String memory = elem.getAttributes().getNamedItem("memory").getNodeValue();
                String disk = elem.getAttributes().getNamedItem("disk").getNodeValue();
                
                Integer limitInt = Integer.parseInt(limit);

                for(int j = 0; j < limitInt; j++){
                    servers.add(new Server(type, j, bootupTime, hourlyRate, cores, memory, disk));
                }
                
            }
        }
    }

    //Class for server object. 
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
        Integer bootupTime;
        float hourlyRate;

        public Server(String serverType, int serverID, String bootupTime, String hourlyRate, String cores, String memory, String disk){
            this.serverType = serverType;
            this.serverID = serverID;
            this.hourlyRate = Float.parseFloat(hourlyRate);
            this.cores = Integer.parseInt(cores);
            this.memory = Integer.parseInt(memory);
            this.disk = Integer.parseInt(disk);
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

        public void updateServer(String state, String curStartTime, String cores, String memory, String disk, String waitingJobs, String runningJobs){
            this.state = state;
            this.curStartTime = Integer.parseInt(curStartTime);
            this.cores = Integer.parseInt(cores);
            this.memory = Integer.parseInt(memory);
            this.disk = Integer.parseInt(disk);
            this.waitingJobs = Integer.parseInt(waitingJobs);
            this.runningJobs = Integer.parseInt(runningJobs);
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
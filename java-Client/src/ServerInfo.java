import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ServerInfo {

    ArrayList<Server> servers;
    JobList globalJobList;

    public ServerInfo() throws ParserConfigurationException, SAXException, IOException{    
        globalJobList = new JobList();
        servers = new ArrayList<>();
        initServers(globalJobList);
    }

    public void submitJob(Job job) throws IOException{
        balanceServers();
        updateServerStates();
        boolean jobSubmitted = false;
        ArrayList<Server> tempServersList = getServerCapable(job);
        for(Server elm: tempServersList){
            if(elm.checkForInstantRun(job) && elm.jobStateCheck()){ 
                jobSubmitted = true; 
                elm.submitJob(job);
                break;
            }
        }
        if(!jobSubmitted){
            Server bestServer = tempServersList.get(0); 
            for(int i = 1; i < tempServersList.size(); i++){
                Server tempServer = tempServersList.get(i);
                if(bestServer.getCoresInt() - job.getCoreInt() > tempServer.getCoresInt() - job.getCoreInt()){
                    bestServer = tempServer;
                }
            }
            if(bestServer.checkForInstantRun(job) && bestServer.jobStateCheck()) {
                bestServer.submitJob(job);           
                jobSubmitted = true; 
            }
            else{
                bestServer.submitJob(job);
            }
            
        }
    }

    public void updateServerStates() throws IOException{    
        ClientAction.sendGETSAll(); 
        String[] dataEvent = Utilities.readServerOutput();      
        ClientAction.sendOK();   
        for(int i = 0; i < Integer.parseInt(dataEvent[1]); i++){
            String[] tempInput = Utilities.readServerOutput();   

            getServerByID(tempInput[0], Integer.parseInt(tempInput[1])).updateServer(tempInput[2], tempInput[3], tempInput[4], tempInput[5], tempInput[6]);        
        }
        ClientAction.sendOK();
        globalJobList.validateJobLists(servers);
    }

    public boolean balanceServers() throws IOException{
        updateServerStates();
        if(globalJobList.checkWaitingJobs()){
            for(Server elm: servers){
                 elm.balanceServer();
             }
             return false;
        }
        return true;
    }

    public void reSubmitJob(String jobID) throws IOException{
        Job job = globalJobList.findJob(jobID);
        Server server = globalJobList.getJobServer(job);
        ClientAction.sendSCHD(job.jobID, server.getType(), server.getID().toString());

    }

    public void completeJob(String job){
        globalJobList.removeJob(job);
    }

    public ArrayList<Server> getServerCapable(Job job) throws IOException{
        ArrayList<Server> tempServerList = new ArrayList<>();
        ClientAction.sendGETSCapable(job.core, job.memory, job.disk); 
        String[] dataEvent = Utilities.readServerOutput();      
        ClientAction.sendOK();   
        for(int i = 0; i < Integer.parseInt(dataEvent[1]); i++){
            String[] tempInput = Utilities.readServerOutput();   
            tempServerList.add(getServerByID(tempInput[0], Integer.parseInt(tempInput[1])));          
        }
        ClientAction.sendOK();

        return tempServerList;
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

    //Gets inital server information from ds-system.xml 
    public void initServers(JobList globalJobList) throws ParserConfigurationException, SAXException, IOException{

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
                    servers.add(new Server(type, j, bootupTime, hourlyRate, cores, memory, disk, globalJobList));
                }
                
            }
        }
    }



}
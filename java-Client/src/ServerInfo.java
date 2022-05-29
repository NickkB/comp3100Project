import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ServerInfo {

    ArrayList<Server> serversAll;
    ArrayList<Server> serversCapable;
    JobList globalJobList;
    

    public ServerInfo(JobList globalJobList) throws ParserConfigurationException, SAXException, IOException{
        serversAll = new ArrayList<>();
        this.globalJobList = globalJobList;
        initserversAll(globalJobList);
    }

    public void submitJob(Job job) throws IOException, InterruptedException{
        updateServerStates(job);
        boolean jobSubmitted = false;
        for(Server elm: serversCapable){
            if(elm.getCoresInt() >= Integer.parseInt(job.core) && elm.jobStateCheck()){ 
                elm.submitJob(job);
                jobSubmitted = true; 
                break;
            }
        }
        if(!jobSubmitted){
            Server bestServer = getServerByBestFit(job.core);
            if(bestServer.cores - Integer.parseInt(job.core) >= 0 && bestServer.jobStateCheck()) {
                bestServer.submitJob(job);
                jobSubmitted = true; 
            } 
        }
        if(!jobSubmitted){
            Server bestServer = getServerByBestFit(job.core);
            bestServer.submitJob(job);
        }
    }

    public void migrateJob(Job job, Server srcServer, Server tgtServer) throws IOException{
        ClientAction.sendMIGJ(job.jobID, srcServer.getType(), srcServer.getID().toString(), tgtServer.getType(), tgtServer.getID().toString());
        job.assignServer(tgtServer);
        Utilities.readServerOutput();
    }

    public void updateServerStates(Job job) throws IOException, InterruptedException{
        ClientAction.sendGETSCapable(job.core, job.memory, job.disk);
        String[] dataEvent = Utilities.readServerOutput();   
        ClientAction.sendOK();
        serversCapable = new ArrayList<>();
        for(int i = 0; i < Integer.parseInt(dataEvent[1]); i++){
            String[] tempInput = Utilities.readServerOutput();   
            getServerByID(tempInput[0], tempInput[1]).updateServer(tempInput[2], tempInput[3], tempInput[4], tempInput[5], tempInput[6], tempInput[7], tempInput[8]);
            serversCapable.add(getServerByID(tempInput[0], tempInput[1])); 
        }
        ClientAction.sendOK();  
    }

    public Server getServerByBestFit(String cores){
        int core = Integer.parseInt(cores);
        Server bestServer = serversCapable.get(0); 
        for(int i = 1; i < serversCapable.size(); i++){
            Server tempServer = serversCapable.get(i);
            if(bestServer.getCoresInt() - core > tempServer.getCoresInt() - core){
                bestServer = tempServer;
            }
        }
        return bestServer;
    }

    public Server getServer(int index){
        return serversAll.get(index);
    }

    public Server getServerByID(String serverType, String id){
        int ID = Integer.parseInt(id);
        for(Server elm: serversAll){
            if(elm.getType().equals(serverType) && elm.getID() == ID){
                return elm;
            }
        }
        return null;
    }

    public ArrayList<Server> getAllServers(){
        return serversAll;
    }

    public boolean serverIsCapable(Server server){
        return serversCapable.contains(server);
    }

    //Gets inital server information from ds-system.xml 
    public void initserversAll(JobList globalJobList) throws ParserConfigurationException, SAXException, IOException{

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
                    serversAll.add(new Server(type, j, bootupTime, hourlyRate, cores, memory, disk, globalJobList));
                }
                
            }
        }
    }



}
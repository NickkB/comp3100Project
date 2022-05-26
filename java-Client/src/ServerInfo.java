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

public class ServerInfo {

    ArrayList<Server> servers;
    ArrayList<Server> tempServersList;

    public ServerInfo(JobList globalJobList) throws ParserConfigurationException, SAXException, IOException{
        
        servers = new ArrayList<>();
        initServers(globalJobList);
    }

    public void updateServerStates(Job job) throws IOException{
        tempServersList = new ArrayList<>();
        
        ClientAction.sendGETSCapable(job.core, job.memory, job.disk);
        
        String[] dataEvent = Utilities.readServerOutput();

        
        
        ClientAction.sendOK();
        
        for(int i = 0; i < Integer.parseInt(dataEvent[1]); i++){
            String[] tempInput = Utilities.readServerOutput();   
            getServerByID(tempInput[0], Integer.parseInt(tempInput[1])).updateServer(tempInput[2], tempInput[3], tempInput[4], tempInput[5], tempInput[6]);
            tempServersList.add(getServerByID(tempInput[0], Integer.parseInt(tempInput[1])));
            
        }
        ClientAction.sendOK();
    }

    public void updateServerJobStates() throws IOException{
        for(int i = 0; i < servers.size(); i++){
            servers.get(i).updateJobListState();
        }
    }

    public void sortListByCoreCount(){
        tempServersList.sort(Comparator.comparing(Server::getCoresInt));
    }

    public Server getServerByBestFit(String cores){
        int core = Integer.parseInt(cores);
        Server bestServer = tempServersList.get(0); 
        for(int i = 1; i < tempServersList.size(); i++){
            Server tempServer = tempServersList.get(i);
            if(bestServer.getCoresInt() - core > tempServer.getCoresInt() - core){
                bestServer = tempServer;
            }
        }
        return bestServer;
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
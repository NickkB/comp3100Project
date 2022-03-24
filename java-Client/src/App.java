import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class App {

    

    public static void main(String[] args) throws Exception {
        
        try{


            SystemInfo sysInfo = new SystemInfo();






        } catch(Exception e){System.out.println(e);}
    }


}

class ClientAction {

    public void sendHELO(DataOutputStream outputStream) throws IOException{
        outputStream.write(("HELO\n").getBytes());
    }

    public void sendOK(DataOutputStream outputStream) throws IOException{
        outputStream.write(("OK\n").getBytes());
    }

    public void sendQUIT(DataOutputStream outputStream) throws IOException{
        outputStream.write(("QUIT\n").getBytes());
    }

    public void sendAUTH(DataOutputStream outputStream) throws IOException{
        String username = System.getProperty("user.name");
        outputStream.write(("AUTH " + username + "\n").getBytes());
    }

    public void sendREDY(DataOutputStream outputStream) throws IOException{
        outputStream.write(("REDY\n").getBytes());
    }

    public void sendGETSAll(DataOutputStream outputStream) throws IOException{
        outputStream.write(("GETS All\n").getBytes());
    }

}

class Utilities{

    private Socket socket;
    private DataOutputStream outputStream;
    private BufferedReader inputReader;
    private ClientAction clientAction; 

    public void initConnection() throws UnknownHostException, IOException{
        clientAction = new ClientAction();
        socket = new Socket("localhost", 50000);
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputReader = new BufferedReader(new InputStreamReader (socket.getInputStream()));
        handShake();
    }

    public void handShake() throws IOException{
        clientAction.sendHELO(outputStream);
        if(readServerOutput() != "OK"){
            clientAction.sendQUIT(outputStream);
        }  
        clientAction.sendAUTH(outputStream); 
        if(readServerOutput() != "OK"){
            clientAction.sendQUIT(outputStream);
        } 
        clientAction.sendREDY(outputStream);
    }

    public String readServerOutput() throws IOException{
        String str = (String)inputReader.readLine();
        System.out.println(str);
        return str;
    }

    public void closeConnection() throws IOException{
        inputReader.close();
        socket.close();
    }

    public DataOutputStream getOutputStream(){
        return outputStream;
    }

}

class LargestRoundRobbin{

    Utilities utilities;
    ClientAction clientAction;
    SystemInfo sysInfo; 

    public LargestRoundRobbin() throws IOException, ParserConfigurationException, SAXException{
        utilities = new Utilities();
        clientAction = new ClientAction();
        sysInfo = new SystemInfo();
        
        
        clientAction.sendGETSAll(utilities.getOutputStream());

        utilities.readServerOutput();
        
        clientAction.sendOK(utilities.getOutputStream());

        for(int i = 0; i < 5; i++){
            utilities.readServerOutput();
        }
    }

    public static void scheduleLogic(String input){

        if(input.matches("JOBN")){

        }

    }

}

class SystemInfo {

    ArrayList<ServerInfo> serverInfo;

    public SystemInfo() throws ParserConfigurationException, SAXException, IOException{

        serverInfo = new ArrayList<>();

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
     
                serverInfo.add(new ServerInfo(type, limit, bootupTime, hourlyRate, cores, memory, disk));
            }
        }
        for (ServerInfo servers: serverInfo)
            System.out.println(servers.toString());
    }

    public ArrayList<ServerInfo> getSystemInfo(){
        return serverInfo;
    }

}

class ServerInfo {

    private String type;
    private String limit;
    private String bootupTime;
    private String hourlyRate;
    private String cores;
    private String memory;
    private String disk;

    public ServerInfo (String type, String limit, String bootupTime, String hourlyRate, String cores, String memory, String disk){
        this.type = type;
        this.limit = limit;
        this.bootupTime = bootupTime;
        this.hourlyRate = hourlyRate;
        this.cores = cores;
        this.memory = memory;
        this.disk = disk;
    }

    public String getType(){
        return type;
    }

    public String getLimit(){
        return limit;
    }

    public String getBootupTime(){
        return bootupTime;
    }

    public String getHourlyRate(){
        return hourlyRate;
    }

    public String getCores(){
        return cores;
    }

    public String getMemory(){
        return memory;
    }

    public String getDisk(){
        return disk;
    }

    @Override
    public String toString(){
        String serverInfo = type + " " + limit + " " +  bootupTime + " " +   hourlyRate + " " +   cores + " " +   memory + " " +  disk;

        return serverInfo;
    }


}
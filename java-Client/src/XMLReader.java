import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
//import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {

    ArrayList<ServerInfo> serverInfo;

    public XMLReader() throws ParserConfigurationException, SAXException, IOException{

        serverInfo = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("ds-system.xml"));
        
        NodeList nodeList = document.getElementsByTagName("server");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                //Element elem = (Element) node;

                // String type = elem.getAttributes().getNamedItem("type").getNodeValue();
                // String limit = elem.getAttributes().getNamedItem("limit").getNodeValue();
                // String bootupTime = elem.getAttributes().getNamedItem("bootupTime").getNodeValue();
                // String hourlyRate = elem.getAttributes().getNamedItem("hourlyRate").getNodeValue();
                // String cores = elem.getAttributes().getNamedItem("cores").getNodeValue();
                // String memory = elem.getAttributes().getNamedItem("memory").getNodeValue();
                // String disk = elem.getAttributes().getNamedItem("disk").getNodeValue();
     
                //serverInfo.add(new ServerInfo(type, limit, bootupTime, hourlyRate, cores, memory, disk));
            }
        }
        for (ServerInfo servers: serverInfo)
            System.out.println(servers.toString());
    }

    public ArrayList<ServerInfo> getSystemInfo(){
        return serverInfo;
    }

}

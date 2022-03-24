import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class LargestRoundRobbin{

    Utilities utilities;
    ClientAction clientAction;
    //SystemInfo sysInfo; 

    public LargestRoundRobbin() throws IOException, ParserConfigurationException, SAXException{
        utilities = new Utilities();
        clientAction = new ClientAction();
        //sysInfo = new SystemInfo();
        
        
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

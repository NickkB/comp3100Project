import java.io.*;
import java.net.*;

public class Utilities{

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


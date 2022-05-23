import java.io.*;
import java.net.*;

public class Utilities{

    private Socket socket;
    private DataOutputStream outputStream;
    private BufferedReader inputReader;
    private ClientAction clientAction; 

    //Initiates a connection with ds-server 
    //Uses port 50000
    public void initConnection() throws UnknownHostException, IOException{
        clientAction = new ClientAction();
        socket = new Socket("localhost", 50000);
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputReader = new BufferedReader(new InputStreamReader (socket.getInputStream()));
        handShake();
    }

    //Executes the initial hand shake with ds-server
    private void handShake() throws IOException{
        clientAction.sendHELO(outputStream);
        readServerOutput();
        clientAction.sendAUTH(outputStream); 
        readServerOutput();
        clientAction.sendREDY(outputStream);
    }

    //reads the output from the server and splits the string by whitespace 
    public String[] readServerOutput() throws IOException{    
        String input = (String)inputReader.readLine();
        String[] serverInput = splitInput(input);       
        return serverInput;
    }

    //splits the string by white space
    private String[] splitInput(String input) throws IOException{
        String[] serverInputArr = input.split("\\s+");
        return serverInputArr;
    }

    // Closes the connection with ds-server
    public void closeConnection() throws IOException{
        inputReader.close();
        socket.close();
    }

    public DataOutputStream getOutputStream(){
        return outputStream;
    }

    public BufferedReader getInputReader(){
        return inputReader;
    }

}


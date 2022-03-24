import java.io.*;
import java.net.*;
import java.util.Arrays;

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

    private void handShake() throws IOException{
        clientAction.sendHELO(outputStream);
        readServerOutput();
        clientAction.sendAUTH(outputStream); 
        readServerOutput();
        clientAction.sendREDY(outputStream);
    }

    public ServerOutObj readServerOutput() throws IOException{
        
        String input = (String)inputReader.readLine();
        
        String[] serverInput = splitInput(input);

        System.out.println(Arrays.deepToString(serverInput));

        switch(serverInput[0]){
            case "JOBN":
                return new JOBNEvent(serverInput[1], serverInput[2], serverInput[3], serverInput[4], serverInput[5], serverInput[6]);
            case "DATA":
                return new DATAEvent(serverInput[1], serverInput[2]);
            default:
                //readStringOutput(serverInput[0]);
                break;
        }

        return null;
    }

    // private String readStringOutput(String input){
    //     return input;
    // }

    private String[] splitInput(String input) throws IOException{
        String[] serverInputArr = input.split("\\s+");
        return serverInputArr;
    }

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


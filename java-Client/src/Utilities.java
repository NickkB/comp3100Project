import java.io.*;
import java.net.*;

final class Utilities{

    static Socket socket;
    static DataOutputStream outputStream;
    static BufferedReader inputReader;
 

    private Utilities(){
        
    }
    //Initiates a connection with ds-server 
    //Uses port 50000
    public static void initConnection() throws UnknownHostException, IOException{

        socket = new Socket("localhost", 50000);
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputReader = new BufferedReader(new InputStreamReader (socket.getInputStream()));
        handShake();
    }

    //Executes the initial hand shake with ds-server
    private static void handShake() throws IOException{
        ClientAction.sendHELO();
        readServerOutput();
        ClientAction.sendAUTH(); 
        readServerOutput();
        ClientAction.sendREDY();
    }

    //reads the output from the server and splits the string by whitespace 
    public static String[] readServerOutput() throws IOException{    
        String input = (String)inputReader.readLine();
        String[] serverInput = splitInput(input);     
        if(serverInput[0].equals(".") || serverInput[0] == "\n"){
            input = (String)inputReader.readLine();
            serverInput = splitInput(input);
        }  
        return serverInput;
    }

    //splits the string by white space
    private static String[] splitInput(String input) throws IOException{
        String[] serverInputArr = input.split("\\s+");
        return serverInputArr;
    }

    // Closes the connection with ds-server
    public static void closeConnection() throws IOException{
        inputReader.close();
        socket.close();
    }

    public static DataOutputStream getOutputStream(){
        return outputStream;
    }

    public static BufferedReader getInputReader(){
        return inputReader;
    }

}


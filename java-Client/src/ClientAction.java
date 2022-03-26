import java.io.*;

public class ClientAction {

    public void sendHELO(DataOutputStream outputStream) throws IOException{
        outputStream.write(("HELO\n").getBytes());
    }

    public void sendOK(DataOutputStream outputStream) throws IOException{
        outputStream.write(("OK\n").getBytes());
    }

    public void sendQUIT(DataOutputStream outputStream) throws IOException{
        outputStream.write(("QUIT\n").getBytes());
    }

    public void sendSCHD(String jobID, String serverType, String serverID, DataOutputStream outputStream) throws IOException{
        outputStream.write(("SCHD " + jobID + " " + serverType + " " + serverID + "\n").getBytes());
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

    public void sendGETSType(String serverType, DataOutputStream outputStream) throws IOException{
        outputStream.write(("GETS Type " + serverType + "\n").getBytes());
    }

    public void sendGETSCapable(String core, String memory, String disk, DataOutputStream outputStream) throws IOException{
        outputStream.write(("GETS Capable " + core + " " + memory + " " + disk + "\n").getBytes());
    }

}

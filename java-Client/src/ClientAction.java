import java.io.*;

final class ClientAction {

    private ClientAction(){

    }

    public static void sendHELO() throws IOException{
        Utilities.outputStream.write(("HELO\n").getBytes());
    }

    public static void sendOK() throws IOException{
        Utilities.outputStream.write(("OK\n").getBytes());
    }

    public static void sendQUIT() throws IOException{
        Utilities.outputStream.write(("QUIT\n").getBytes());
    }

    public static void sendSCHD(String jobID, String serverType, String serverID) throws IOException{
        Utilities.outputStream.write(("SCHD " + jobID + " " + serverType + " " + serverID + "\n").getBytes());
    }

    public static void sendAUTH() throws IOException{
        String username = System.getProperty("user.name");
        Utilities.outputStream.write(("AUTH " + username + "\n").getBytes());
        
    }

    public static void sendREDY() throws IOException{
        Utilities.outputStream.write(("REDY\n").getBytes());
    }

    public static void sendGETSAll() throws IOException{
        Utilities.outputStream.write(("GETS All\n").getBytes());
    }

    public static void sendGETSType(String serverType) throws IOException{
        Utilities.outputStream.write(("GETS Type " + serverType + "\n").getBytes());
    }

    public static void sendGETSCapable(String core, String memory, String disk) throws IOException{
        Utilities.outputStream.write(("GETS Capable " + core + " " + memory + " " + disk + "\n").getBytes());
    }

    public static void sendLSTJ(String serverType, String serverID) throws IOException{
        Utilities.outputStream.write(("LSTJ " + serverType + " " + serverID + "\n").getBytes());
    }

    public static  void sendTERM(String serverType, String serverID) throws IOException{
        Utilities.outputStream.write(("TERM " + serverType + " " + serverID + "\n").getBytes());
    }

    public static void sendMIGJ(String jobID, String srcServerType, String srcServerID, String tgtServerType, String tgtServerID) throws IOException{
        Utilities.outputStream.write(("MIGJ " + jobID + " " + srcServerType + " " + srcServerID + " " +  tgtServerType + " " +  tgtServerID + "\n").getBytes());
    }

    public static void sendCNTJ(String serverType, String serverID, String jobState) throws IOException {
        Utilities.outputStream.write(("CNTJ " + serverType + " " + serverID + " " + jobState + "\n").getBytes());
    }

    public static void sendKILJ(String serverType, String serverID, String jobID) throws IOException {
        Utilities.outputStream.write(("KILJ " + serverType + " " + serverID + " " + jobID + "\n").getBytes());
    }

    public static void sendPSHJ() throws IOException {
        Utilities.outputStream.write(("PSHJ \n").getBytes());
    }

}

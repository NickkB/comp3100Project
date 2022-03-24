public class ServerInfo {

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
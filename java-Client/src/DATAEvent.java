public class DATAEvent implements ServerOutObj {

    private int count; 
    //private String temp; 

    public DATAEvent(String count, String temp){
        this.count = Integer.parseInt(count);
        //this.temp = temp;
    }

    public int getCount(){
        return count; 
    }

    
}

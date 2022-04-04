public class DATAEvent {

    private int count; 

    public DATAEvent(String count, String temp){
        this.count = Integer.parseInt(count);
    }

    public int getCount(){
        return count; 
    }    
}

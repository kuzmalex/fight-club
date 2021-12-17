package monitoring;

public class ExecutionMetrics {
    private int dbRequestNumber;
    private int dbRequestExecutionTime;

    public void incrementDbRequestNumber(){
        dbRequestNumber++;
    }

    public void addDbRequestExecutionTime(int time){
        dbRequestExecutionTime+=time;
    }

    public void addDbRequestNumber(int number){
        dbRequestNumber+=number;
    }

    public int getDbRequestNumber() {
        return dbRequestNumber;
    }

    public int getDbRequestExecutionTime() {
        return dbRequestExecutionTime;
    }
}

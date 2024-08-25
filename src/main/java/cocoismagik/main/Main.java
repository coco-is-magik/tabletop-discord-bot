package cocoismagik.main;
public class Main {
    public static void main(String[] args) {
        DataOutputter.writeToFile("this is a test");
        DataOutputter.logMessage("Log message 1", 1);
        DataOutputter.logMessage("Log message 2", 2);
        DataOutputter.logMessage("Log message 3", 3);
    }
}
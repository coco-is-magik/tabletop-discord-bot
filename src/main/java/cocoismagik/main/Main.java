package cocoismagik.main;

import cocoismagik.manager.ApiDataManager;

public class Main {
    /**
     * The main entry point of the application.
     *
     * @param  args	an array of command-line arguments passed to the application
     * @return     	none
     */
    public static void main(String[] args) {
        DataOutputter.logMessage("Program start", DataOutputter.INFO);
        ApiDataManager.startDiscordBot();
    }
}
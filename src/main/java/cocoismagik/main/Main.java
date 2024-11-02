package cocoismagik.main;

import java.util.Scanner;

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

        Scanner scanner = new Scanner(System.in);
        System.out.println("Basic Shell started. Type 'help' for commands or 'exit' to quit.");

        DataOutputter.logMessage("Starting user shell", DataOutputter.INFO);
        while (true) {
            String command = scanner.nextLine().trim(); // Read user input and trim any extra spaces

            // Check for commands
            if (command.equalsIgnoreCase("exit")) {
                DataOutputter.logMessage("Exiting program", DataOutputter.INFO);
                ApiDataManager.stopDiscordBot();
                break; // Exit the loop to stop the shell
            } else if (command.equalsIgnoreCase("help")) {
                DataOutputter.logMessage("Available commands:\n"+
                "  help - Show available commands\n"+
                "  exit - Exit the shell", DataOutputter.INFO);
            } else {
                DataOutputter.logMessage("Unknown command: " + command, DataOutputter.ERROR);
            }
        }

        scanner.close();
        DataOutputter.logMessage("Program end", DataOutputter.TERM);
    }
}
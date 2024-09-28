package cocoismagik.datastructures;

import java.util.HashMap;
import java.util.Map;

public class Messages {

    // Current task codes
    public static final int NO_ACTION = 0;
    public static final int CHAR_CREATE = 1;

    // TTRPG Codes
    public static final int DND5E = 0;
    public static final int HOUSE_GAMES = 1;
    public static final int DARK_AGES = 2;
    public static final int VTM = 3;
    public static final int SR5 = 4;
    public static final int PF1 = 5;
    public static final int CP2020 = 6;
    public static final int MM3 = 7;

    // Datastructure
    private Map<Long, Long> directMessageStates;


    // Private constructor to prevent instantiation
    private Messages() {
        directMessageStates = new HashMap<>();
    }

    // Private static instance of the class
    private static Messages instance = null;

    // Public static method to get the instance of the class
    public static Messages getInstance() {
        if (instance == null) {
            instance = new Messages();
        }
        return instance;
    }

    public static long encodeNumbers(int num1, int num2) {
        return (long) num1 << 32 | num2;
    }

    public static int[] decodeNumbers(long encodedValue) {
        int num1 = (int) (encodedValue >> 32);
        int num2 = (int) (encodedValue & 0xFFFFFFFFL);
        return new int[] { num1, num2 };
    }

    public int getCurrentDirectMessageState(Long userID){
        int[] states = decodeNumbers(directMessageStates.get(userID));
        switch (states[0]) {
            case NO_ACTION:
                return 0;
            case CHAR_CREATE:
                switch(states[1]) {
                    case DND5E:
                        return CHAR_CREATE + DND5E;
                    case HOUSE_GAMES:
                        return CHAR_CREATE + HOUSE_GAMES;
                    case DARK_AGES:
                        return CHAR_CREATE + DARK_AGES;
                    case VTM:
                        return CHAR_CREATE + VTM;
                    case SR5:
                        return CHAR_CREATE + SR5;
                    case PF1:
                        return CHAR_CREATE + PF1;
                    case CP2020:
                        return CHAR_CREATE + CP2020;
                    case MM3:
                        return CHAR_CREATE + MM3;
                    default:
                        return -1;
                }
            default:
                return -1;
        }
    }

    public void setDirectMessageState(Long userID, int taskCode, int secondaryCode) {
        // TODO: use a database
        directMessageStates.put(userID, encodeNumbers(taskCode, secondaryCode));
    }

    public void resetAllDirectMessageStates() {
        for (Map.Entry<Long, Long> entry : directMessageStates.entrySet()) {
            directMessageStates.put(entry.getKey(), encodeNumbers(NO_ACTION, 0));
        }
    }
}

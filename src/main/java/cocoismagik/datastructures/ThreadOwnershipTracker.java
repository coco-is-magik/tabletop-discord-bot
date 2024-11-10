package cocoismagik.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cocoismagik.main.DataOutputter;

public class ThreadOwnershipTracker {
    // Static map to store user (key) to thread (value) mappings
    private static final Map<Long, List<Long>> userToThreadsMap = new HashMap<>();

    // Private constructor to prevent instantiation
    private ThreadOwnershipTracker() {
    }

    /**
     * Associates a user with a thread, marking the thread as owned by the user.
     * 
     * @param userId the ID of the user to associate with the thread
     * @param threadId the ID of the thread to associate with the user
     */
    public static void addOwnership(long userId, long threadId) {
        // First, check if any user already owns the thread
        for (Map.Entry<Long, List<Long>> entry : userToThreadsMap.entrySet()) {
            Long existingUserId = entry.getKey();
            List<Long> threads = entry.getValue();

            if (threads.contains(threadId)) {
                String s = "Thread ID "+threadId+" is already owned by User ID "+existingUserId+", cannot assign to User ID "+userId;
                DataOutputter.logMessage(s, DataOutputter.WARNING);
                return; // Exit the method without adding the ownership
            }
        }
        userToThreadsMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(threadId);
    }

    /**
     * Disassociates a user from a thread, marking the thread as no longer owned
     * by the user.
     * 
     * @param userId the ID of the user to disassociate from the thread
     * @param threadId the ID of the thread to disassociate from the user
     */
    public static void removeOwnership(long userId, long threadId) {
        List<Long> threads = userToThreadsMap.get(userId);
        if (threads != null) {
            threads.remove(threadId);
            // If the list is empty, remove the user entry entirely
            if (threads.isEmpty()) {
                userToThreadsMap.remove(userId);
            }
        }
    }

    /**
     * Checks if a user owns a specific thread.
     *
     * @param userId the ID of the user to check
     * @param threadId the ID of the thread to check
     * @return true if the user owns the thread, false otherwise
     */
    public static boolean ownsThread(long userId, long threadId) {
        List<Long> threads = userToThreadsMap.get(userId);
        return threads != null && threads.contains(threadId);
    }
}

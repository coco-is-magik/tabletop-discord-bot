package cocoismagik.games.leavingearth;

import cocoismagik.games.ActionResult;

public class LeavingEarthGameLogic {

    private Long[] players = new Long[5];
    private int playerCount = 0;
    private int turn = 0;
    private int currentMoney = 30;
    private Long threadID;
    private Long channelID;
    private Long guildID;
    
    public LeavingEarthGameLogic(Long playerID, Long threadID, Long channelID, Long guildID) {
        players[playerCount] = playerID;
        this.threadID = threadID;
        this.channelID = channelID;
        this.guildID = guildID;
        playerCount++;
    }

    /**
     * Adds a player to the game.
     *
     * @param playerID the ID of the player to add
     * @throws IllegalArgumentException if the maximum number of players (5) is reached
     */
    public void addPlayer(Long playerID) throws IllegalArgumentException {
        if (playerCount >= 4) {
            throw new IllegalArgumentException("Cannot add more than 5 players");
        }
        players[playerCount] = playerID;
        playerCount++;
    }

    /**
     * Removes a player from the game by their ID.
     *
     * @param playerID the ID of the player to remove
     * @throws IllegalArgumentException if the player is not found in the game
     */
    public void removePlayer(Long playerID) throws IllegalArgumentException {
        int index = -1;
        for (int i = 0; i < playerCount; i++) {
            if (players[i].equals(playerID)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            // shift all players after the removed player one position to the left
            for (int j = index; j < playerCount - 1; j++) {
                players[j] = players[j + 1];
            }
            // set the last element to null
            players[playerCount - 1] = null;
            playerCount--;
        } else {
            throw new IllegalArgumentException("Player not found");
        }
    }

    /**
     * Retrieves the list of player IDs in the game.
     *
     * @return the array of player IDs
     */
    public Long[] getPlayers() {
        return players;
    }

    /**
     * Retrieves the number of players in the game.
     *
     * @return the number of players
     */
    public int getPlayerCount() {
        return playerCount + 1;
    }

    /**
     * Retrieves the unique identifier of the thread associated with this instance.
     *
     * @return the thread ID
     */
    public Long getThreadID() {
        return threadID;
    }

    /**
     * Retrieves the unique identifier of the channel associated with this instance.
     *
     * @return the channel ID
     */
    public Long getChannelID() {
        return channelID;
    }

    /**
     * Retrieves the unique identifier of the guild associated with this instance.
     *
     * @return the guild ID
     */
    public Long getGuildID() {
        return guildID;
    }

    /**
     * Ends the current player's turn, incrementing the turn counter and resetting it to 0 if necessary.
     *
     * @return An ActionResult object containing the updated turn information.
     */
    public ActionResult playerEndTurn() {
        ActionResult result = new ActionResult("Player " + players[turn] + " ended their turn", true, 0);
        turn++;
        if (turn >= playerCount) {
            turn = 0;
        }
        result.addAdditionalData("turn", turn);
        return result;
    }

    /**
     * Initiates a player's turn, resetting their money and updating the turn tracker.
     *
     * @return An ActionResult object containing the turn and money information.
     */
    public ActionResult playerStartTurn() {
        currentMoney = 30;
        ActionResult result = new ActionResult("Player " + players[turn] + " started their turn", true, 0);
        result.addAdditionalData("turn", turn);
        result.addAdditionalData("money", currentMoney);
        return result;
    }
}

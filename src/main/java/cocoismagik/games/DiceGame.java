package cocoismagik.games;

public class DiceGame {
    public static ActionResult guess(int guess) {
        int dice1 = (int) (Math.random() * 6) + 1;
        int dice2 = (int) (Math.random() * 6) + 1;
        ActionResult result = new ActionResult("Player guessed " + guess + ((dice1 + dice2 == guess) ? " and won" : " and lost") + "!", true, 0);
        result.addAdditionalData("dice1", dice1);
        result.addAdditionalData("dice2", dice2);
        result.addAdditionalData("guess", guess);
        return result;
    }
}

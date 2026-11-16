package com.ludoking.offline;

import java.util.Random;

public class DiceController {
    private static Integer oneShotQueue = null;
    private static String godModeColor = null; // Stores "RED", "BLUE", "GREEN", "YELLOW", or null
    private static final Random random = new Random();

    // Queues a manual dice number (1-6) for ONE roll only
    public static void queueSingleRoll(int number) {
        if (number >= 1 && number <= 6) {
            oneShotQueue = number;
        }
    }

    // Sets whichever color you chose on the controller to win 100%
    public static void setGodModeColor(String color) {
        if (color == null || color.equalsIgnoreCase("NONE")) {
            godModeColor = null;
        } else {
            godModeColor = color.toUpperCase();
        }
    }

    // Call this inside your game's dice roll method: DiceController.getNextRoll(currentPlayerColor)
    public static int getNextRoll(String currentTurnColor) {
        // 1. One-Shot Manual Override (Never locks!)
        if (oneShotQueue != null) {
            int result = oneShotQueue;
            oneShotQueue = null; // Clears immediately so you must tap again next time
            return result;
        }

        // 2. Dynamic God Mode for whichever color you selected
        if (godModeColor != null) {
            if (godModeColor.equalsIgnoreCase(currentTurnColor)) {
                // High, natural-looking rolls for your chosen winning side
                int[] winningRolls = {4, 5, 6, 5, 6, 4};
                return winningRolls[random.nextInt(winningRolls.length)];
            } else {
                // Low rolls for opponents so they cannot catch up
                int[] losingRolls = {1, 2, 3, 2, 1, 3};
                return losingRolls[random.nextInt(losingRolls.length)];
            }
        }

        // 3. Normal fair roll when God Mode is OFF and no number is queued
        return random.nextInt(6) + 1;
    }
}

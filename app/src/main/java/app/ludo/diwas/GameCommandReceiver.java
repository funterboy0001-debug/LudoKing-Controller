package com.ludoking.offline;

import org.json.JSONObject;

public class GameCommandReceiver {

    public static void onMessageReceived(String rawJson) {
        try {
            JSONObject json = new JSONObject(rawJson);
            String action = json.getString("action");

            if ("ONE_SHOT_DICE".equals(action)) {
                int diceNumber = json.getInt("value");
                DiceController.queueSingleRoll(diceNumber);

            } else if ("SET_GOD_MODE".equals(action)) {
                String color = json.getString("color"); // "RED", "BLUE", "GREEN", "YELLOW", or "NONE"
                DiceController.setGodModeColor(color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

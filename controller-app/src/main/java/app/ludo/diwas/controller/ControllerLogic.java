package com.ludoking.controller;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import org.json.JSONObject;

public class ControllerLogic {

    private String selectedColor = "RED"; // Changes dynamically to whichever color you tap
    private boolean isGodModeOn = false;
    private TextView statusLabel;

    public void setupControls(
            Button btnRed, Button btnBlue, Button btnGreen, Button btnYellow,
            Switch godModeSwitch, TextView statusText) {

        this.statusLabel = statusText;

        // Tap ANY color button to switch your active/winning side
        btnRed.setOnClickListener(v -> selectSide("RED", godModeSwitch.isChecked()));
        btnBlue.setOnClickListener(v -> selectSide("BLUE", godModeSwitch.isChecked()));
        btnGreen.setOnClickListener(v -> selectSide("GREEN", godModeSwitch.isChecked()));
        btnYellow.setOnClickListener(v -> selectSide("YELLOW", godModeSwitch.isChecked()));

        // Toggle God Mode ON or OFF for whichever color is currently selected
        godModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isGodModeOn = isChecked;
            sendGodModeCommand(selectedColor, isGodModeOn);
            updateStatusDisplay();
        });
    }

    private void selectSide(String color, boolean godModeActive) {
        selectedColor = color;
        isGodModeOn = godModeActive;
        sendGodModeCommand(selectedColor, isGodModeOn);
        updateStatusDisplay();
    }

    // One-Shot Dice Buttons (1-6): Sends once and resets immediately
    public void setupDiceButton(Button diceBtn, int diceValue) {
        diceBtn.setOnClickListener(v -> {
            sendSingleDiceCommand(diceValue);

            diceBtn.setBackgroundColor(Color.parseColor("#10B981"));
            statusLabel.setText("[" + selectedColor + "]: SENT " + diceValue + " (1 ROLL)");

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                diceBtn.setBackgroundColor(Color.parseColor("#374151"));
                updateStatusDisplay();
            }, 350);
        });
    }

    private void updateStatusDisplay() {
        if (isGodModeOn) {
            statusLabel.setText("[" + selectedColor + "]: GOD MODE ON");
        } else {
            statusLabel.setText("[" + selectedColor + "]: READY");
        }
    }

    private void sendSingleDiceCommand(int number) {
        try {
            JSONObject json = new JSONObject();
            json.put("action", "ONE_SHOT_DICE");
            json.put("value", number);
            SocketClient.send(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendGodModeCommand(String color, boolean enabled) {
        try {
            JSONObject json = new JSONObject();
            json.put("action", "SET_GOD_MODE");
            json.put("color", enabled ? color : "NONE");
            SocketClient.send(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

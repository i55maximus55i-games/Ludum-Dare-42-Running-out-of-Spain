package ru.codemonkeystudio.old42;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;

public class GamePad implements ControllerListener {
    private HashMap<Integer, Button> buttons;

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (!buttons.containsKey(buttonCode))
            buttons.put(buttonCode, new Button());
        buttons.get(buttonCode).justPressed = true;
        buttons.get(buttonCode).pressed = true;
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if (!buttons.containsKey(buttonCode))
            buttons.put(buttonCode, new Button());
        buttons.get(buttonCode).justPressed = false;
        buttons.get(buttonCode).pressed = false;
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }


    public boolean isButtonJustPressed(int buttonCode) {
        return buttons.get(buttonCode).justPressed;
    }

    public boolean isButtonPressed(int buttonCode) {
        return buttons.get(buttonCode).pressed;
    }

    public void update() {
        for (Button button : buttons.values()) {
            button.justPressed = false;
        }
    }

    private class Button {
        boolean pressed = false;
        boolean justPressed = false;
    }
}

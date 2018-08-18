package ru.codemonkeystudio.old42.tools;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;

public class GamePad implements ControllerListener {
    private HashMap<Integer, Button> buttons;
    private Vector2 lStick = new Vector2();
    private float deathZone = 0.1f;

    private boolean leftJustPressed = false;
    private boolean rightJustPressed = false;

    public GamePad() {
        buttons = new HashMap<Integer, Button>();
    }

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
        if (axisCode == 0)
            lStick.y = -value;
        if (axisCode == 1)
            lStick.x = value;
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        if (value == PovDirection.west)
            leftJustPressed = true;
        if (value == PovDirection.east)
            rightJustPressed = true;
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
        return buttons.containsKey(buttonCode) && buttons.get(buttonCode).justPressed;
    }

    public boolean isButtonPressed(int buttonCode) {
        return buttons.containsKey(buttonCode) && buttons.get(buttonCode).pressed;
    }

    public boolean isLeftJustPressed() {
        if (leftJustPressed) {
            leftJustPressed = false;
            return true;
        }
        return false;
    }

    public boolean isRightJustPressed() {
        if (rightJustPressed) {
            rightJustPressed = false;
            return true;
        }
        return false;
    }

    public Vector2 getlStick() {
        if (lStick.len() <= 0.2)
            lStick.setZero();
        return lStick.cpy();
    }

    public void update() {
        for (Button button : buttons.values()) {
            button.justPressed = false;
        }
        leftJustPressed = false;
        rightJustPressed = false;
    }

    private class Button {
        boolean pressed = false;
        boolean justPressed = false;
    }
}

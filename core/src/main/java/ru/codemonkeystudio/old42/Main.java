package ru.codemonkeystudio.old42;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ru.codemonkeystudio.old42.screens.LogoScreen;
import ru.codemonkeystudio.old42.screens.StrategyScreen;
import ru.codemonkeystudio.old42.tools.GamePad;

import java.util.ArrayList;

public class Main extends Game {

    public static Main game;

    public static ArrayList<GamePad> gamePads;


    private ShapeRenderer shapeRenderer;

    private float blend;
    private float blendDelta;
    private Color blendColor;

    private Color color;
    private Screen screen;


    public int[] stages;
    public float difficulty = 1;

    @Override
    public void create() {
        game = this;
        shapeRenderer = new ShapeRenderer();
        color = Color.WHITE;
        setScreen(new LogoScreen(), Color.BLACK, 0.015f);
        blend = -0.3f;

        gamePads = new ArrayList<GamePad>();
        for (Controller controller : Controllers.getControllers()) {
            gamePads.add(new GamePad());
            controller.addListener(gamePads.get(gamePads.size() - 1));
        }

        stages = new int[8];
        for (int i = 0; i < 8; i++) {
            stages[i] = 0;
        }
    }

    public void setScreen(Screen screen, Color color, float delta) {
        this.screen = screen;
        flash(color, delta);
    }

    public void flash(Color color, float delta) {
        blend = -1.1f;
        blendDelta = delta;
        blendColor = color;
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(color.r, color.g, color.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();


        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(blendColor.r, blendColor.g, blendColor.b, - blend * blend + 1.1f);
        blend += blendDelta;
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();


        if (screen != null && blend >= 0f) {
            setScreen(screen);
            screen = null;
            color = blendColor;
        }


        for (GamePad gamePad : gamePads) {
            gamePad.update();
        }

        if (gamePads.get(0).isButtonJustPressed(6))
            restart();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        shapeRenderer = new ShapeRenderer();
    }

    public void restart() {
        for (int i = 0; i < 8; i++) {
            stages[i] = 0;
        }
        difficulty = 1;
        setScreen(new LogoScreen());
    }
}
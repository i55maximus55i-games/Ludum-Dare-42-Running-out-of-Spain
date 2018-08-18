package ru.codemonkeystudio.old42.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.codemonkeystudio.old42.Main;

import java.util.Random;

public class StrategyScreen implements Screen {

    private OrthographicCamera camera;
    private Viewport viewport;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private float timer;
    private boolean blink;

    int cursor = 0;
    int[] a = new int[8];
    int n;


    SpriteBatch batch;

    Texture yes;


    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.position.x = 64;
        camera.position.y = 64;
        viewport = new FitViewport(128, 128, camera);

        map = new TmxMapLoader().load("maps/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        timer = 0;
        blink = false;

        attack();
        attack();


        yes = new Texture("sprites/yes.png");

        batch = new SpriteBatch();

        int c = 0;
        for (int i = 0; i < 8; i++) {
            if (Main.game.stages[i] == 1 && i != cursor)
                c = i;
        }
        cursor = c;
    }

    private void attack() {
        n = 0;

        if (Main.game.stages[0] == 2) {
            check(1);
            check(2);
            check(6);
        }
        if (Main.game.stages[1] == 2) {
            check(0);
            check(2);
            check(4);
        }
        if (Main.game.stages[2] == 2) {
            check(0);
            check(1);
            check(3);
            check(4);
            check(6);
        }
        if (Main.game.stages[3] == 2) {
            check(2);
            check(4);
            check(5);
            check(7);
        }
        if (Main.game.stages[4] == 2) {
            check(1);
            check(2);
            check(3);
        }
        if (Main.game.stages[5] == 2) {
            check(2);
            check(3);
            check(6);
            check(7);
        }
        if (Main.game.stages[6] == 2) {
            check(0);
            check(2);
            check(5);
            check(7);
        }
        if (Main.game.stages[7] == 2) {
            check(3);
            check(5);
            check(6);
        }

        check(0);
        check(1);
        check(4);
        check(6);
        check(7);

        if (n > 0) {
            int c = new Random().nextInt(n);
            Main.game.stages[a[c]] = 1;
        }
    }

    private void check(int c) {
        boolean p = Main.game.stages[c] == 0;
        for (int i = 0; i < n; i++)
            if (a[i] == c)
                p = false;
        if (p) {
            a[n] = c;
            n++;
        }
    }

    @Override
    public void render(float delta) {
        mapRenderer.getBatch().begin();
        mapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("back"));
        for (int i = 0; i < 8; i++) {
            if (Main.game.stages[i] == 1 && blink)
                mapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(i+1));
            if (Main.game.stages[i] == 2)
                mapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(i+1));
        }
        mapRenderer.getBatch().end();
        timer += delta;
        if (timer >= 0.4f) {
            timer -= 0.4f;
            blink = !blink;
        }

        batch.begin();
        if (cursor == 0)
            batch.draw(yes, 20, 95);
        if (cursor == 1)
            batch.draw(yes, 54, 99);
        if (cursor == 2)
            batch.draw(yes, 61, 84);
        if (cursor == 3)
            batch.draw(yes, 75, 60);
        if (cursor == 4)
            batch.draw(yes, 102, 87);
        if (cursor == 5)
            batch.draw(yes, 42, 57);
        if (cursor == 6)
            batch.draw(yes, 19, 40);
        if (cursor == 7)
            batch.draw(yes, 74, 34);
        batch.end();

        if (Main.gamePads.get(0).isLeftJustPressed()) {
            cursor--;
            if (cursor < 0)
                cursor = 7;
            while (Main.game.stages[cursor] != 1) {
                cursor--;
                if (cursor < 0)
                    cursor = 7;
            }
        }
        if (Main.gamePads.get(0).isRightJustPressed()) {
            cursor++;
            if (cursor > 7)
                cursor = 0;
            while (Main.game.stages[cursor] != 1) {
                cursor++;
                if (cursor > 7)
                    cursor = 0;
            }
        }

        if (Main.gamePads.get(0).isButtonJustPressed(0)) {
            for (int i = 0; i < 8; i++) {
                if (Main.game.stages[i] == 1 && i != cursor) {
                    Main.game.stages[i] = 2;
                }
            }
            Main.game.setScreen(new ActionScreen(cursor), new Color(117f / 255f, 168f / 255f, 231f / 255f, 1), 0.05f);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
        mapRenderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

package ru.codemonkeystudio.old42.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.codemonkeystudio.old42.Main;
import ru.codemonkeystudio.old42.tools.GifDecoder;

public class LogoScreen implements Screen {

    private Viewport viewport;
    private SpriteBatch batch;
    private Animation<TextureRegion> intro1;
    private Animation<TextureRegion> intro2;
    private float elapsed;

    private boolean start;
    private boolean flash;
    private float timer;

    @Override
    public void show() {
        viewport = new ScreenViewport();
        batch = new SpriteBatch();
        intro1 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("intro.gif").read());
        intro2 = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("intro1.gif").read());
        start = false;
        flash = true;
        timer = 0;
    }

    @Override
    public void render(float delta) {
        float x = Math.max(1920f / (float)Gdx.graphics.getWidth(), 1080f / (float)Gdx.graphics.getHeight());

        elapsed += delta * 2;
        timer += delta;
        batch.begin();
        batch.draw(start ? intro2.getKeyFrame(elapsed) : intro1.getKeyFrame(timer), ((float) - 1920) / x / 2, ((float) - 1080) / x / 2, 1920 / x, 1080 / x);
        batch.end();

        if (Main.gamePads.get(0).isButtonJustPressed(7) && !start) {
            start = true;
            timer = 0;
            elapsed = 0;
        }
        if (timer >= 2.3f && start && flash) {
            Main.game.setScreen(new MainMenuScreen(), Color.WHITE, 0.1f);
            flash = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        batch.setProjectionMatrix(viewport.getCamera().combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}

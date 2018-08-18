package ru.codemonkeystudio.old42.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.codemonkeystudio.old42.Main;
import ru.codemonkeystudio.old42.sprites.Enemy;
import ru.codemonkeystudio.old42.sprites.Player;
import ru.codemonkeystudio.old42.tools.ContactHandler;

import java.util.ArrayList;

public class ActionScreen implements Screen {

    public static final float SCALE = 5f;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    private float timer;
    private float timer2;


    int num;

    public ActionScreen(int num) {
        this.num = num;
    }

    @Override
    public void show() {
        map = new TmxMapLoader().load("maps/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        batch = new SpriteBatch();
        viewport = new ScreenViewport(camera);

        world = new World(new Vector2(0, -400), true);
        world.setContactListener(new ContactHandler());
        createWalls();
        createPlayer();
        createEnemy();

        debugRenderer = new Box2DDebugRenderer();
    }

    private void createWalls() {
        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;

        for (RectangleMapObject i : map.getLayers().get("walls").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = i.getRectangle();
            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth() / 2) / SCALE, (rect.getY() + rect.getHeight() / 2) / SCALE);

            body = world.createBody(bDef);

            shape.setAsBox(rect.getWidth() / 2 / SCALE, rect.getHeight() / 2 / SCALE);
            fDef.shape = shape;
            fDef.friction = 0;
            body.createFixture(fDef);
            body.setUserData("wall");
        }
    }

    private void createPlayer() {
        for (RectangleMapObject i : map.getLayers().get("player").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = i.getRectangle();
            player = new Player(world, new Vector2(rect.getX(), rect.getY()));
            camera.position.x = player.body.getPosition().cpy().scl(SCALE).x;
            camera.position.y = player.body.getPosition().cpy().scl(SCALE).y;
        }
    }

    private void createEnemy() {
        for (RectangleMapObject i : map.getLayers().get("enemy").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = i.getRectangle();
            enemies.add(new Enemy(world, new Vector2(rect.getX(), rect.getY())));
        }
    }

    @Override
    public void render(float delta) {
        timer2 += delta;
        world.step(delta, 120, 10);

        player.update(enemies, world);
        for (Enemy enemy : enemies)
            enemy.update(player.body.getPosition().x, delta);
        timer += delta;
        if (timer * Main.game.difficulty >= 10) {
            timer = 0;
            createEnemy();
        }


        Vector2 camPos = player.body.getPosition().scl(SCALE);
        if (camPos.x <= 250f * Gdx.graphics.getWidth() / Gdx.graphics.getHeight())
            camPos.x = 250f * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        if (camPos.x >= 1536 - 250f * Gdx.graphics.getWidth() / Gdx.graphics.getHeight())
            camPos.x = 1536 - 250f * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        if (camPos.y <= 250)
            camPos.y = 250;

        Vector2 camSpeed = new Vector2(camera.position.x, camera.position.y).sub(camPos).rotate(180).scl(0.075f);
        camera.position.x += camSpeed.x;
        camera.position.y += camSpeed.y;

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);


        mapRenderer.render();

        batch.begin();
        player.draw(batch);
        for (Enemy enemy : enemies)
            enemy.draw(batch);
        batch.end();

        if (timer2 >= 10) {
            Main.game.difficulty *= 1.4f;
            Main.game.setScreen(new StrategyScreen(), new Color(117f / 255f, 168f / 255f, 231f / 255f, 1), 0.05f);
            timer2 = -100000000;
        }

        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        float scale = 500f;

        viewport.update(width, height);
        camera.viewportWidth = scale * width / height;
        camera.viewportHeight = scale;
        camera.update();
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

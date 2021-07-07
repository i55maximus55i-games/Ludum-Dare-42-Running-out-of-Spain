package ru.codemonkeystudio.old42.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ru.codemonkeystudio.old42.Main;
import ru.codemonkeystudio.old42.screens.ActionScreen;

import java.util.ArrayList;

public class Player extends Sprite {

    public Body body;
    public int health = 15;
    private int jump = 0;
    private boolean stand = false;

    private Texture texture;
    private TextureRegion textureRegion;

    public Player(World world, Vector2 pos) {
        texture = new Texture("sprites/player.png");
        textureRegion = new TextureRegion(texture, 0 ,0, 128, 128);

        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();

        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set((pos.x + 64 / 2) / ActionScreen.SCALE, (pos.y + 64 / 2) / ActionScreen.SCALE);
        setPosition((pos.x + 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE, (pos.y + 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE);

        body = world.createBody(bDef);

        shape.setAsBox(64 / 2 / ActionScreen.SCALE, 64 / 2 / ActionScreen.SCALE);
        fDef.shape = shape;
        fDef.friction = 0;
        body.createFixture(fDef);
        body.setUserData("player");
    }

    public void update(ArrayList<Enemy> enemies, World world) {
        setPosition((body.getPosition().x - 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE, (body.getPosition().y - 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE);

        float x = 0f;
        if (!Main.gamePads.isEmpty()) x += Main.gamePads.get(0).getlStick().x;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) x -= 1f;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += 1f;
        if (x < -1f) x = -1f;
        if (x > 1f) x = 1f;

        body.setLinearVelocity(x * 70, body.getLinearVelocity().y);
        if ((!Main.gamePads.isEmpty() && Main.gamePads.get(0).isButtonJustPressed(1) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) && jump < 2) {
            body.setLinearVelocity(body.getLinearVelocity().x, 150);
            jump++;
        }
        if (body.getLinearVelocity().y == 0 && stand)
            jump = 0;
        stand = body.getLinearVelocity().y == 0;

//        if (Main.gamePads.get(0).isButtonJustPressed(0)) {
//            for (Enemy enemy : enemies) {
//                if (Math.abs(body.getPosition().x - enemy.body.getPosition().x) * ActionScreen.SCALE <= 72) {
//                    world.destroyBody(body);
//                    enemies.remove(enemy);
//                }
//            }
//        }
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(textureRegion, getX(), getY(), 64, 64);
    }
}

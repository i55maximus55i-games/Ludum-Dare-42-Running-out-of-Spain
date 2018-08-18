package ru.codemonkeystudio.old42.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ru.codemonkeystudio.old42.screens.ActionScreen;

public class Enemy extends Sprite {

    public Body body;
    public int health = 3;
    private int jump = 0;
    private boolean stand = false;
    private float timer;
    private Texture texture;
    private TextureRegion textureRegion;
    private boolean penis = false;
    private float timer2;

    public Enemy(World world, Vector2 pos) {
        texture = new Texture("sprites/enemy.png");
        textureRegion = new TextureRegion(texture, 0,0, 128, 128);

        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();

        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set((pos.x + 64 / 2) / ActionScreen.SCALE, (pos.y + 64 / 2) / ActionScreen.SCALE);
        setPosition((pos.x + 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE, (pos.y + 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE);

        body = world.createBody(bDef);

        shape.setAsBox(32 / 2 / ActionScreen.SCALE, 64 / 2 / ActionScreen.SCALE);
        fDef.shape = shape;
        body.createFixture(fDef);
        body.setUserData("enemy");
    }

    public void update(float x, float delta) {
        timer += delta;
        setPosition((body.getPosition().x - 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE, (body.getPosition().y - 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE);
        if (body.getLinearVelocity().x == 0 && jump < 1) {
            body.setLinearVelocity(body.getLinearVelocity().x, 150);
            jump++;
        }
        if (body.getLinearVelocity().y == 0 && stand)
            jump = 0;
        stand = body.getLinearVelocity().y == 0;
        body.setLinearVelocity(body.getPosition().x < x ? 50 : -50 , body.getLinearVelocity().y);
        if (timer <= 0)
            body.setLinearVelocity(-body.getLinearVelocity().x, body.getLinearVelocity().y);
        if (Math.abs((body.getPosition().x - x) * ActionScreen.SCALE) <= 64) {
            timer = -0.5f;
        }
        timer2 += delta;
        if (timer2 > 0.25) {
            timer2 = 0;
            penis = ! penis;
        }

        if (penis)
            textureRegion = new TextureRegion(texture, 0, 0, 128, 128);
        else
            textureRegion = new TextureRegion(texture, 128, 0, 128, 128);
        if (timer <= 0) {
            textureRegion = new TextureRegion(texture, 256, 0, 128, 128);
            textureRegion.flip(true, false);
        }
        if (body.getLinearVelocity().x > 0)
            textureRegion.flip(true, false);
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(textureRegion, getX(), getY(), 64, 64);
    }
}

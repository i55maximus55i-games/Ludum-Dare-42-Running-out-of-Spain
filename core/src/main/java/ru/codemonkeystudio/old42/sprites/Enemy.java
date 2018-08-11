package ru.codemonkeystudio.old42.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ru.codemonkeystudio.old42.screens.ActionScreen;

public class Enemy extends Sprite {

    public Body body;

    public Enemy(World world, Vector2 pos) {
        super(new Texture("sprites/enemy.png"));

        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();

        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set((pos.x + 64 / 2) / ActionScreen.SCALE, (pos.y + 64 / 2) / ActionScreen.SCALE);
        setPosition((pos.x + 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE, (pos.y + 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE);

        body = world.createBody(bDef);

        shape.setAsBox(64 / 2 / ActionScreen.SCALE, 64 / 2 / ActionScreen.SCALE);
        fDef.shape = shape;
        body.createFixture(fDef);
        body.setUserData("enemy");
    }

    public void update(float x) {
        setPosition((body.getPosition().x - 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE, (body.getPosition().y - 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE);
        body.setLinearVelocity(body.getPosition().x < x ? 10 : -10 , body.getLinearVelocity().y);
    }
}

package ru.codemonkeystudio.old42.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ru.codemonkeystudio.old42.Main;
import ru.codemonkeystudio.old42.screens.ActionScreen;

public class Player extends Sprite {

    public Body body;
    private int jump = 0;
    private boolean stand = false;

    public Player(World world, Vector2 pos) {
        super(new Texture("sprites/player.png"));

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

    public void update() {
        setPosition((body.getPosition().x - 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE, (body.getPosition().y - 64 / 2 / ActionScreen.SCALE) * ActionScreen.SCALE);

        body.setLinearVelocity(Main.gamePads.get(0).getlStick().x * 70, body.getLinearVelocity().y);
        if (Main.gamePads.get(0).isButtonJustPressed(0) && jump < 2) {
            body.setLinearVelocity(body.getLinearVelocity().x, 150);
            jump++;
        }
        if (body.getLinearVelocity().y == 0 && stand)
            jump = 0;
        stand = body.getLinearVelocity().y == 0;
    }
}

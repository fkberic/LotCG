package com.neffy.lotcg.Sprites.MapFeatures.Platforms;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Screens.PlayScreen;

public class PlatformVert extends Sprite{
    public enum State {UP, DOWN};
    public State currentState;
    public State previousState;
    public Vector2 velocity;
    public World world;
    public Body body;
    private TextureRegion platformIdle;

    public PlatformVert(PlayScreen screen, float x, float y) {
        super(screen.getAtlas().findRegion("LotCGSprite"));
        currentState = State.DOWN;
        previousState = State.DOWN;
        world = screen.getWorld();

        setPosition(x + 7 / LotCG.V_SCALE, y + 7 / LotCG.V_SCALE);
        platformIdle = new TextureRegion(getTexture(), 369, 0, 16, 16);
        setBounds (getX(), getY(), 72/ LotCG.V_SCALE, 3/LotCG.V_SCALE);
        setRegion (platformIdle);
        definePlatform();
    }

    public void definePlatform () {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);
        body.setGravityScale(0);
        MassData mass = new MassData();
        mass.mass = 9999999999999f;
        body.setMassData(mass);

        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        shape.setAsBox(72 / 2 / LotCG.V_SCALE, 3 / LotCG.V_SCALE);
        fdef.filter.categoryBits = LotCG.PLATFORM_BIT;
        fdef.filter.maskBits = LotCG.HERO_BIT | LotCG.PLATFORM_BOUND_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    public void update(float delta) {
        if (body.getLinearVelocity().y == 0 && currentState == State.DOWN) {
             body.setLinearVelocity(0, 0.5f);
            currentState = State.UP;
        }
        else if (body.getLinearVelocity().y == 0 && currentState == State.UP) {
            body.setLinearVelocity(0, -0.5f);
            currentState = State.DOWN;
        }

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(platformIdle);
    }
}

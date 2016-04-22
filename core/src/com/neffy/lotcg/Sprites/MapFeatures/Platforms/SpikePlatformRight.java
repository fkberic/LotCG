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

public class SpikePlatformRight extends Sprite{
    public enum State {LEFT, RIGHT};
    public State currentState;
    public State previousState;
    public Vector2 velocity;
    public World world;
    public Body body;
    private TextureRegion platformIdle;

    public SpikePlatformRight (PlayScreen screen, float x, float y) {
        super(screen.getAtlas().findRegion("LotCGSprite"));
        currentState = State.RIGHT;
        previousState = State.RIGHT;
        world = screen.getWorld();

        setPosition(x + 7 / LotCG.V_SCALE, y + 7 / LotCG.V_SCALE);
        platformIdle = new TextureRegion(getTexture(), 353, 0, 16, 16);
        setBounds (getX(), getY(), 16/ LotCG.V_SCALE, 16/LotCG.V_SCALE);
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
        shape.setAsBox(16 / 2 / LotCG.V_SCALE, 14 / 2 / LotCG.V_SCALE);
        fdef.filter.categoryBits = LotCG.ENEMY_BIT;
        fdef.filter.maskBits = LotCG.HERO_BIT | LotCG.PLATFORM_BOUND_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    public void update(float delta) {
        if (body.getLinearVelocity().x == 0 && currentState == State.RIGHT) {
            body.setLinearVelocity(2, 0);
            currentState = State.LEFT;
        }
        else if (body.getLinearVelocity().x == 0 && currentState == State.LEFT) {
            body.setLinearVelocity(-0.3f, 0);
            currentState = State.RIGHT;
        }

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(platformIdle);
    }
}

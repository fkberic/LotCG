package com.neffy.lotcg.Sprites.MapFeatures.Platforms;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Screens.PlayScreen;

public class PlatformBoundary extends Sprite{
    public Vector2 velocity;
    public World world;
    public Body body;

    private TextureRegion platformIdle;

    public PlatformBoundary(PlayScreen screen, float x, float y) {
        super(screen.getAtlas().findRegion("LotCGSprite"));
        world = screen.getWorld();

        setPosition(x + 7 / LotCG.V_SCALE, y + 7 / LotCG.V_SCALE);
        platformIdle = new TextureRegion(getTexture(), 0, 18, 16, 16);
        setBounds (getX(), getY(), 15/ LotCG.V_SCALE, 15/LotCG.V_SCALE);
        setRegion(platformIdle);
        definePlatform();
    }

    public void definePlatform () {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        shape.setAsBox(15 / 2 / LotCG.V_SCALE, 15 / 2 / LotCG.V_SCALE);
        fdef.filter.categoryBits = LotCG.PLATFORM_BOUND_BIT;
        fdef.filter.maskBits = LotCG.PLATFORM_BIT | LotCG.ENEMY_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }
}

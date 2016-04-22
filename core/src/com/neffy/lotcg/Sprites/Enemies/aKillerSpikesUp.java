package com.neffy.lotcg.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Screens.PlayScreen;

public class aKillerSpikesUp extends Enemy{
    // animation + texture for killer coins
    private Animation coinIdle;
    private TextureRegion spikeIdle;

    // tools for animation + texture for killer coins
    private float stateTimer;

    public aKillerSpikesUp(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        stateTimer = 0;
        velocity = new Vector2(0, 3);
        body.setActive(false);


        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("LotCGSprite"), 304, 0, 16, 16));
        coinIdle = new Animation (100, frames);
        frames.clear();
        setBounds(getX(), getY(), 16 / LotCG.V_SCALE, 16 / LotCG.V_SCALE);
    }

    @Override
    protected void defineEnemy() {
        // body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);

        CircleShape shape = new CircleShape();
        FixtureDef fdef = new FixtureDef();

        // hit box fixture definitions
        shape.setRadius(8/LotCG.V_SCALE);
        fdef.filter.categoryBits = LotCG.ENEMY_BIT;
        fdef.filter.maskBits = LotCG.HERO_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    // update position
    public void update (float delta){
        stateTimer += delta;
        body.setLinearVelocity(velocity);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(coinIdle.getKeyFrame(stateTimer, true));
    }
}

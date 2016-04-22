package com.neffy.lotcg.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Screens.PlayScreen;

public class KillerWater extends Enemy{
    // animation + texture for killer coins
    private Animation coinIdle;

    // tools for animation + texture for killer coins
    private float stateTimer;

    public KillerWater(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        stateTimer = 0;
        velocity = new Vector2(0, 0);
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
        shape.setRadius(7/LotCG.V_SCALE);
        fdef.filter.categoryBits = LotCG.ENEMY_BIT;
        fdef.filter.maskBits = LotCG.HERO_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    // update position
    public void update (float delta){
    }
}

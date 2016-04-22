package com.neffy.lotcg.Sprites.LevelObject;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Screens.MenuScreen;
import com.neffy.lotcg.Screens.PlayScreen;

public class LevelUp extends Sprite {
    protected PlayScreen screen;
    protected MenuScreen mScreen;
    protected World world;
    public Body body;

    public LevelUp(PlayScreen screen, float x, float y) {
        world = screen.getWorld();
        this.screen = screen;
        setPosition(x + 7/LotCG.V_SCALE, y + 7/LotCG.V_SCALE);

        // body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);

        CircleShape shape = new CircleShape();
        FixtureDef fdef = new FixtureDef();

        // hit box fixture definitions
        shape.setRadius(7/ LotCG.V_SCALE);
        fdef.filter.categoryBits = LotCG.LEVELUP_BIT;
        fdef.filter.maskBits = LotCG.HERO_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }
    public LevelUp(MenuScreen screen, float x, float y) {
        world = screen.getWorld();
        this.mScreen = screen;
        setPosition(x + 7/LotCG.V_SCALE, y + 7/LotCG.V_SCALE);

        // body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);

        CircleShape shape = new CircleShape();
        FixtureDef fdef = new FixtureDef();

        // hit box fixture definitions
        shape.setRadius(7/ LotCG.V_SCALE);
        fdef.filter.categoryBits = LotCG.LEVELUP_BIT;
        fdef.filter.maskBits = LotCG.HERO_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

}

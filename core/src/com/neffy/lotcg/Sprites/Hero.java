package com.neffy.lotcg.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Screens.MenuScreen;
import com.neffy.lotcg.Screens.PlayScreen;
import com.neffy.lotcg.Tools.SoundManager;

public class Hero extends Sprite{
    // states for hero
    public enum State {DEAD, FALLING, JUMPING, RUNNING, STANDING, LEVELUP, LEVELDOWN, ONPLATFORM};
    public State currentState;
    public State previousState;

    public World world;
    public Body body;

    // animation + texture for hero states
    private Animation heroDead;
    private Animation heroJump;
    private Animation heroRun;
    private TextureRegion heroIdle;

    // tools for animation + texture for hero state
    private boolean faceRight;
    private boolean isDead;
    private boolean isLevelUp;
    private boolean isLevelDown;
    private boolean nextLevel = true;
    private boolean grounded;
    private float stateTimer;

    /*          HERO SETUP          */
    // constructor
    public Hero(PlayScreen screen, boolean levelUp) {
        super(screen.getAtlas().findRegion("LotCGSprite"));
        nextLevel = levelUp;
        setupHero(screen);
    }
    public Hero(MenuScreen screen, boolean levelUp) {
        super(screen.getAtlas().findRegion("LotCGSprite"));
        nextLevel = levelUp;
        setupHero(screen);
    }

    // setup constructor
    public void setupHero(PlayScreen screen) {
        currentState = State.STANDING;
        previousState = State.STANDING;
        world = screen.getWorld();
        faceRight = true;
        isDead = false;
        grounded = true;
        isLevelUp = false;
        isLevelDown = false;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        // animation for running state
        for (int i = 1; i < 5; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        }
        heroRun = new Animation(0.1f, frames);
        frames.clear();

        // animation for dead state
        for (int i = 5; i < 8; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        }
        heroJump = new Animation(0.1f, frames);
        frames.clear();

        // animation for dead state
        for (int i = 8; i < 15; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        }
        heroDead = new Animation(0.1f, frames);
        frames.clear();

        // texture for idle state
        heroIdle = new TextureRegion(getTexture(), 0, 0, 16, 16);
        setBounds(0, 0, 16 / LotCG.V_SCALE, 16 / LotCG.V_SCALE);
        setRegion(heroIdle);

        defineHero();
    }
    public void setupHero(MenuScreen screen) {
        currentState = State.STANDING;
        previousState = State.STANDING;
        world = screen.getWorld();
        faceRight = true;
        isDead = false;
        grounded = true;
        isLevelUp = false;
        isLevelDown = false;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        // animation for running state
        for (int i = 1; i < 5; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        }
        heroRun = new Animation(0.1f, frames);
        frames.clear();

        // animation for dead state
        for (int i = 5; i < 8; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        }
        heroJump = new Animation(0.1f, frames);
        frames.clear();

        // animation for dead state
        for (int i = 8; i < 15; i++) {
            frames.add(new TextureRegion(getTexture(), i*16, 0, 16, 16));
        }
        heroDead = new Animation(0.1f, frames);
        frames.clear();

        // texture for idle state
        heroIdle = new TextureRegion(getTexture(), 0, 0, 16, 16);
        setBounds(0, 0, 16 / LotCG.V_SCALE, 16 / LotCG.V_SCALE);
        setRegion(heroIdle);

        defineHero();
    }


    public void defineHero() {
        BodyDef bdef = new BodyDef();
        setupPos(bdef);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        CircleShape shape = new CircleShape();
        FixtureDef fdef = new FixtureDef();

        // fixture definitions
        shape.setRadius(7/LotCG.V_SCALE);
        fdef.filter.categoryBits = LotCG.HERO_BIT;
        fdef.filter.maskBits = LotCG.GROUND_BIT | LotCG.NOTHING_BIT | LotCG.PLATFORM_BIT | LotCG.INV_BIT | LotCG.REVEALINV_BIT | LotCG.ENEMY_BIT | LotCG.LEVELUP_BIT | LotCG.LEVELDOWN_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(8 / 2 / LotCG.V_SCALE, 1 / LotCG.V_SCALE);
        shape.setRadius(1 / 100 / LotCG.V_SCALE);
        fdef.shape = shape2;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);

        PolygonShape shape3 = new PolygonShape();
        Vector2[] vertex = new Vector2[4];
        vertex[0] = new Vector2(-5, -9).scl(1/LotCG.V_SCALE);
        vertex[1] = new Vector2(5, -9).scl(1/LotCG.V_SCALE);
        vertex[2] = new Vector2(-3, -3).scl(1/LotCG.V_SCALE);
        vertex[3] = new Vector2(3, -3).scl(1/LotCG.V_SCALE);
        shape3.set(vertex);
        fdef.shape = shape3;
        fdef.filter.categoryBits = LotCG.HERO_FOOT_BIT;
        fdef.filter.maskBits = LotCG.GROUND_BIT | LotCG.PLATFORM_BIT;
        body.createFixture(fdef).setUserData(this);
    }
    public void setupPos (BodyDef bdef) {
        switch (LotCG.level) {
            case -99:
                bdef.position.set(200 / LotCG.V_SCALE, 48 / LotCG.V_SCALE);
                break;
            case 0:
                if (nextLevel == true) {
                    bdef.position.set(8 / LotCG.V_SCALE, 16 / LotCG.V_SCALE);
                    break;
                }
                else if (nextLevel == false){
                    bdef.position.set(1096 / LotCG.V_SCALE, 40 / LotCG.V_SCALE);
                    break;
                }
            case 1:
                if (nextLevel == true) {
                    bdef.position.set(8 / LotCG.V_SCALE, 40 / LotCG.V_SCALE);
                }
                else if (nextLevel == false) {
                    bdef.position.set(1096 / LotCG.V_SCALE, 320 / LotCG.V_SCALE);
                }
                break;
            case 2:
                if (nextLevel == true) {
                    bdef.position.set(8 / LotCG.V_SCALE, 320 / LotCG.V_SCALE);
                }
                else if (nextLevel == false) {
                    bdef.position.set(1096 / LotCG.V_SCALE, 48 / LotCG.V_SCALE);
                }
                break;
            case 3:
                if (nextLevel == true) {
                    bdef.position.set(8 / LotCG.V_SCALE, 48 / LotCG.V_SCALE);
                }
                else if (nextLevel == false) {
                    bdef.position.set(8 / LotCG.V_SCALE, 272 / LotCG.V_SCALE);
                }
                break;
            case 4:
                if (nextLevel == true) {
                    bdef.position.set(1096 / LotCG.V_SCALE, 272 / LotCG.V_SCALE);
                }
                else if (nextLevel == false) {
                    bdef.position.set(736 / LotCG.V_SCALE, 368 / LotCG.V_SCALE);
                }
                break;
            case 5:
                if (nextLevel == true) {
                    bdef.position.set(880 / LotCG.V_SCALE, 8 / LotCG.V_SCALE);
                }
                else if (nextLevel == false) {
                    bdef.position.set(800 / LotCG.V_SCALE, 320 / LotCG.V_SCALE);
                }
                break;
            case 6:
                if (nextLevel == true) {
                    bdef.position.set(800 / LotCG.V_SCALE, 8 / LotCG.V_SCALE);
                }
                else if (nextLevel == false) {
                    bdef.position.set(1088 / LotCG.V_SCALE, 296 / LotCG.V_SCALE);
                }
                break;
            case 7:
                bdef.position.set(8 / LotCG.V_SCALE, 288 / LotCG.V_SCALE);
                break;
            case 8:
                if (nextLevel == true) {
                    bdef.position.set(8 / LotCG.V_SCALE, 124 / LotCG.V_SCALE);
                }
                else if (nextLevel == false) {
                    bdef.position.set(1040 / LotCG.V_SCALE, 368 /LotCG.V_SCALE);
                }
                break;
            case 9:
                if (nextLevel == true) {
                    bdef.position.set(1040 / LotCG.V_SCALE, 8 / LotCG.V_SCALE);
                }
                else if (nextLevel == false){
                    bdef.position.set(8 / LotCG.V_SCALE, 16 / LotCG.V_SCALE);
                }
                break;
            case 10:
                bdef.position.set(1088 / LotCG.V_SCALE, 16 / LotCG.V_SCALE);
                break;
            case 11:
                bdef.position.set(560 / LotCG.V_SCALE, 2208 / LotCG.V_SCALE);
                break;
            case 12:
                bdef.position.set(560 / LotCG.V_SCALE, 2208 / LotCG.V_SCALE);
                break;
            case 13:
                bdef.position.set(560 / LotCG.V_SCALE, 368 / LotCG.V_SCALE);
                break;
        }
    }


    /*          SETTERS/GETTERS         */
    public State getState() {
        if (isDead) {
            SoundManager.area1Music.stop();
            //SoundManager.area1Music.dispose();
            return State.DEAD;
        }
        if (isLevelUp) {
            return State.LEVELUP;
        }
        else if (isLevelDown) {
            return State.LEVELDOWN;
        }
        else if(grounded == true && (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 ))) {
            return State.JUMPING;
        }
        else if(body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        }
        else {
            return State.STANDING;
        }
    }
    public boolean getFaceRight() {
        return faceRight;
    }
    public boolean getGrounded() {
        return grounded;
    }
    public float getStateTimer() {
        return stateTimer;
    }
    public TextureRegion getFrame(float delta) {
        currentState = getState();
        TextureRegion region;
        switch(currentState) {
            case JUMPING:
                region = heroJump.getKeyFrame(stateTimer);
                break;
            case DEAD:
                region = heroDead.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = heroRun.getKeyFrame(stateTimer, true);
                break;
            case ONPLATFORM:
            case FALLING:
            case STANDING:
            default:
                region = heroIdle;
                break;
        }

        if ((body.getLinearVelocity().x < 0 || !faceRight) && !region.isFlipX()){
            region.flip(true, false);
            faceRight = false;
        }
        else if ((body.getLinearVelocity().x > 0 || faceRight) && region.isFlipX()) {
            region.flip(true, false);
            faceRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;
    }


    /*          HERO VARIABLES          */
    public void dead() {
        isDead = true;
        body.setLinearVelocity(0, -1);
        Filter filter = new Filter();
        filter.maskBits = LotCG.GROUND_BIT;
        for (Fixture fixture : body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
    }
    public void levelUP() {
        isLevelUp = true;
    }
    public void levelDown() {isLevelDown = true; }
    public void isGrounded() {grounded = true; }
    public void isNotGrounded() {grounded = false;}


    /*          UPDATE HERO         */
    public void menuUpdate(float delta) {
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        setRegion(getFrame(delta));
    }
    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        setRegion(getFrame(delta));
    }
}
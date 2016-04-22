package com.neffy.lotcg.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Sprites.Hero;
import com.neffy.lotcg.Sprites.Tiles.InteractiveTiles;

public class WorldContact implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (fixA.getUserData() == "hero" || fixB.getUserData() == "hero") {
            Fixture head = fixA.getUserData() == "hero" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() != null && InteractiveTiles.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTiles) object.getUserData()).onHit();
            }
        }

        switch (cDef) {
            case LotCG.HERO_BIT | LotCG.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == LotCG.HERO_BIT) {
                    SoundManager.blood.play();
                    SoundManager.deathMusic.play();
                    ((Hero) fixA.getUserData()).dead();
                }
                else {
                    SoundManager.blood.play();
                    SoundManager.deathMusic.play();
                    ((Hero) fixB.getUserData()).dead();
                }
                break;
            case LotCG.HERO_BIT | LotCG.LEVELUP_BIT:
                if(fixA.getFilterData().categoryBits == LotCG.HERO_BIT) {
                    ((Hero) fixA.getUserData()).levelUP();
                }
                else {
                    ((Hero) fixB.getUserData()).levelUP();
                }
                break;
            case LotCG.HERO_BIT | LotCG.LEVELDOWN_BIT:
                if(fixA.getFilterData().categoryBits == LotCG.HERO_BIT) {
                    ((Hero) fixA.getUserData()).levelDown();
                }
                else {
                    ((Hero) fixB.getUserData()).levelDown();
                }
                break;
            case LotCG.HERO_BIT | LotCG.PLATFORM_BIT:
                if(fixA.getFilterData().categoryBits == LotCG.HERO_BIT) {

                    ((Hero) fixB.getUserData()).isGrounded();
                }
                else {
                    ((Hero) fixB.getUserData()).isGrounded();
                }
                break;
            case LotCG.HERO_FOOT_BIT | LotCG.GROUND_BIT:
                if(fixA.getFilterData().categoryBits == LotCG.HERO_BIT) {
                    ((Hero) fixB.getUserData()).isGrounded();
                }
                else {
                    ((Hero) fixB.getUserData()).isGrounded();
                }
                break;
            case LotCG.HERO_FOOT_BIT | LotCG.PLATFORM_BIT:
                if(fixA.getFilterData().categoryBits == LotCG.HERO_BIT) {
                    ((Hero) fixB.getUserData()).isGrounded();
                }
                else {
                    ((Hero) fixB.getUserData()).isGrounded();
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

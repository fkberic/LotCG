package com.neffy.lotcg.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected PlayScreen screen;
    protected World world;
    public Body body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y) {
        world = screen.getWorld();
        this.screen = screen;
        setPosition(x + 7/LotCG.V_SCALE, y + 7/ LotCG.V_SCALE);
        defineEnemy();
    }

    protected abstract void defineEnemy();
    public abstract void update(float delta);
}

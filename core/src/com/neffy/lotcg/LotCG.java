package com.neffy.lotcg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.neffy.lotcg.Screens.PlayScreen;
import com.neffy.lotcg.Screens.SplashScreen;
import com.neffy.lotcg.Tools.SoundManager;

public class LotCG extends Game {
	// virtual width + height, scale = pixels per meter
    public static final int V_WIDTH = 600;
    public static final int V_HEIGHT = 384;
    public static final float V_SCALE = 100;

	// collision bits
    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short HERO_BIT = 2;
    public static final short ENEMY_BIT = 4;
    public static final short INV_BIT = 8;
    public static final short REVEALINV_BIT = 16;
	public static final short LEVELUP_BIT = 32;
	public static final short LEVELDOWN_BIT = 64;
	public static final short PLATFORM_BIT = 128;
	public static final short PLATFORM_BOUND_BIT = 256;
	public static final short HERO_FOOT_BIT = 512;
	public static long startTime = System.currentTimeMillis();

	//public static
	public static int level = -99;

	public static boolean paused = false;

	// sprite batch collection
	public SpriteBatch batch;
	@Override
	public void create() {
		batch = new SpriteBatch();
		SoundManager.load();
		setScreen(new SplashScreen(this));
		//setScreen(new PlayScreen(this, true));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}
}

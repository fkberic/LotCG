package com.neffy.lotcg.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.neffy.lotcg.HUD.Control;
import com.neffy.lotcg.HUD.Credits;
import com.neffy.lotcg.HUD.GameOver;
import com.neffy.lotcg.HUD.Quit;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Sprites.Enemies.*;
import com.neffy.lotcg.Sprites.Hero;
import com.neffy.lotcg.Sprites.MapFeatures.Platforms.PlatformBoundary;
import com.neffy.lotcg.Sprites.MapFeatures.Platforms.PlatformHori;
import com.neffy.lotcg.Sprites.MapFeatures.Platforms.PlatformVert;
import com.neffy.lotcg.Sprites.MapFeatures.Platforms.SpikePlatformLeft;
import com.neffy.lotcg.Sprites.MapFeatures.Platforms.SpikePlatformRight;
import com.neffy.lotcg.Tools.SoundManager;
import com.neffy.lotcg.Tools.WorldContact;
import com.neffy.lotcg.Tools.WorldCreator;

public class PlayScreen extends ScreenAdapter implements Screen {
    // references to game + game objects
    private LotCG game;
    private Hero hero;
    private TextureAtlas atlas;

    // camera and viewports
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage hudStage;
    private Viewport hudViewport;
    private Table hudTable;

    // map variables
    private TiledMap map;
    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer mapRenderer;

    // box2D variables
    private World world;
    private WorldCreator worldCreator;
    private Box2DDebugRenderer b2dr;

    private int level = LotCG.level;
    boolean dj = true;
    boolean lp;

    private int quitHold = 0;
    private Control control;
    private GameOver gameOver;
    private Credits credits;
    private Quit quit;

    long startTime = 0;
    long deathTime = 0;

    /*          GAME SETUP          */
    // constructor
    public PlayScreen(LotCG game, boolean levelUp) {
        setupPlayScreen(game, level);
        lp = levelUp;
        SoundManager.area1Music.play();
        hero = new Hero(this, levelUp);

    }

    // setup constructor
    public void setupPlayScreen(LotCG game, int level) {
        this.game = game;
        atlas = new TextureAtlas("LotCGSprite.pack");
        startTime = TimeUtils.nanoTime();
        control = new Control(game.batch);
        gameOver = new GameOver(game.batch);
        credits = new Credits(game.batch);
        quit = new Quit(game.batch);
        loadLevel("Level" + level);

        setCamera();            // setup game camera
        setGravity();           // setup game gravity
        setWorld();             // setup game world
    }

    // handle input
    public void handleInput() {
        // handle input if hero is not dead
        if (hero.currentState != Hero.State.DEAD) {
            androidControl();       // screen touch control
            pcControl();            // keyboard input control
        }
    }

    // game update
    public void update(float delta) {
        handleInput();             // handle user input
        world.step(1 / 60f, 6, 2);      // set fps @ 60f

        updateHero(delta);
        updateEnemy(delta);
        updatePlatform(delta);
        updateCamera();
    }


    /*          SETTERS/GETTERS         */
    // setup game camera
    public void setCamera() {
        /*
            camera height: 0.5 world height
            camera width: 0.5 world width
        */
        camera = new OrthographicCamera();
        viewport = new StretchViewport(LotCG.V_WIDTH / LotCG.V_SCALE / 1.75f, LotCG.V_HEIGHT / LotCG.V_SCALE / 1.75f, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
    }

    // setup game gravity
    public void setGravity() {
        /*
            main level gravity: -10m/s^2
            space level gravity: -4.5m/s^2
         */
        if (level >= 9 && level <= 12) {
            world = new World(new Vector2(0, -4.5f), true);
        }
        else {
            world = new World(new Vector2(0, -10), true);
        }
    }

    // setup game world
    public void setWorld() {
        world.setContactListener(new WorldContact());
        worldCreator = new WorldCreator(this);
        b2dr = new Box2DDebugRenderer();
    }

    // atlas getter
    public TextureAtlas getAtlas() {
        return atlas;
    }

    // map getter
    public TiledMap getMap() {
        return map;
    }

    // world getter
    public World getWorld() {
        return world;
    }


    /*          GAME VARIABLES          */
    public boolean gameOver() {
        if (hero.currentState == Hero.State.DEAD) {
            gameOver.stage.draw();
            return true;

        }
        return false;
    }

    public boolean levelUp() {
        if (hero.currentState == Hero.State.LEVELUP) {
            return true;
        }
        return false;
    }

    public boolean levelDown() {
        if (hero.currentState == Hero.State.LEVELDOWN) {
            return true;
        }
        return false;
    }

    public void loadLevel(String level) {
        // map creation and rendering
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/" + level + ".tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1/LotCG.V_SCALE);
    }


    /*          GAME CONTROL            */
    public void androidControl() {
        boolean jump = false;
        boolean mLeft = false;
        boolean mRight = false;

        final int jumpAreaMax = Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 50;
        final int jumpAreaMin = Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 50 - Gdx.graphics.getWidth() / 10 ;
        final int jumpHeightMax = Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 40 + Gdx.graphics.getWidth() / 10);
        final int leftAreaMin = Gdx.graphics.getWidth() / 50;
        final int leftAreaMax = Gdx.graphics.getWidth() / 10 + leftAreaMin;
        final int leftHeightMax = Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 40 + Gdx.graphics.getWidth() / 10);
        final int rightAreaMin = leftAreaMax + + Gdx.graphics.getWidth() / 40 + Gdx.graphics.getWidth() / 50;
        final int rightAreaMax = Gdx.graphics.getWidth() / 10 + rightAreaMin;
        final int rightHeightMax = Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 40 + Gdx.graphics.getWidth() / 10);


        for (int i = 0; i < 4; i++) {
            if (Gdx.input.isTouched(i)) {
                final int iX = Gdx.input.getX(i);
                final int iY = Gdx.input.getY(i);
                jump = jump || (iX > jumpAreaMin) && (iX < jumpAreaMax) && (iY > jumpHeightMax);
                mRight = mRight || (iX > rightAreaMin) && (iX < rightAreaMax) && (iY > rightHeightMax) && hero.body.getLinearVelocity().x <= 2;
                mLeft = mLeft || (iX > leftAreaMin) && (iX < leftAreaMax) && (iY > leftHeightMax) && hero.body.getLinearVelocity().x >= -2;
            }
        }
        if (jump) {
            switch (level) {
                case 11:
                    break;
                case 12:
                    break;
                default:
                    if (hero.getGrounded() == true) {
                        SoundManager.jump.play(0.3f);
                        startTime = TimeUtils.nanoTime();
                        hero.isNotGrounded();
                        hero.isNotGrounded();
                        dj = false;
                        hero.body.setLinearVelocity(hero.body.getLinearVelocity().x, 3);
                    } else if (!dj && hero.getGrounded() == false && TimeUtils.timeSinceNanos(startTime) > 300000000) {
                        SoundManager.jump.play(0.3f);
                        startTime = 0;
                        dj = true;
                        hero.body.setLinearVelocity(hero.body.getLinearVelocity().x, 2);
                    }
                    break;
            }
        }
        if (mRight) {
            if (hero.getFaceRight() == false) {
                hero.body.applyLinearImpulse(new Vector2(0.5f, -0.1f), hero.body.getWorldCenter(), true);
            }

            hero.body.applyLinearImpulse(new Vector2(0.1f, 0), hero.body.getWorldCenter(), true);
        }
        if (mLeft) {
            if (hero.getFaceRight() == true) {
                hero.body.setLinearVelocity(-0.3f, hero.body.getLinearVelocity().y);
            }
            hero.body.applyLinearImpulse(new Vector2(-0.1f, 0), hero.body.getWorldCenter(), true);
        }
    }

    public void pcControl() {
        // R KEY - Suicide
        if (Gdx.input.isKeyJustPressed((Input.Keys.R))) {
            hero.dead();
        }
        // SPACE KEY - Jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            switch (level) {
                case 11:
                    break;
                case 12:
                    break;
                default:
                    if (hero.getGrounded() == true) {
                        hero.isNotGrounded();
                        dj = false;
                        hero.body.setLinearVelocity(hero.body.getLinearVelocity().x, 3);
                    } else if (!dj && hero.getGrounded() == false) {
                        dj = true;
                        hero.body.setLinearVelocity(hero.body.getLinearVelocity().x, 2);
                    }
                    break;
            }
        }
        // RIGHT KEY - Move Right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && hero.body.getLinearVelocity().x <= 2) {
            if (hero.getFaceRight() == false) {
                hero.body.setLinearVelocity(0.3f, hero.body.getLinearVelocity().y);
            }
            hero.body.applyLinearImpulse(new Vector2(0.1f, 0), hero.body.getWorldCenter(), true);

        }
        // LEFT KEY - MOVE LEFT
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && hero.body.getLinearVelocity().x >= -2) {
            if (hero.getFaceRight() == true) {
                hero.body.setLinearVelocity(-0.3f, hero.body.getLinearVelocity().y);
            }
            hero.body.applyLinearImpulse(new Vector2(-0.1f, 0), hero.body.getWorldCenter(), true);
        }
    }


    /*          DRAW GAME           */
    public void drawHero() {
        hero.draw(game.batch);
    }

    public void drawControlHUD() {
        control.stage.draw();
    }

    public void drawEnemy() {
        for (Enemy enemy : worldCreator.getKillerCoinUp()) {
            enemy.draw(game.batch);
        }
        for (Enemy enemy : worldCreator.getKillerCoinDown()) {
            enemy.draw(game.batch);
        }
        for (Enemy enemy : worldCreator.getAKillerSpikesDown()) {
            enemy.draw(game.batch);
        }
        for (Enemy enemy : worldCreator.getAKillerSpikesUp()) {
            enemy.draw(game.batch);
        }
        for (Enemy enemy : worldCreator.getIKillerSpikesDown()) {
            enemy.draw(game.batch);
        }
        for (Enemy enemy : worldCreator.getIKillerSpikessUp()) {
            enemy.draw(game.batch);
        }
        for (PlatformBoundary platformBoundary : worldCreator.getPlatformBoundary()) {
            platformBoundary.draw(game.batch);
        }
        for (PlatformVert platformVert : worldCreator.getPlatformVert()) {
            platformVert.draw(game.batch);
        }
        for (PlatformHori platformHori : worldCreator.getPlatformHori()) {
            platformHori.draw(game.batch);
        }
        for (SpikePlatformLeft spikePlatformLeft : worldCreator.getSpikePlatformLeft()) {
            spikePlatformLeft.draw(game.batch);
        }
        for (SpikePlatformRight spikePlatformRight : worldCreator.getSpikePlatformRight()) {
            spikePlatformRight.draw(game.batch);
        }
    }


    /*          UPDATE GAME         */
    public void updateHero(float delta){
        hero.update(delta);
    }

    public void updateEnemy(float delta) {
        // killer coin up
        for (Enemy enemy : worldCreator.getKillerCoinUp()) {
            enemy.update(delta);

            if (enemy.getX() > hero.getX() - enemy.getWidth() && enemy.getX() < hero.getX() + enemy.getWidth() && enemy.getY() < hero.getY() && enemy.getY() > hero.getY() - 5 * enemy.getWidth()) {
                enemy.body.setActive(true);
            }
        }

        // killer coin down
        for (Enemy enemy : worldCreator.getKillerCoinDown()) {
            enemy.update(delta);

            if (enemy.getX() > hero.getX() - enemy.getWidth() && enemy.getX() < hero.getX() + enemy.getWidth() && enemy.getY() > hero.getY() && enemy.getY() < hero.getY() + 5 * enemy.getWidth()) {
                enemy.body.setActive(true);
            }
        }

        // active killer spikes up
        for (Enemy enemy : worldCreator.getAKillerSpikesUp()) {
            enemy.update(delta);

            if (enemy.getX() > hero.getX() - enemy.getWidth() && enemy.getX() < hero.getX() + enemy.getWidth() && enemy.getY() > hero.getY() && enemy.getY() < hero.getY() + 3 * enemy.getWidth()) {
                enemy.body.setActive(true);
            }
        }

        // active killer spikes down
        for (Enemy enemy : worldCreator.getAKillerSpikesDown()) {
            enemy.update(delta);

            if (enemy.getX() > hero.getX() - enemy.getWidth() && enemy.getX() < hero.getX() + enemy.getWidth() && enemy.getY() > hero.getY() && enemy.getY() < hero.getY() + 5 * enemy.getWidth()) {
                enemy.body.setActive(true);
            }
        }

        // inactive killer spikes up
        for (Enemy enemy : worldCreator.getIKillerSpikessUp()) {
            enemy.update(delta);

            if (enemy.getX() > hero.getX() - enemy.getWidth() && enemy.getX() < hero.getX() + enemy.getWidth() && enemy.getY() > hero.getY() && enemy.getY() < hero.getY() + 5 * enemy.getWidth()) {
                enemy.body.setActive(true);
            }
        }

        // inactive killer spikes down
        for (Enemy enemy : worldCreator.getIKillerSpikesDown()) {
            enemy.update(delta);

            if (enemy.getX() > hero.getX() - enemy.getWidth() && enemy.getX() < hero.getX() + enemy.getWidth() && enemy.getY() > hero.getY() && enemy.getY() < hero.getY() + 5 * enemy.getWidth()) {
                enemy.body.setActive(true);
            }
        }

        // killer spike platform right
        for (SpikePlatformRight spikePlatformRight : worldCreator.getSpikePlatformRight()) {
            spikePlatformRight.update(delta);
        }

        // killer spike platform left
        for (SpikePlatformLeft spikePlatformLeft : worldCreator.getSpikePlatformLeft()) {
            spikePlatformLeft.update(delta);
        }
    }

    public void updatePlatform(float delta) {
        // vertical platform
        for (PlatformVert platformVert : worldCreator.getPlatformVert()) {
            platformVert.update(delta);
        }

        // horizontal platform
        for (PlatformHori platformHori : worldCreator.getPlatformHori()) {
            platformHori.update(delta);
        }
    }
    public void updateCamera() {
        if (hero.currentState != Hero.State.DEAD) {
            // update camera X
            if (hero.body.getPosition().x > 1.71f && hero.body.getPosition().x < 9.3f) {
                camera.position.x = hero.body.getPosition().x;
            }
            else if (hero.body.getPosition().x <= 1.71f ){
                camera.position.x = 1.71f;
            }
            else if (hero.body.getPosition().x >= 9.3f) {
                camera.position.x = 9.3f;
            }

            // update camera y
            switch (level) {
                case 11:
                    if (hero.body.getPosition().y > 20.9f) {
                        camera.position.y = 20.9f;
                    }
                    else if (hero.body.getPosition().y <= 1.1f) {
                        camera.position.y = 1.1f;
                    }
                    else {
                        camera.position.y = hero.body.getPosition().y;
                    }
                    break;
                case 12:
                    if (hero.body.getPosition().y > 20.9f) {
                        camera.position.y = 20.9f;
                    }
                    else if (hero.body.getPosition().y <= 1.1f) {
                        camera.position.y = 1.1f;
                    }
                    else {
                        camera.position.y = hero.body.getPosition().y;
                    }
                    break;
                default:
                    if (hero.body.getPosition().y > 1.1f && hero.body.getPosition().y < 2.7f) {
                        camera.position.y = hero.body.getPosition().y;
                    } else if (hero.body.getPosition().y <= 1.1f) {
                        camera.position.y = 1.1f;
                    } else if (hero.body.getPosition().y >= 2.7f) {
                        camera.position.y = 2.7f;
                    }
                    break;
            }
        }

        camera.update();                // update camera
        mapRenderer.setView(camera);    // render map
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
            update(delta);
            mapRenderer.render();
            //b2dr.render(world, camera.combined);

            // draw main game
            show();
            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();
            drawEnemy();
            drawHero();
            game.batch.end();

            // draw HUD
            game.batch.setProjectionMatrix(control.stage.getCamera().combined);
            drawControlHUD();

            if (gameOver()) {
                Preferences prefs = Gdx.app.getPreferences("test");

                long timeDelta = System.currentTimeMillis() - LotCG.startTime;
                LotCG.startTime = System.currentTimeMillis();
                prefs.flush();
                deathTime ++;
                if (Gdx.input.isTouched() && deathTime > 150) {
                        SoundManager.blood.stop();
                        SoundManager.deathMusic.stop();
                        game.setScreen(new PlayScreen(game, lp));

                }
            }
            if (levelUp()) {
                if (LotCG.level < 13) {
                    Preferences prefs = Gdx.app.getPreferences("test");
                    prefs.putInteger("level", LotCG.level + 1);
                    prefs.flush();

                    lp = true;
                    LotCG.level++;
                    game.setScreen(new PlayScreen(game, true));
                }
                else if (LotCG.level == 13) {
                    SoundManager.area1Music.stop();
                    LotCG.level = -99;
                    game.setScreen(new SplashScreen(game));
                }
            }
            if (levelDown()) {
                lp = false;
                LotCG.level--;
                game.setScreen (new PlayScreen(game, false));
            }
        if (LotCG.level == 13) {
            credits.stage.draw();
        }

        Gdx.input.setCatchBackKey(true);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            quit.stage.draw();
            quitHold ++;
            if (quitHold > 150) {
                Gdx.app.exit();
                System.exit(0);
            }
        }
        else {
            quitHold = 0;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}

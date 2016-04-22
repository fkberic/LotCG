package com.neffy.lotcg.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Sprites.Hero;
import com.neffy.lotcg.Tools.SoundManager;
import com.neffy.lotcg.Tools.WorldContact;
import com.neffy.lotcg.Tools.WorldCreator;

import java.awt.Dialog;

public class MenuScreen extends ScreenAdapter implements Screen {
    // references to game + game objects
    private LotCG game;
    private Hero hero;
    private TextureAtlas atlas;
    private TextureAtlas menuAtlas;
    private Skin skin;

    // camera and viewports
    private OrthographicCamera camera;
    private Viewport viewport;

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

    private Stage stage;
    private Table table;
    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton settingsButton;

    private BitmapFont white;

    long startTime = 0;

    /*          GAME SETUP          */
    // constructor
    public MenuScreen(LotCG game) {
        setupPlayScreen(game, level);
        lp = true;
        SoundManager.mainMenuMusic.play();
        hero = new Hero(this, lp);
    }

    // setup constructor
    public void setupPlayScreen(LotCG game, int level) {
        this.game = game;
        atlas = new TextureAtlas("LotCGSprite.pack");
        menuAtlas = new TextureAtlas(Gdx.files.internal("menu/menuButton.pack"));
        skin = new Skin(menuAtlas);
        startTime = TimeUtils.nanoTime();
        loadLevel("Level" + level);

        setCamera();            // setup game camera
        setGravity();           // setup game gravity
        setWorld();             // setup game world
    }

    // game update
    public void update(float delta) {
        world.step(1 / 60f, 6, 2);      // set fps @ 60f

        updateHero(delta);
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

    public boolean levelUp() {
        if (hero.currentState == Hero.State.LEVELUP) {
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


    /*          DRAW GAME           */
    public void drawHero() {
        hero.draw(game.batch);
    }


    /*          UPDATE GAME         */
    public void updateHero(float delta){
        hero.body.setLinearVelocity(0.3f, 0);
        hero.menuUpdate(delta);
    }

    public void updateCamera() {
        if (hero.currentState != Hero.State.DEAD) {
            // update camera X
            camera.position.x = hero.body.getPosition().x;


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
        stage = new Stage();
        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        white = new BitmapFont(Gdx.files.internal("font/font.fnt"), false);
        Image title = new Image (new Texture("title.png"));
        title.setWidth(Gdx.graphics.getWidth());
        title.setHeight(Gdx.graphics.getHeight() / 2);
        title.setPosition(Gdx.graphics.getWidth() / 2 - title.getWidth() / 2, Gdx.graphics.getHeight()/1.7f);


        TextButton.TextButtonStyle newGameButtonStyle = new TextButton.TextButtonStyle();
        final TextButton.TextButtonStyle loadGameButtonStyle = new TextButton.TextButtonStyle();
        TextButton.TextButtonStyle settingButtonStyle = new TextButton.TextButtonStyle();
        newGameButtonStyle.font = white;
        newGameButtonStyle.fontColor = Color.BLACK;
        newGameButtonStyle.up = skin.getDrawable("newGameButton");
        loadGameButtonStyle.font = white;
        loadGameButtonStyle.fontColor = Color.BLACK;
        loadGameButtonStyle.up = skin.getDrawable("loadGameButton");
        settingButtonStyle.font = white;
        settingButtonStyle.fontColor = Color.BLACK;
        settingButtonStyle.up = skin.getDrawable("settingsButton");
        newGameButton = new TextButton ("", newGameButtonStyle);
        newGameButton.setWidth(Gdx.graphics.getWidth()/2.5f);
        newGameButton.setHeight(Gdx.graphics.getHeight()/6);
        newGameButton.setPosition(Gdx.graphics.getWidth()/2 - newGameButton.getWidth()/2, Gdx.graphics.getHeight() / 1.75f);
        loadGameButton = new TextButton ("", loadGameButtonStyle);
        loadGameButton.setWidth(Gdx.graphics.getWidth()/2.5f);
        loadGameButton.setHeight(Gdx.graphics.getHeight()/6);
        loadGameButton.setPosition(Gdx.graphics.getWidth()/2 - loadGameButton.getWidth()/2, Gdx.graphics.getHeight() /2.5f);
        settingsButton = new TextButton ("", settingButtonStyle);

        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LotCG.level = 0;
                newGameButton.clearListeners();
                loadGameButton.clearListeners();
                Preferences prefs = Gdx.app.getPreferences("test");
                prefs.putInteger("deaths", 1);
                prefs.putInteger("secTime", 0);
                prefs.flush();
                SoundManager.mainMenuMusic.stop();
                game.setScreen(new PlayScreen(game, true));
            }
        });

        loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Preferences prefs = Gdx.app.getPreferences("test");
                int deathCounter = prefs.getInteger("deaths") + 1;
                int timeCounter = prefs.getInteger("time");
                prefs.putInteger("deaths", deathCounter);
                prefs.putLong("secTime", timeCounter);
                prefs.flush();
                LotCG.level = prefs.getInteger("level");
                newGameButton.clearListeners();
                loadGameButton.clearListeners();
                SoundManager.mainMenuMusic.stop();
                game.setScreen(new PlayScreen(game, true));
            }
        });
        //table.setHeight(LotCG.V_HEIGHT);
        table.addActor(title);
        table.addActor(newGameButton);
     //   //table.add(newGameButton).height(Gdx.graphics.getHeight() / 5).width(Gdx.graphics.getWidth() / 2).padTop(Gdx.graphics.getHeight()/10);
        //table.row();
       table.addActor(loadGameButton);
       // table.add(loadGameButton).height(Gdx.graphics.getHeight() / 5).width(Gdx.graphics.getWidth() / 2).padTop(10);
     //   table.row();
       // table.add(settingsButton).height(Gdx.graphics.getHeight() / 5).width(Gdx.graphics.getWidth()/2).padTop(10);
       // table.top();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        update(delta);
        mapRenderer.render();
        b2dr.render(world, camera.combined);

        // draw main game
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        drawHero();
        game.batch.end();

        // draw HUD
        Gdx.input.setInputProcessor(stage);
        stage.act(delta);
        stage.draw();

        if (levelUp()) {
            lp = true;
            game.setScreen(new MenuScreen(game));
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
    public void dispose() {
        stage.dispose();
    }
}

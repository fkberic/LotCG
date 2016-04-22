package com.neffy.lotcg.Tools;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Screens.MenuScreen;
import com.neffy.lotcg.Screens.PlayScreen;
import com.neffy.lotcg.Sprites.Enemies.KillerCoinDown;
import com.neffy.lotcg.Sprites.Enemies.KillerCoinUp;
import com.neffy.lotcg.Sprites.Enemies.KillerWater;
import com.neffy.lotcg.Sprites.Enemies.aKillerSpikesDown;
import com.neffy.lotcg.Sprites.Enemies.aKillerSpikesUp;
import com.neffy.lotcg.Sprites.Enemies.iKillerSpikesDown;
import com.neffy.lotcg.Sprites.Enemies.iKillerSpikesUp;
import com.neffy.lotcg.Sprites.LevelObject.LevelDown;
import com.neffy.lotcg.Sprites.LevelObject.LevelUp;
import com.neffy.lotcg.Sprites.MapFeatures.Platforms.PlatformBoundary;
import com.neffy.lotcg.Sprites.MapFeatures.Platforms.PlatformHori;
import com.neffy.lotcg.Sprites.MapFeatures.Platforms.PlatformVert;
import com.neffy.lotcg.Sprites.MapFeatures.Platforms.SpikePlatformLeft;
import com.neffy.lotcg.Sprites.MapFeatures.Platforms.SpikePlatformRight;
import com.neffy.lotcg.Sprites.Tiles.InvisibleBlocks;

public class WorldCreator {
    private Array<KillerCoinUp> killerCoinUp;
    private Array<KillerCoinDown> killerCoinDown;
    private Array<KillerWater> killerWater;
    private Array<LevelUp> levelUp;
    private Array<LevelDown> levelDown;
    private Array<PlatformVert> platformVert;
    private Array<PlatformHori> platformHori;
    private Array<PlatformBoundary> platformBoundary;
    private Array<SpikePlatformLeft> spikePlatformLeft;
    private Array<SpikePlatformRight> spikePlatformRight;
    private Array<aKillerSpikesDown> aKillerSpikesDown;
    private Array<aKillerSpikesUp> aKillerSpikesUp;
    private Array<iKillerSpikesDown> iKillerSpikesDown;
    private Array<iKillerSpikesUp> iKillerSpikesUp;

    // constructor
    public WorldCreator(PlayScreen screen) {
        TiledMap map = screen.getMap();
        createMap(map, screen);
        createEnemy(map, screen);
        createPlatform(map, screen);
    }

    // overloaded constructor
    public WorldCreator(MenuScreen screen) {
        TiledMap map = screen.getMap();
        createMap(map, screen);
    }

    /*          GETTERS         */
    public Array<KillerCoinUp> getKillerCoinUp() {return killerCoinUp; }
    public Array<KillerCoinDown> getKillerCoinDown() { return killerCoinDown; }
    public Array<PlatformBoundary> getPlatformBoundary() {return platformBoundary; }
    public Array<PlatformVert> getPlatformVert() { return platformVert; }
    public Array<PlatformHori> getPlatformHori() { return platformHori; }
    public Array<SpikePlatformLeft> getSpikePlatformLeft() { return spikePlatformLeft; }
    public Array<SpikePlatformRight> getSpikePlatformRight() { return spikePlatformRight; }
    public Array<aKillerSpikesDown> getAKillerSpikesDown() { return aKillerSpikesDown; }
    public Array<aKillerSpikesUp> getAKillerSpikesUp() { return aKillerSpikesUp; }
    public Array<iKillerSpikesDown> getIKillerSpikesDown() { return iKillerSpikesDown; }
    public Array<iKillerSpikesUp> getIKillerSpikessUp() { return iKillerSpikesUp; }


    /*          CREATOR         */
    // create map features
    public void createMap (TiledMap map, PlayScreen screen) {
        createGround(map, screen);
        createBoundary(map, screen);
        createLevelUp(map, screen);
        createLevelDown(map, screen);
    }

    public void createMap (TiledMap map, MenuScreen screen) {
        createGround(map, screen);
        createLevelUp(map, screen);
    }

    // create enemy
    public void createEnemy(TiledMap map, PlayScreen screen) {
        createKCD(map, screen);
        createKCU(map, screen);
        createKW(map, screen);
        createSpikePlatformLeft(map, screen);
        createSpikePlatformRight(map, screen);
        createAKSD(map, screen);
        createAKSU(map, screen);
        createIKSD(map, screen);
        createIKSU(map, screen);
    }

    // create platform
    public void createPlatform(TiledMap map, PlayScreen screen) {
        createPlatformBoundary(map, screen);
        createPlatformVert(map, screen);
        createPlatformHori(map, screen);
    }

    public void createGround (TiledMap map, PlayScreen screen) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        World world = screen.getWorld();

        MapLayer ml = map.getLayers().get("Inanimate");
        if (ml == null) return;
        for(MapObject object : ml.getObjects()){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2)/ LotCG.V_SCALE, (rect.getY() + rect.getHeight()/2)/LotCG.V_SCALE);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2/LotCG.V_SCALE, rect.getHeight() / 2/LotCG.V_SCALE);
            fdef.filter.categoryBits = LotCG.GROUND_BIT;
            fdef.filter.maskBits = LotCG.HERO_FOOT_BIT | LotCG.HERO_BIT;
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }
    public void createGround (TiledMap map, MenuScreen screen) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        World world = screen.getWorld();

        MapLayer ml = map.getLayers().get("Inanimate");
        if (ml == null) return;
        for(MapObject object : ml.getObjects()){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2)/ LotCG.V_SCALE, (rect.getY() + rect.getHeight()/2)/LotCG.V_SCALE);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2/LotCG.V_SCALE, rect.getHeight() / 2/LotCG.V_SCALE);
            fdef.filter.categoryBits = LotCG.GROUND_BIT;
            fdef.filter.maskBits = LotCG.HERO_FOOT_BIT | LotCG.HERO_BIT;
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }

    public void createKCU (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("KillerCoinUp");
        killerCoinUp = new Array<KillerCoinUp>();
        if (ml == null) return;

        for (MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            killerCoinUp.add(new KillerCoinUp(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createKCD (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("KillerCoinDown");
        killerCoinDown = new Array<KillerCoinDown>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            killerCoinDown.add(new KillerCoinDown(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createAKSD (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("aKillerSpikesDown");
        aKillerSpikesDown = new Array<aKillerSpikesDown>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            aKillerSpikesDown.add(new aKillerSpikesDown(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createAKSU (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("aKillerSpikesUp");
        aKillerSpikesUp = new Array<aKillerSpikesUp>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            aKillerSpikesUp.add(new aKillerSpikesUp(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createIKSD (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("iKillerSpikesDown");
        iKillerSpikesDown = new Array<iKillerSpikesDown>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            iKillerSpikesDown.add(new iKillerSpikesDown(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createIKSU (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("iKillerSpikesUp");
        iKillerSpikesUp = new Array<iKillerSpikesUp>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            iKillerSpikesUp.add(new iKillerSpikesUp(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createKW (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("KillerWater");
        killerWater = new Array<KillerWater>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            killerWater.add(new KillerWater(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createLevelUp (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("NextLevel");
        levelUp = new Array<LevelUp>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            levelUp.add(new LevelUp(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createLevelUp (TiledMap map, MenuScreen screen) {
        MapLayer ml = map.getLayers().get("NextLevel");
        levelUp = new Array<LevelUp>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            levelUp.add(new LevelUp(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }

    public void createLevelDown (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("PreviousLevel");
        levelDown = new Array<LevelDown>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            levelDown.add(new LevelDown(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createPlatformBoundary (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("PlatformBoundary");
        platformBoundary = new Array<PlatformBoundary>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            platformBoundary.add(new PlatformBoundary(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createPlatformVert (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("PlatformVert");
        platformVert = new Array<PlatformVert>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            platformVert.add(new PlatformVert(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createPlatformHori (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("PlatformHori");
        platformHori = new Array<PlatformHori>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            platformHori.add(new PlatformHori(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createSpikePlatformLeft (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("SpikePlatformLeft");
        spikePlatformLeft = new Array<SpikePlatformLeft>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            spikePlatformLeft.add(new SpikePlatformLeft(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createSpikePlatformRight (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("SpikePlatformRight");
        spikePlatformRight = new Array<SpikePlatformRight>();
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            spikePlatformRight.add(new SpikePlatformRight(screen, rect.getX() / LotCG.V_SCALE, rect.getY() / LotCG.V_SCALE));
        }
    }
    public void createBoundary (TiledMap map, PlayScreen screen) {
        MapLayer ml = map.getLayers().get("Boundary");
        if (ml == null) return;

        for(MapObject object : ml.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new InvisibleBlocks(screen, rect);
        }
    }
}
package com.neffy.lotcg.Sprites.Tiles;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Screens.PlayScreen;

public abstract class InteractiveTiles {
    protected World world;
    protected Body body;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Fixture fixture;

    public InteractiveTiles(PlayScreen screen, Rectangle bounds) {
        world = screen.getWorld();
        map = screen.getMap();
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2)/ LotCG.V_SCALE, (bounds.getY() + bounds.getHeight() / 2)/LotCG.V_SCALE);
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bounds.getWidth() / 2/LotCG.V_SCALE, bounds.getHeight() / 2/LotCG.V_SCALE);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    public abstract void onHit();

    //  cell getter
    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * LotCG.V_SCALE / 16), (int)(body.getPosition().y * LotCG.V_SCALE / 16));
    }

    public void setCatagoryFilter(short bits) {
        Filter filter = new Filter();
        filter.categoryBits = bits;
        fixture.setFilterData(filter);
    }
}

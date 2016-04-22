package com.neffy.lotcg.Sprites.Tiles;

import com.badlogic.gdx.math.Rectangle;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Screens.PlayScreen;

public class InvisibleBlocks extends InteractiveTiles{
    public InvisibleBlocks(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCatagoryFilter(LotCG.INV_BIT);
    }

    @Override
    public void onHit() {
        setCatagoryFilter(LotCG.REVEALINV_BIT);
        getCell().setTile(null);
    }
}

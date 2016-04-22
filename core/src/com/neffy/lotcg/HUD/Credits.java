package com.neffy.lotcg.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.neffy.lotcg.LotCG;

public class Credits extends ScreenAdapter{
    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
    }

    public Stage stage;
    private Viewport viewPort;

    Image leftArrow;

    public Credits(SpriteBatch sb) {
        Table table = new Table();
        viewPort = new FitViewport (Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewPort, sb);

        Image credits = new Image(new Texture("endingCredits.png"));
        credits.setWidth(Gdx.graphics.getWidth() / 1.5f);
        credits.setHeight(Gdx.graphics.getHeight() / 2);
        credits.setPosition(Gdx.graphics.getWidth()/2 - credits.getWidth()/2, Gdx.graphics.getHeight() / 2f);

        Image rightArrow = new Image(new Texture("title2.png"));
        rightArrow.setWidth(Gdx.graphics.getWidth() / 1.5f);
        rightArrow.setHeight(Gdx.graphics.getHeight() / 2);
        rightArrow.setPosition(Gdx.graphics.getWidth()/2 - rightArrow.getWidth()/2, Gdx.graphics.getHeight() / 3.5f);

        table.addActor(credits);
        table.addActor(rightArrow);
        stage.addActor(table);
    }
}

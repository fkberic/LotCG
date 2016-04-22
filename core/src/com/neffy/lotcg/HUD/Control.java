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

public class Control extends ScreenAdapter{
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

    public Control(SpriteBatch sb) {
        Table table = new Table();
        viewPort = new FitViewport (Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewPort, sb);

        Image leftArrow = new Image(new Texture("arrowL.png"));
        leftArrow.setWidth(Gdx.graphics.getWidth() / 10);
        leftArrow.setHeight(Gdx.graphics.getHeight() / 10);
        leftArrow.setPosition(Gdx.graphics.getWidth() / 50, Gdx.graphics.getHeight() / 40);

        Image rightArrow = new Image(new Texture("arrowR.png"));
        rightArrow.setWidth(Gdx.graphics.getWidth() / 10);
        rightArrow.setHeight(Gdx.graphics.getHeight() / 10);
        rightArrow.setPosition(leftArrow.getWidth() + Gdx.graphics.getWidth() / 50 + Gdx.graphics.getWidth() / 20, Gdx.graphics.getHeight() / 40);

        Image jump = new Image(new Texture("jump.png"));
        jump.setWidth(Gdx.graphics.getWidth()/10);
        jump.setHeight(Gdx.graphics.getWidth() / 10);
        jump.setPosition(Gdx.graphics.getWidth() - jump.getWidth() - Gdx.graphics.getWidth() / 50, Gdx.graphics.getHeight()/40);

        table.addActor(leftArrow);
        table.addActor(rightArrow);
        table.addActor(jump);
        stage.addActor(table);
    }
}

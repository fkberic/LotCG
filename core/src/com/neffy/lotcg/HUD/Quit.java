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
import com.sun.prism.image.ViewPort;

public class Quit extends ScreenAdapter{
    public Stage stage;

    public Quit(SpriteBatch sb) {
        Table table = new Table();
        FitViewport viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewPort, sb);

        Image quit = new Image(new Texture("quit.png"));
        quit.setWidth(Gdx.graphics.getWidth()/2);
        quit.setHeight(Gdx.graphics.getHeight() / 4);
        quit.setPosition(Gdx.graphics.getWidth() / 2 - quit.getWidth() / 2, Gdx.graphics.getHeight() / 2 - quit.getHeight() / 2);
        table.addActor(quit);
        stage.addActor(table);
    }

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
        super.dispose();
    }
}

package com.neffy.lotcg.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Tools.SoundManager;

public class GameOver extends ScreenAdapter{
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

    Label numDeaths;
    Label secLabel;

    public GameOver(SpriteBatch sb) {
        Table table = new Table();
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewPort, sb);

        Preferences prefs = Gdx.app.getPreferences("test");

        Image gameOver = new Image (new Texture("gameOver.png"));
        gameOver.setWidth(Gdx.graphics.getWidth() / 1.5f);
        gameOver.setHeight(Gdx.graphics.getHeight() / 4);
        gameOver.setPosition(Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 3);

        Image cont = new Image (new Texture("continue.png"));
        cont.setWidth(Gdx.graphics.getWidth() / 1.5f);
        cont.setHeight(Gdx.graphics.getHeight() / 4);
        cont.setPosition(Gdx.graphics.getWidth() / 2 - cont.getWidth() / 2, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 1.5f);

        table.addActor(gameOver);
        table.addActor(cont);
        stage.addActor(table);
    }
}

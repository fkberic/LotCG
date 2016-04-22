package com.neffy.lotcg.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.neffy.lotcg.LotCG;
import com.neffy.lotcg.Tools.SoundManager;

public class SplashScreen extends ScreenAdapter implements Screen {
    private LotCG game;
    private SpriteBatch batch;
    private Sprite splashImage;

    private long startTime = 0;

    public SplashScreen(LotCG game) {
        this.game = game;
        SoundManager.splashMusic.play();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        Texture splashTexture = new Texture(Gdx.files.internal("splash/PureGamesLogo.png"));
        splashImage = new Sprite(splashTexture);
       // splashImage.setPosition(Gdx.graphics.getWidth()/2 - splashTexture.getWidth()/2, Gdx.graphics.getHeight()/2 - splashTexture.getHeight()/2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        splashImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        batch.begin();
        batch.setColor(splashImage.getColor());

        batch.draw(
                splashImage,
                0,
                Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 10,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight() / 3
        );

        batch.end();
        startTime = startTime + 1;

        if (startTime > 175) {
            game.setScreen(new MenuScreen(game));
        }

    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

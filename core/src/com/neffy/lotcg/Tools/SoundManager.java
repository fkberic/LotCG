package com.neffy.lotcg.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    public static Music area1Music;
    public static Music deathMusic;
    public static Music mainMenuMusic;
    public static Sound splashMusic;
    public static Sound blood;
    public static Sound bump;
    public static Sound jump;
    public static Sound walk;

    public SoundManager() {}

    public static void load() {
        area1Music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/Area1.mp3"));
        area1Music.setLooping(true);
        deathMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/Death.mp3"));
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/MainMenu.mp3"));
        mainMenuMusic.setLooping(true);
        splashMusic = Gdx.audio.newSound(Gdx.files.internal("audio/music/Splash.mp3"));

        blood = Gdx.audio.newSound(Gdx.files.internal("audio/blood_fx2.mp3"));
        bump = Gdx.audio.newSound(Gdx.files.internal("audio/bump_fx.mp3"));
        jump = Gdx.audio.newSound(Gdx.files.internal("audio/jump_fx.mp3"));
        walk = Gdx.audio.newSound(Gdx.files.internal("audio/walk_fx.mp3"));
    }
}

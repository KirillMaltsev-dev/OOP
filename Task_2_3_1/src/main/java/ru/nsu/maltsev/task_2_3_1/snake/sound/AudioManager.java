package ru.nsu.maltsev.task_2_3_1.snake.sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class AudioManager {
    private static final String MENU_MUSIC = "/ru/nsu/maltsev/task_2_3_1/snake/audio/menu.mp3";
    private static final String GAME_MUSIC = "/ru/nsu/maltsev/task_2_3_1/snake/audio/game.mp3";
    private static final String DEATH_SOUND = "/ru/nsu/maltsev/task_2_3_1/snake/audio/death.wav";
    private static final String EAT_SOUND = "/ru/nsu/maltsev/task_2_3_1/snake/audio/eat.wav";
    private static final String WIN_MUSIC = "/ru/nsu/maltsev/task_2_3_1/snake/audio/win.mp3";
    private static final String LOSE_MUSIC = "/ru/nsu/maltsev/task_2_3_1/snake/audio/lose.mp3";

    private MediaPlayer currentMusic;
    private AudioClip deathSound;
    private AudioClip eatSound;
    private String currentMusicPath;

    public AudioManager() {
        deathSound = loadAudioClip(DEATH_SOUND);
        eatSound = loadAudioClip(EAT_SOUND);
    }

    public void playMenuMusic() {
        playLoop(MENU_MUSIC, 0.35, 1.0);
    }

    public void playGameMusic() {
        playLoop(GAME_MUSIC, 0.30, 1.0);
    }

    public void playWinMusic() {
        playOnce(WIN_MUSIC, 0.8);
    }

    public void playLoseMusic() {
        playOnce(LOSE_MUSIC, 0.8);
    }

    public void playDeathSound() {
        if (deathSound != null) {
            deathSound.play(0.5);
        }
    }

    public void playEatSound() {
        if (eatSound != null) {
            eatSound.play(0.3);
        }
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
            currentMusicPath = null;
        }
    }

    private void playLoop(String path, double volume, double rate) {
        if (path.equals(currentMusicPath)) {
            return;
        }

        stopMusic();

        currentMusic = loadMediaPlayer(path);
        currentMusicPath = path;

        if (currentMusic != null) {
            currentMusic.setCycleCount(MediaPlayer.INDEFINITE);
            currentMusic.setVolume(volume);
            currentMusic.setRate(rate);
            currentMusic.play();
        }
    }

    private void playOnce(String path, double volume) {
        stopMusic();

        currentMusic = loadMediaPlayer(path);
        currentMusicPath = path;

        if (currentMusic != null) {
            currentMusic.setCycleCount(1);
            currentMusic.setVolume(volume);
            currentMusic.play();
        }
    }

    private MediaPlayer loadMediaPlayer(String path) {
        URL resource = getClass().getResource(path);

        if (resource == null) {
            return null;
        }

        try {
            return new MediaPlayer(new Media(resource.toExternalForm()));
        } catch (RuntimeException exception) {
            return null;
        }
    }

    private AudioClip loadAudioClip(String path) {
        URL resource = getClass().getResource(path);

        if (resource == null) {
            return null;
        }

        try {
            return new AudioClip(resource.toExternalForm());
        } catch (RuntimeException exception) {
            return null;
        }
    }
}
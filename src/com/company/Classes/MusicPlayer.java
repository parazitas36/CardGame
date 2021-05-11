package com.company.Classes;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;
import java.net.URL;

public class MusicPlayer {
    // Reads audio files
    public AudioInputStream getAudio(String path) {
        AudioInputStream audioInput = null;
        try {
            File song = new File(path);
            if (song.exists()) {
                audioInput = AudioSystem.getAudioInputStream(song);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audioInput;
    }

    // Background music
    public Clip playMusic(AudioInputStream audio) {
        Clip clip = null;
        try (audio) {
            if (audio != null) {
                clip = AudioSystem.getClip();
                clip.open(audio);
                return clip;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }

    // Sound effects
    public Clip playSound(AudioInputStream audio) {
        Clip clip = null;
        try {
            if (audio != null) {
                clip = AudioSystem.getClip();
                clip.open(audio);
                return clip;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }
    public void repeatMusic(Clip clip) {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void repeatSound(Clip clip) {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }
}

package com.company.Classes;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;
import java.net.URL;

public class MusicPlayer{

    public void playMusic(String musicLocation) {
        try {
            File songPath = new File(musicLocation);
            if(songPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(songPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(0);
                JOptionPane.showMessageDialog(null, "Baigti leidimą");
            }
            else {
                System.out.println("File not found");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


//        try {
//            URL url = this.getClass().getClassLoader().getResource("PugaciovaIrReperis(sutrumpinta).wav");
//            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
//
//            Clip clip = AudioSystem.getClip();
//            clip.open(audioIn);
//            clip.start();
//        }
//        catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (LineUnavailableException e) {
//            e.printStackTrace();
//        } catch (UnsupportedAudioFileException e) {
//            e.printStackTrace();
//        }

    }

}

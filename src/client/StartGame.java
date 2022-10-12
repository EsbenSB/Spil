package client;

import client.gui.Window;
import javafx.application.Application;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class StartGame {
  public static void main(String[] args) {
    // Start audio track
    try {
      String filePath = "src/client/audio/8bit.wav";
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
      Clip clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      clip.loop(Clip.LOOP_CONTINUOUSLY);
      clip.start();
    } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
      throw new RuntimeException(e);
    }

    Application.launch(Window.class);
  }
}

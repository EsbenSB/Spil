package client;

import AudioPlayer.SimpleAudioPlayer;
import client.gui.Window;
import javafx.application.Application;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class StartGame {
  public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
    try
    {
      SimpleAudioPlayer audioPlayer =
              new SimpleAudioPlayer();
      audioPlayer.play();

    }
    catch (Exception ex)
    {
      System.out.println("Error with playing sound.");
      ex.printStackTrace();

    }
    Application.launch(Window.class);

  }
}

package server.components;

import client.Player;

import java.util.ArrayList;

public class GameServer {
  private final String id;
  private final String name;
  private final ArrayList<Player> players = new ArrayList<>();

  public GameServer(String id, String name) {
    this.id = id;
    this.name = name;
  }

  // TODO: A LOT

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}

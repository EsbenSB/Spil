package utils;

import server.network.CommunicationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class PackageService {
  public static HashMap<String, String> constructGridData(int[][] grid) {
    String[][] stringMaze = new String[grid.length][grid[0].length];

    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[0].length; col++) {
        stringMaze[row][col] = Integer.toString(grid[row][col]);
      }
    }

    StringBuilder sb = new StringBuilder();
    ArrayList<String> mazeColArray = new ArrayList<>();

    for (String[] row : stringMaze) {
      mazeColArray.add(String.join(",", row));
    }
    sb.append(String.join(":", mazeColArray));

    HashMap<String, String> gridData = new HashMap<>();
    gridData.put("task", "start_server");
    gridData.put("grid", sb.toString());
    gridData.put("width", Integer.toString(grid[0].length));
    gridData.put("height", Integer.toString(grid.length));

    return gridData;
  }

  public static int[][] deconstructGridData(HashMap<String, String> gridData) {
    int width = Integer.parseInt(gridData.get("width"));
    int height = Integer.parseInt(gridData.get("height"));
    String gridString = gridData.get("grid");
    int[][] grid = new int[height][width];
    String[] rowSplit = gridString.split(":");

    for (int row = 0; row < rowSplit.length; row++) {
      String[] colSplit = rowSplit[row].split(",");

      for (int col = 0; col < colSplit.length; col++) {
        grid[row][col] = Integer.parseInt(colSplit[col]);
      }
    }

    return grid;
  }

  public static HashMap<String, String> constructClientsData(String clientID, ArrayList<CommunicationService> clients) {
    StringBuilder sb = new StringBuilder();
    ArrayList<String> clientStrings = new ArrayList<>();
    for (CommunicationService client : clients) {
      clientStrings.add(String.format("%s,%s", client.getClientID(), client.getClientName()));
    }
    sb.append(String.join(":", clientStrings));

    HashMap<String, String> data = new HashMap<>();
    data.put("task", "join_server");
    data.put("client_id", clientID);
    data.put("clients", sb.toString());

    return data;
  }

  public static ArrayList<HashMap<String, String>> deconstructClientsString(String clientsString) {
    ArrayList<HashMap<String, String>> clients = new ArrayList<>();
    String[] clientParts = clientsString.split(":");

    for (String clientPart : clientParts) {
      String[] clientSplit = clientPart.split(",");
      String clientID  = clientSplit[0];
      String clientName = clientSplit[1];

      HashMap<String, String> client = new HashMap<>();
      client.put("client_id", clientID);
      client.put("client_name", clientName);
      clients.add(client);
    }

    return clients;
  }

  public static String constructQuery(HashMap<String, String> data) {
    StringBuilder sb = new StringBuilder();

    for (Map.Entry<String, String> entry : data.entrySet()) {
      String attribute = String.format("%s=%s", entry.getKey(), entry.getValue());
      sb.append(attribute).append("&");
    }

    return sb.substring(0, sb.length() - 1);
  }

  public static HashMap<String, String> deconstructQuery(String query) {
    HashMap<String, String> data = new HashMap<>();
    String[] attributes = query.split("&");

    for (String attribute : attributes) {
      String[] split = attribute.split("=");
      data.put(split[0], split[1]);
    }

    return data;
  }
}

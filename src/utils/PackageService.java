package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class PackageService {
  public static HashMap<String, String> constructGridMap(int[][] grid) {
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
    gridData.put("width", Integer.toString(grid.length));
    gridData.put("height", Integer.toString(grid[0].length));

    return gridData;
  }

  public static int[][] deconstructGridMap(HashMap<String, String> data) {
    int width = Integer.parseInt(data.get("width"));
    int height = Integer.parseInt(data.get("height"));
    String gridString = data.get("grid");
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

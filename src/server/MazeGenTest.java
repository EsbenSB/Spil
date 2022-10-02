package server;

import server.components.MazeGenerator;
import utils.PackageService;

import java.util.Arrays;
import java.util.HashMap;

public class MazeGenTest {

  public static void main(String[] args) {
    MazeGenerator mazeGenerator = new MazeGenerator(10, 10);
    int[][] maze = mazeGenerator.getMaze();
    System.out.println("Maze:                " + Arrays.deepToString(maze));

    int[][] grid = mazeGenerator.getGrid();
    System.out.println("Grid on server:      " + Arrays.deepToString(grid));

    HashMap<String, String> gridData = PackageService.constructGridData(grid);
    System.out.println("Grid map on Server:  " + gridData);

    String query = PackageService.constructQuery(gridData);
    System.out.println("Query:               " + query);

    HashMap<String, String> data = PackageService.deconstructQuery(query);
    System.out.println("Grid map on Client:  " + data);

    int[][] receivedGrid = PackageService.deconstructGridData(data);
    System.out.println("Grid on Client:      " + Arrays.deepToString(receivedGrid));

    System.out.println();
    mazeGenerator.prettyPrint();
  }

}

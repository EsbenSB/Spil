package server.components;

import java.util.Collections;
import java.util.Arrays;

public class MazeGenerator {
  private final int x;
  private final int y;
  private final int[][] maze;
  private final int[][] grid;
  private int max;
  private int endx, endy;

  public MazeGenerator(int x, int y) {
    this.x = x;
    this.y = y;
    maze = new int[this.x][this.y];
    generateMaze(0, 0, 0);
    grid = new int[y*2 + 1][x*2 + 1];
    generateGrid();
  }

  public int[][] getMaze() {
    return Arrays.copyOf(maze, maze.length);
  }
  public int[][] getGrid() {
    return Arrays.copyOf(grid, grid.length);
  }

  private void generateMaze(int cx, int cy, int max) {
    if (max > this.max) {
      this.max = max;
      this.endx = cx;
      this.endy = cy;
    }
    DIR[] dirs = DIR.values();
    Collections.shuffle(Arrays.asList(dirs));
    for (DIR dir : dirs) {
      int nx = cx + dir.dx;
      int ny = cy + dir.dy;
      if (between(nx, x) && between(ny, y)
              && (maze[nx][ny] == 0)) {
        maze[cx][cy] |= dir.bit;
        maze[nx][ny] |= dir.opposite.bit;
        generateMaze(nx, ny, max + 1);
      }
    }
  }

  private static boolean between(int v, int upper) {
    return (v >= 0) && (v < upper);
  }

  private enum DIR {
    N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
    private final int bit;
    private final int dx;
    private final int dy;
    private DIR opposite;

    // use the static initializer to resolve forward references
    static {
      N.opposite = S;
      S.opposite = N;
      E.opposite = W;
      W.opposite = E;
    }

    DIR(int bit, int dx, int dy) {
      this.bit = bit;
      this.dx = dx;
      this.dy = dy;
    }
  }

  private void generateGrid() {
    // CORNERS
    grid[0  ][0  ] = 0;
    grid[y*2][0  ] = 0;
    grid[y*2][x*2] = 0;
    grid[0  ][x*2] = 0;

    // CELLS
    for (int row = 0; row < y; row++) {
      for (int col = 0; col < x; col++) {
        // UP
        grid[row*2    ][col*2 + 1] = (maze[col][row] & 1) == 0 ? 0 : -1;
        // CENTER
        grid[row*2 + 1][col*2 + 1] = -1;
        // LEFT
        grid[row*2 + 1][col*2    ] = (maze[col][row] & 8) == 0 ? 0 : -1;
      }
      // MOST RIGHT WALL
      grid[row*2 + 1][x*2] = 0;
    }

    // BOTTOM WALLS
    for (int col = 1; col < x*2; col++) {
      grid[y*2][col] = 0;
    }

    // START
    grid[1][1] = -3;
    // END
    grid[endy*2 + 1][endx*2 + 1] = -2;

    generatePowerups();
  }

  private void generatePowerups() {
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[0].length; col++) {
        if (grid[row][col] != -1) continue;

        double percentage = .0667;
        boolean isPowerup = Math.random() <= percentage;
        if (!isPowerup) continue;

        double chance = Math.random();
        if      (chance < .2500) grid[row][col] = 2;  // Trap
        else if (chance < .5000) grid[row][col] = 7;  // Shield
        else if (chance < .6250) grid[row][col] = 5;  // Speed boost
        else if (chance < .7500) grid[row][col] = 6;  // Gun
        else if (chance < .8750) grid[row][col] = 9;  // Bomb
        else if (chance < .9325) grid[row][col] = 3;  // Super star
        else if (chance < .9900) grid[row][col] = 8;  // Demon
        else                     grid[row][col] = 4;  // Pickaxe
      }
    }
  }

  public void prettyPrint() {
    for (int[] row : grid) {
      for (int col : row) {
        System.out.printf("%2d ", col);
      }
      System.out.println();
    }
  }
}

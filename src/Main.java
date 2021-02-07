import java.io.InputStreamReader;
import java.util.Scanner;

import mazes.controller.Controller;
import mazes.controller.ControllerG;
import mazes.model.Maze;
import mazes.model.MazeType;
import mazes.model.PerfectMaze;
import mazes.model.RoomMaze;
import mazes.model.WrappingRoomMaze;
import mazes.view.IView;
import mazes.view.GameView;

/**
 * This is the driver class contains workflow of this homework.
 */
public class Main {

  /**
   * This is the main method.
   * --text will result in Text mode play.
   * --gui will result in GUI mode play.
   */
  public static void main(String[] args) {
    if (args[0].equals("--gui")) {
      System.out.println("GUI MODE");
      // Create the view
      IView view = new GameView("Hunt the Wumpus!");
      Maze maze = new PerfectMaze(5, 4, 2, 2, 1, 1);
      ControllerG controller = new ControllerG(maze, view);
    }

    if (args[0].equals("--text")) {
      System.out.println("TEXT MODE");
      // First Stage. Specify row, col, arrow, number og player and Maze type.
      System.out.println("Specify the row of this maze: ");
      Scanner in1 = new Scanner(System.in);
      String str1 = in1.nextLine();
      final int numRow = Integer.parseInt(str1);

      System.out.println("Specify the column of this maze: ");
      in1 = new Scanner(System.in);
      str1 = in1.nextLine();
      final int numCol = Integer.parseInt(str1);

      System.out.println("Specify the number of arrows of the game(between 1 and 10 inclusively):");
      in1 = new Scanner(System.in);
      str1 = in1.nextLine();
      final int numArrow = Integer.parseInt(str1);

      String strSize = "Foo";
      System.out.println("Specify the type of this maze: ");
      while (!strSize.equals("perfect")
              && !strSize.equals("imperfect")
              && !strSize.equals("wrapping")) {
        System.out.println("Please input 'perfect', 'imperfect' or 'wrapping'!");
        Scanner in2 = new Scanner(System.in);
        strSize = in2.nextLine();
      }
      final MazeType mazeType = MazeType.valueOf(strSize.toUpperCase());

      // Second Stage. Specify pits, bats, walls. They rely on row and col.
      System.out.println("Specify the number of pits of the game(between 0 and "
              + (numRow * numCol / 2) + " exclusively):");
      Scanner in3 = new Scanner(System.in);
      String str3 = in3.nextLine();
      final int numPit = Integer.parseInt(str3);

      System.out.println("Specify the number of bats of the game(between 0 and "
              + (numRow * numCol / 2) + " exclusively):");
      in3 = new Scanner(System.in);
      str3 = in3.nextLine();
      final int numBat = Integer.parseInt(str3);

      final int totalWallsNow = numCol * numRow - numCol - numRow + 1;
      System.out.println("Specify the number of walls remaining(between 0 and "
              + totalWallsNow + " inclusively):");
      in3 = new Scanner(System.in);
      str3 = in3.nextLine();
      final int remainingWalls = Integer.parseInt(str3);

      // Generate maze.
      if (mazeType.equals(MazeType.PERFECT)) {
        try {
          // create the model
          Maze maze1 = new PerfectMaze(numRow, numCol, numArrow, 1, numPit, numBat);
          maze1.generateGame();
          System.out.println(maze1.getBatList());
          System.out.println(maze1.getPitList());
          System.out.println(maze1.getWumpusLoc());
          System.out.println("Perfect maze generated!\n");
          // create the controller
          Readable reader = new InputStreamReader(System.in);
          Controller control = new Controller(reader, System.out);
          // give the model to the controller and give it control
          control.start(maze1);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      if (mazeType.equals(MazeType.IMPERFECT)) {
        try {
          Maze maze2 = new RoomMaze(numRow, numCol, numArrow, 1,
                  numPit, numBat, remainingWalls);
          maze2.generateGame();
          System.out.println("Non-Perfect maze generated!\n");
          Readable reader = new InputStreamReader(System.in);
          Controller control = new Controller(reader, System.out);
          control.start(maze2);
        }  catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      if (mazeType.equals(MazeType.WRAPPING)) {
        try {
          Maze maze3 = new WrappingRoomMaze(numRow, numCol, numArrow, 1,
                  numPit, numBat, remainingWalls);
          maze3.generateGame();
          System.out.println("Wrapping non-perfect maze generated!\n");
          Readable reader = new InputStreamReader(System.in);
          Controller control = new Controller(reader, System.out);
          control.start(maze3);
        }  catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
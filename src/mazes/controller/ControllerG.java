package mazes.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import mazes.model.Direction;
import mazes.model.Maze;
import mazes.model.MazeType;
import mazes.model.PerfectMaze;
import mazes.model.RoomMaze;
import mazes.model.WrappingRoomMaze;
import mazes.view.IView;

/**
 * This controller is the controller of GUI version hunt the Wumpus game.
 * It has start() method to start the game.
 */
public class ControllerG implements ActionListener, KeyListener {
  private Maze maze;
  private IView view;

  /**
   * Constructor of ControllerG object. And initializes with model and view.
   *
   * @param maze  model of this game
   * @param view  view of this game
   */
  public ControllerG(Maze maze, IView view) {
    this.maze = maze;
    this.view = view;
    view.setListeners(this, this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      // read from the input text field
      case "Shoot Button":
        String dir = view.getDirectionInput();
        int dis = Integer.parseInt(view.getDistanceInput());
        this.shootArrow(Direction.valueOf(dir), dis);
        view.clearInputString();
        view.resetFocus();
        break;
      case "Start Button":
        this.beginGame();
        //System.out.println("INBETWEEN");
        //System.out.println(view.getMazeType());
        //System.out.println("NOW?" + view.getMazeType());
        //System.out.println("INBETWEEN");
        view.resetFocus();
        break;
      case "Restart Button":
        view.removeRestartListener(this);
        view.removeAllListeners(this);
        view.setListeners(this, this);
        this.beginGame();
        view.resetFocus();
        break;
      case "Up Button":
        this.movePlayer(Direction.N);
        view.resetFocus();
        break;
      case "Left Button":
        this.movePlayer(Direction.W);
        view.resetFocus();
        break;
      case "Down Button":
        this.movePlayer(Direction.S);
        view.resetFocus();
        break;
      case "Right Button":
        this.movePlayer(Direction.E);
        view.resetFocus();
        break;
      default:
        throw new IllegalStateException("Error: Unknown button");
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    if (e.getKeyChar() == 'd') {
      System.out.println("Hi");
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyChar() == 'f') {
      System.out.println("NoNoNo");
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_UP:
        //System.out.println("2nd");
        this.movePlayer(Direction.N);
        return;
      case KeyEvent.VK_DOWN:
        this.movePlayer(Direction.S);
        return;
      case KeyEvent.VK_LEFT:
        this.movePlayer(Direction.W);
        return;
      case KeyEvent.VK_RIGHT:
        this.movePlayer(Direction.E);
        return;
      default:
        throw new IllegalStateException("Error: Unknown button");
    }
  }

  /**
   * Make the player move one step and update prompt pane.
   *
   * @param dir  specified direction, one of N, E, S, W
   */
  public void movePlayer(Direction dir) {
    maze.movePlayer(dir);
    int pLayerLoc = maze.getPlayerLocation();
    boolean batBar = maze.getPlayer().getMeetBat();
    boolean loseBar = maze.getPlayer().getLose();
    if (batBar) {
      this.view.getPromptPane()
              .setText("You are whisked away by superbats and ...\n");
    }
    if (loseBar) {
      if (maze.getWumpusLoc() == pLayerLoc) {
        this.view.getPromptPane()
                .setText("Player " + maze.getPlayer().getPlayerId() + "eaten by Wumpus! Lose!\n");
      } else {
        this.view.getPromptPane()
                .setText("Player " + maze.getPlayer().getPlayerId() + "fall into a pit! Lose!\n");
      }
    }
    if (maze.allLose()) {
      this.view.getPromptPane()
              .setText("Game Over! All Lose!\n");
      this.view.removeAllListeners(this);
    } else {
      maze.nextPlayer();
      String turnInfo = "Player " + maze.getPlayer().getPlayerId() + "'s turn\n";
      String cur = this.view.getPromptPane().getText();
      if (!(batBar || loseBar)) {
        this.printCurrentInfo(turnInfo);
      } else {
        this.printCurrentInfo(cur + turnInfo);
      }
    }
    this.view.setChangingMark(maze.getRoomList(), maze.smell(),
            maze.getPlayerLocation(), maze.getPlayerQueue());
    view.refreshView(maze.getRow(), maze.getCol());
  }

  /**
   * Make the player shoot arrow and update prompt pane.
   *
   * @param direction  specified direction
   * @param distance   specified distance
   */
  public void shootArrow(Direction direction, int distance) {
    maze.shootArrow(direction, distance);
    boolean winBar = maze.getPlayer().getWin();
    boolean loseBar = maze.getPlayer().getLose();
    if (winBar) {
      this.view.getPromptPane()
              .setText("Player " + maze.getPlayer().getPlayerId() + "killed Wumpus! Game Over!\n");
      this.view.removeAllListeners(this);
    } else if (loseBar) {
      this.view.getPromptPane()
              .setText("Player " + maze.getPlayer().getPlayerId() + "out of arrows! Lose!\n");
    }
    if (maze.allLose()) {
      this.view.getPromptPane()
              .setText("Game Over! All Lose!\n");
      this.view.removeAllListeners(this);
    } else {
      String arrowInfo = "Player " + maze.getPlayer().getPlayerId()
              + "has " + maze.getPlayer().getArrow() + " arrow left\n";
      maze.nextPlayer();
      String turnInfo = "Player " + maze.getPlayer().getPlayerId() + "'s turn\n";
      String cur = this.view.getPromptPane().getText();
      if (!(winBar || loseBar)) {
        this.printCurrentInfo(arrowInfo + turnInfo);
      } else {
        if (! winBar) {
          this.printCurrentInfo(cur + arrowInfo + turnInfo);
        }
      }
    }
  }

  /**
   * Initialize and begin the game.
   */
  public void beginGame() {
    if (this.view.getMazeType().equals(MazeType.PERFECT)) {
      this.maze = new PerfectMaze(view.getGameSetting().getMazeRow(),
              view.getGameSetting().getMazeCol(),
              view.getGameSetting().getMazeArrow(),
              view.getGameSetting().getMazePlayer(),
              view.getGameSetting().getMazePit(),
              view.getGameSetting().getMazeBat());
      maze.generateGame();
      this.view.setUnchangedMark(maze.getPitList(), maze.getBatList(), maze.getWumpusLoc());
    } else if (this.view.getMazeType().equals(MazeType.IMPERFECT)) {
      this.maze = new RoomMaze(view.getGameSetting().getMazeRow(),
              view.getGameSetting().getMazeCol(),
              view.getGameSetting().getMazeArrow(),
              view.getGameSetting().getMazePlayer(),
              view.getGameSetting().getMazePit(),
              view.getGameSetting().getMazeBat(),
              view.getGameSetting().getMazeWall());
      maze.generateGame();
      this.view.setUnchangedMark(maze.getPitList(), maze.getBatList(), maze.getWumpusLoc());
    }
    else {
      this.maze = new WrappingRoomMaze(view.getGameSetting().getMazeRow(),
              view.getGameSetting().getMazeCol(),
              view.getGameSetting().getMazeArrow(),
              view.getGameSetting().getMazePlayer(),
              view.getGameSetting().getMazePit(),
              view.getGameSetting().getMazeBat(),
              view.getGameSetting().getMazeWall());
      maze.generateGame();
      this.view.setUnchangedMark(maze.getPitList(), maze.getBatList(), maze.getWumpusLoc());
    }

    this.view.generateGameView(view.getGameSetting().getMazeRow(),
            view.getGameSetting().getMazeCol());
    // this.view.setController(this);
    this.printCurrentInfo("");
    this.view.setChangingMark(maze.getRoomList(), maze.smell(),
            maze.getPlayerLocation(), maze.getPlayerQueue());
    this.view.refreshView(view.getGameSetting().getMazeRow(),
            view.getGameSetting().getMazeCol());
  }

  /**
   * Update prompt pane with information of current user.
   *
   * @param cur   previous information on prompt pane.
   */
  public void printCurrentInfo(String cur) {
    String curLoc = String.format("you are in cave %d (%d arrow left) with neighbor %s.\n",
            maze.getPlayerLocation(), maze.getPlayer().getArrow(), maze.getPossibleStep());
    String curDanger = String.format("Wumpus nearby: %s. Pit nearby: %s.\n",
            maze.smell().get(0), maze.smell().get(1));
    String prompt = "Move(click or type arrow keys) or Shoot(click)?\n";
    this.view.getPromptPane()
            .setText(cur + curLoc + curDanger + prompt);
  }

  /**
   * Getter of maze.
   */
  public Maze getMaze() {
    return this.maze;
  }
}

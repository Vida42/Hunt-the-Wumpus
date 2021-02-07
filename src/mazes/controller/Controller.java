package mazes.controller;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import mazes.model.Direction;
import mazes.model.Maze;

/**
 * This controller is the controller of text version hunt the Wumpus game.
 * It has start() method to start the game.
 */
public class Controller {
  private final Readable in;
  private final Appendable out;

  /**
   * Constructor of Controller object. And initializes with in and out.
   *
     * @param in  take care of all input
     * @param out take care of all output
   */
  public Controller(Readable in, Appendable out) {
    this.in = in;
    this.out = out;
  }

  /**
   * Method that gives control to the controller.
   *
   * @param model the model to use.
   * @throws IOException if something goes wrong appending to out
   */
  public void start(Maze model) throws IOException {
    Objects.requireNonNull(model);
    String move;
    String direction;
    int distance;
    Scanner scan = new Scanner(this.in);
    while (! (model.anyWin() || model.allLose())) {
      this.out.append(String.format("you are in cave %d (%d arrow left) with neighbor %s.\n",
              model.getPlayerLocation(), model.getPlayer().getArrow(), model.getPossibleStep()));
      this.out.append(String.format("Wumpus nearby: %s. Pit nearby: %s.\n",
              model.smell().get(0), model.smell().get(1)));
      this.out.append("Want move or shoot?\n");
      String command = scan.next();
      switch (command) {
        case "move":
          this.out.append("Which cave to?\n");
          move = scan.next();
          model.movePlayer(Direction.valueOf(move));
          if (model.getPlayer().getMeetBat()) {
            this.out.append("You are whisked away by superbats and ...\n");
          }
          break;
        case "shoot":
          this.out.append(String.format("How far(1-%d)? \n", model.getArrowLimit()));
          distance = scan.nextInt();
          this.out.append("Toward direction?\n");
          direction = scan.next();
          model.shootArrow(Direction.valueOf(direction), distance);
          break;
        case "q":
          scan.close();
          return;
        default:
          throw new UnsupportedOperationException(command + " is unsupported");
      }
    }
    if (model.getPlayer().getWin()) {
      this.out.append("Wumpus killed by you! Win!\n");
    }
    if (model.getPlayer().getLose()) {
      if (model.getPlayerLocation() == model.getWumpusLoc()) {
        this.out.append("Wumpus eats you! Lose!\n");
      } else if (model.getPlayer().getArrow() == 0) {
        this.out.append("Out of arrow! Lose!\n");
      } else {
        this.out.append("Fall into a pit! Lose!\n");
      }
    }
  }
}
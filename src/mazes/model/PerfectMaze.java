package mazes.model;

/**
 * This class represents a perfect maze.
 */
public class PerfectMaze extends AbstractMaze {

  /**
   * Constructs a PerfectMaze object.
   * And initializes it to the given numRow, numCol, start, pit, bat and arrow.
   *
   * @param row         the row number of this maze
   * @param col         the column number of this maze
   * @param arrow       the number of arrow in the game
   * @param numPlayer   the number of the player in the game
   * @param pit         the number of pit in the game
   * @param bat         the number of bat in the game
   */
  public PerfectMaze(int row, int col, int arrow, int numPlayer, int pit, int bat) {
    super(row, col, arrow, numPlayer, pit, bat);
  }
}

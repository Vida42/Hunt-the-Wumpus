package mazes.model;

import static java.lang.Boolean.FALSE;

/**
 * This class represents a non-perfect and wrapping maze.
 */
public class WrappingRoomMaze extends AbstractMaze {

  private final int remainingWalls;



  /**
   * Constructs a WrappingRoomMaze object.
   * And initializes it to the given numRow, numCol, start, end and remainingWalls.
   *
   * @param row         the row number of this maze
   * @param col         the column number of this maze
   * @param arrow       the number of arrow in the game
   * @param numPlayer   the number of the player in the game
   * @param pit         the number of pit in the game
   * @param bat         the number of bat in the game
   * @param remainingWalls        the number of walls remaining
   */
  public WrappingRoomMaze(int row, int col, int arrow, int numPlayer,
                          int pit, int bat, int remainingWalls) {
    super(row, col, arrow, numPlayer, pit, bat);
    int totalRemainWalls = numCol * numRow - numCol - numRow + 1;
    if ((remainingWalls < 0) || (remainingWalls > totalRemainWalls)) {
      throw new IllegalArgumentException("Wrong number of remain walls.");
    }
    this.remainingWalls = remainingWalls;
  }

  @Override
  public void generateGame() {
    int totalWalls = numCol * numRow - numCol - numRow + 1;
    super.generateMaze();
    super.ifPerfect = FALSE;
    // System.out.println("begin here");
    super.getRoom(0).setNorth(super.getRoom(numCol * numRow - numCol));
    super.getRoom(numCol * numRow - numCol).setSouth(super.getRoom(0));
    int k = 0;
    boolean success = FALSE;
    for (int visitor = 0; visitor < super.getRoomList().size(); visitor++) {
      // System.out.println(k);
      int i = visitor / super.numCol;
      int j = visitor % super.numCol;
      // System.out.println("i"+i+",j"+j);
      if (k >= totalWalls - remainingWalls) {
        break;
      }
      if ((visitor == 0)
              || (visitor == numCol - 1)
              || (i == numRow - 1 && j == 0)
              || (visitor == super.getRoomList().size() - 1)) {
        success = super.connectWhenCorner(i, j);
        // System.out.println(success+"corner");
      } else if ((i == 0)
              || (j == 0)
              || (i == numRow - 1)
              || (j == numCol - 1)) {
        success = super.connectWhenEdge(i, j);
        // System.out.println(success+"edge");
      } else {
        success = super.connectWhenNormal(i, j);
        // System.out.println(success+"normal");
      }
      if (success) {
        k = k + 1;
      }
    }
    super.findTunnel();
    super.setPit();
    super.setBat();
    super.setWumpus();
    super.reconnect();
    this.setPlayerQueue();
  }

}

package mazes.model;

import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * This interface contains all operations that all types of maze should support.
 */
public interface Maze {

  /**
   * Generate a game.
   */
  void generateGame();

  /**
   * Return number of row of maze.
   *
   * @return row of maze
   */
  int getRow();

  /**
   * Return number of col of maze.
   *
   * @return col of maze
   */
  int getCol();

  /**
   * Return room having this index.
   *
   * @param index index user input
   * @return room in this index
   */
  Room getRoom(int index);

  /**
   * Return List of room.
   *
   * @return list of room
   */
  List<Room> getRoomList();

  /**
   * Get current location of player.
   *
   * @return current location of player
   */
  int getPlayerLocation();

  /**
   * Return List of indices of cave.
   *
   * @return list of indices of cave
   */
  List<Integer> getCaveList();

  /**
   * Return List of indices of tunnel.
   *
   * @return list of indices of tunnel
   */
  List<Integer> getTunnelList();

  /**
   * Return List of indices of room contains pit.
   *
   * @return list of indices of room contains pit
   */
  Set<Integer> getPitList();

  /**
   * Return List of indices of room contains bat.
   *
   * @return list of indices of room contains bat.
   */
  Set<Integer> getBatList();

  /**
   * Return index of room contains wumpus.
   *
   * @return index of room contains wumpus.
   */
  Integer getWumpusLoc();

  /**
   * Get status that if any player wins.
   *
   * @return true if any win otherwise false
   */
  boolean anyWin();

  /**
   * Get status that if all players lose.
   *
   * @return true if all lose otherwise false
   */
  boolean allLose();

  /**
   * Get arrow distance limit.
   *
   * @return arrow distance limit
   */
  int getArrowLimit();

  /**
   * Smells danger around current cave including pit and Wumpus.
   *
   * @return a list which first element stands for if smells Wumpus and
   *         second element stands for if smells pit
   */
  List<Boolean> smell();

  /**
   * Get all possible steps of player.
   *
   * @return all possible steps of player, shown as N, E, S, W
   */
  List<String> getPossibleStep();

  /**
   * Make the player move one step.
   *
   * @param nextStep  specified direction, one of N, E, S, W
   */
  void movePlayer(Direction nextStep);

  /**
   * Make the player shoot arrow.
   *
   * @param direction  specified direction
   * @param distance   specified distance
   */
  void shootArrow(Direction direction, int distance);

  /**
   * Get the queue of player.
   *
   * @return player queue
   */
  Queue<Player> getPlayerQueue();

  /**
   * Set the queue of player.
   */
  void setPlayerQueue();

  /**
   * Get current player.
   *
   * @return current player
   */
  Player getPlayer();

  /**
   * Set player to next valid one.
   */
  void nextPlayer();

}

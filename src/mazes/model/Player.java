package mazes.model;

/**
 * This class represents a player who join the maze game.
 * It contains all the functions a player should support.
 */
public class Player {
  private  int id;
  private int arrow;
  private int location;
  private boolean win;
  private boolean lose;
  private boolean meetBat;

  /**
   * Constructs a Player object. And initializes it to the given id, arrow and location.
   *
   * @param id        the id of the player
   * @param arrow     the number of arrows of this player
   * @param location  the start location of the player
   */
  public Player(int id, int arrow, int location) {
    this.id = id;
    this.arrow = arrow;
    this.location = location;
    this.win = false;
    this.lose = false;
    this.meetBat = false;
  }

  /**
   * Set player ID.
   */
  public void setPlayerId(int playerId) {
    this.id = playerId;
  }

  /**
   * Get player ID.
   *
   * @return player ID
   */
  public int getPlayerId() {
    return id;
  }

  /**
   * Consume one arrow of player.
   */
  public void consumeArrow() {
    this.arrow -= 1;
  }

  /**
   * Get current number of arrows of player.
   *
   * @return current number of arrows of player
   */
  public int getArrow() {
    return arrow;
  }

  /**
   * Set player location.
   */
  public void setPlayerLocation(int playerLocation) {
    this.location = playerLocation;
  }

  /**
   * Get current location of player.
   *
   * @return current location of player
   */
  public int getPlayerLocation() {
    return location;
  }

  /**
   * Change status of win.
   */
  public void setWin() {
    win = true;
  }

  /**
   * Get player's status of win.
   *
   * @return true when win otherwise false
   */
  public boolean getWin() {
    return win;
  }

  /**
   * Change status of lose.
   */
  public void setLose() {
    lose = true;
  }

  /**
   * Get player's status of lose.
   *
   * @return true when lose otherwise false
   */
  public boolean getLose() {
    return lose;
  }

  /**
   * Change status of meet bat.
   */
  public void setMeetBat(boolean meetBat) {
    this.meetBat = meetBat;
  }

  /**
   * Get player's status of meet bat.
   *
   * @return true when meet bat otherwise false
   */
  public boolean getMeetBat() {
    return meetBat;
  }

}

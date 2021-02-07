package mazes.model;

/**
 * This interface represents all the operations to be supported by a Room.
 */
public interface Room {
  /**
   * Return true if it is tunnel otherwise false.
   *
   * @return true if it is tunnel otherwise false
   */
  boolean isTunnel();

  /**
   * Set a room as tunnel.
   */
  void setTunnel();

  /**
   * Return true if visited otherwise false.
   *
   * @return true if visited otherwise false
   */
  boolean ifVisited();

  /**
   * Set a room as visited.
   */
  void setVisited();

  /**
   * Return north room of this room.
   *
   * @return north room of this room
   */
  Room getNorth();

  /**
   * Set northern room of this room.
   *
   * @param room room want to set
   */
  void setNorth(Room room);

  /**
   * Return south room of this room.
   *
   * @return south room of this room
   */
  Room getSouth();

  /**
   * Set southern room of this room.
   *
   * @param room room want to set
   */
  void setSouth(Room room);

  /**
   * Return west room of this room.
   *
   * @return west room of this room
   */
  Room getWest();

  /**
   * Set western room of this room.
   *
   * @param room room want to set
   */
  void setWest(Room room);

  /**
   * Return east room of this room.
   *
   * @return east room of this room
   */
  Room getEast();

  /**
   * Set eastern room of this room.
   *
   * @param room room want to set
   */
  void setEast(Room room);

  /**
   * Return row index of this room.
   *
   * @return row index of this room
   */
  int getRow();

  /**
   * Return column index of this room.
   *
   * @return column index of this room
   */
  int getCol();

  /**
   * Return index of this room.
   *
   * @return index of this room
   */
  int getIndex();
}
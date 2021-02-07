package mazes.model;

/**
 * This class represents a room which contains all the functions a room should support.
 */
public class RoomImpl implements Room {
  private Room north;
  private Room south;
  private Room east;
  private Room west;
  private boolean tunnel;
  private boolean visited;
  private int row;
  private int col;
  private int index;

  /**
   * Constructs a RoomImpl object.
   * And initializes it to the given row, col, and index.
   *
   * @param row      the row number of this room
   * @param col      the column number of this room
   * @param index    the index of this room
   */
  public RoomImpl(int row, int col, int index) {
    if (row < 0) {
      throw new IllegalArgumentException("Row number is non-negative.");
    }
    if (col < 0) {
      throw new IllegalArgumentException("Column number is non-negative.");
    }
    if (index < 0) {
      throw new IllegalArgumentException("Index is non-negative.");
    }
    this.north = null;
    this.south = null;
    this.east = null;
    this.west = null;
    this.tunnel = false;
    this.visited = false;
    this.row = row;
    this.col = col;
    this.index = index;
  }

  @Override
  public boolean isTunnel() {
    return this.tunnel;
  }

  @Override
  public void setTunnel() {
    this.tunnel = true;
  }

  @Override
  public boolean ifVisited() {
    return this.visited;
  }

  @Override
  public void setVisited() {
    this.visited = true;
  }

  @Override
  public Room getNorth() {
    return this.north;
  }

  @Override
  public void setNorth(Room room) {
    this.north = room;
  }

  @Override
  public Room getSouth() {
    return this.south;
  }

  @Override
  public void setSouth(Room room) {
    this.south = room;
  }

  @Override
  public Room getWest() {
    return this.west;
  }

  @Override
  public void setWest(Room room) {
    this.west = room;
  }

  @Override
  public Room getEast() {
    return this.east;
  }

  @Override
  public void setEast(Room room) {
    this.east = room;
  }

  @Override
  public int getRow() {
    return this.row;
  }

  @Override
  public int getCol() {
    return this.col;
  }

  @Override
  public int getIndex() {
    return this.index;
  }

  @Override
  public String toString() {
    return String.format("The %sth room(%s, %s) -- tunnel(%s)",
            this.getIndex(), this.getRow(), this.getCol(), this.isTunnel());
  }
}
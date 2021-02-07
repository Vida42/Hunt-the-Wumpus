package mazes.model;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * This class represents one abstract maze which is the parent of three type of mazes.
 */
public class AbstractMaze implements Maze {
  protected final int numRow;
  protected final int numCol;
  protected List<HashSet> setList;
  private List<Room> roomList;
  private List<Integer> caveList;
  private List<Integer> tunnelList;
  private Set<Integer> pitSet;
  private Set<Integer> batSet;
  private int numArrow;
  private int numPlayer;
  private int numPit;
  private int numBat;
  private int wumpusLoc;
  private List<HashSet> connectedSetList;
  protected boolean ifPerfect;
  private Player player;
  private Queue<Player> playerQueue;
  private List<Room> roomForSmellList;

  /**
   * Constructs an AbstractMaze object. And initializes it to the given numRow, numCol, start,
   * pit, bat and arrow.
   *
   * @param row         the row number of this maze
   * @param col         the column number of this maze
   * @param arrow       the number of arrow in the game
   * @param numPlayer   the number of the player in the game
   * @param pit         the number of pit in the game
   * @param bat         the number of bat in the game
   */
  public AbstractMaze(int row, int col, int arrow, int numPlayer, int pit, int bat) {
    if (row < 2) {
      throw new IllegalArgumentException("Please set a row number larger than 1.");
    }
    if (col < 2) {
      throw new IllegalArgumentException("Please set a column number larger than 1.");
    }
    if ((arrow < 1) || (arrow > 10)) {
      throw new IllegalArgumentException("Please set at most 10 arrows.");
    }
    if (numPlayer != 2 && numPlayer != 1) {
      throw new IllegalArgumentException("Only support 1 player or 2 players.");
    }
    this.numRow = row;
    this.numCol = col;
    if ((pit < 1) || (pit >= numRow * numCol / 2)) {
      throw new IllegalArgumentException("Too many pits.");
    }
    if ((bat < 1) || (bat >= numRow * numCol / 2)) {
      throw new IllegalArgumentException("Too many bats.");
    }
    this.numArrow = arrow;
    this.numPlayer = numPlayer;
    this.numPit = pit;
    this.numBat = bat;
    this.wumpusLoc = 0;
    this.setList = new ArrayList<>();
    this.connectedSetList = new ArrayList<>();
    this.roomList = new ArrayList<>();
    this.pitSet = new HashSet<>();
    this.batSet = new HashSet<>();
    this.caveList = new ArrayList<>();
    this.tunnelList = new ArrayList<>();
    this.roomForSmellList = new ArrayList<>();
    this.ifPerfect = true;
  }

  /**
   * Initialize one maze.
   */
  private void generateGraph() {
    for (int i = 0; i < numRow * numCol; i++) {
      roomList.add(new RoomImpl(i / numCol, i % numCol, i));
      roomForSmellList.add(new RoomImpl(i / numCol, i % numCol, i));
    }
  }

  /**
   * Generate a maze.
   */
  protected void generateMaze() {
    this.generateGraph();
    Random r = new Random();
    r.setSeed(21);
    int roomTotal = numRow * numCol;
    int doorTotal = numRow * numCol - 1;
    int k = 0;
    boolean success = FALSE;
    while (k < doorTotal) {
      int i = r.nextInt(numRow);
      int j = r.nextInt(numCol);
      // System.out.println(i+"row,"+j+"cow");
      int idx = i * numCol + j;
      if ((idx == 0)
              || (idx == numCol - 1)
              || (i == numRow - 1 && j == 0)
              || (idx == roomTotal - 1)) {
        success = connectWhenCorner(i, j);
        // System.out.println(success+"corner");
      } else if ((i == 0)
              || (j == 0)
              || (i == numRow - 1)
              || (j == numCol - 1)) {
        success = connectWhenEdge(i, j);
      } else {
        success = connectWhenNormal(i, j);
      }
      if (success) {
        k = k + 1;
      }
    }
  }

  @Override
  public void generateGame() {
    this.generateMaze();
    this.findTunnel();
    this.setPit();
    this.setBat();
    this.setWumpus();
    this.reconnect();
    this.setPlayerQueue();
  }

  /**
   * Build a door between one room which is at corner with another.
   *
   * @param i row index of room
   * @param j column index of room
   * @return if they're connected successfully
   */
  protected boolean connectWhenCorner(int i, int j) {
    int idx = i * numCol + j;
    Random r = new Random();
    r.setSeed(21);
    int door = r.nextInt(2);
    if (idx == 0) {
      // upper left corner
      if (door == 0) {
        if (!checkExist(roomList.get(idx), roomList.get(idx + 1))) {
          roomList.get(idx).setEast(roomList.get(idx + 1));
          roomList.get(idx + 1).setWest(roomList.get(idx));
          addSet(roomList.get(idx), roomList.get(idx + 1));
          return TRUE;
        }
      } else {
        if (!checkExist(roomList.get(idx), roomList.get(numCol))) {
          roomList.get(idx).setSouth(roomList.get(numCol));
          roomList.get(numCol).setNorth(roomList.get(idx));
          addSet(roomList.get(idx), roomList.get(numCol));
          return TRUE;
        }
      }
    } else if (idx == numCol - 1) {
      // upper right corner
      if (door == 0) {
        if (!checkExist(roomList.get(idx), roomList.get(idx - 1))) {
          roomList.get(idx).setWest(roomList.get(idx - 1));
          roomList.get(idx - 1).setEast(roomList.get(idx));
          addSet(roomList.get(idx), roomList.get(idx - 1));
          return TRUE;
        }
      } else {
        if (!checkExist(roomList.get(idx), roomList.get(idx + numCol))) {
          roomList.get(idx).setSouth(roomList.get(idx + numCol));
          roomList.get(idx + numCol).setNorth(roomList.get(idx));
          addSet(roomList.get(idx), roomList.get(idx + numCol));
          return TRUE;
        }
      }
    } else if (i == numRow - 1 && j == 0) {
      // bottom left corner
      if (door == 0) {
        if (!checkExist(roomList.get(idx), roomList.get(idx + 1))) {
          roomList.get(idx).setEast(roomList.get(idx + 1));
          roomList.get(idx + 1).setWest(roomList.get(idx));
          addSet(roomList.get(idx), roomList.get(idx + 1));
          return TRUE;
        }
      } else {
        if (!checkExist(roomList.get(idx), roomList.get(idx - numCol))) {
          roomList.get(idx).setNorth(roomList.get(idx - numCol));
          roomList.get(idx - numCol).setSouth(roomList.get(idx));
          addSet(roomList.get(idx), roomList.get(idx - numCol));
          return TRUE;
        }
      }
    } else if (idx == numRow * numCol - 1) {
      // bottom right corner
      if (door == 0) {
        if (!checkExist(roomList.get(idx), roomList.get(idx - 1))) {
          roomList.get(idx).setWest(roomList.get(idx - 1));
          roomList.get(idx - 1).setEast(roomList.get(idx));
          addSet(roomList.get(idx), roomList.get(idx - 1));
          return TRUE;
        }
      } else {
        if (!checkExist(roomList.get(idx), roomList.get(idx - numCol))) {
          roomList.get(idx).setNorth(roomList.get(idx - numCol));
          roomList.get(idx - numCol).setSouth(roomList.get(idx));
          addSet(roomList.get(idx), roomList.get(idx - numCol));
          return TRUE;
        }
      }
    }
    return FALSE;
  }

  /**
   * Build a door between one room which is on the edge with another.
   *
   * @param i row index of room
   * @param j column index of room
   * @return if they're connected successfully
   */
  protected boolean connectWhenEdge(int i, int j) {
    int idx = i * numCol + j;
    if (i == 0) {
      // upper edge
      if (!checkExist(roomList.get(idx), roomList.get(idx + 1))) {
        roomList.get(idx).setEast(roomList.get(idx + 1));
        roomList.get(idx + 1).setWest(roomList.get(idx));
        addSet(roomList.get(idx), roomList.get(idx + 1));
        return TRUE;
      } else if (!checkExist(roomList.get(idx), roomList.get(idx + numCol))) {
        roomList.get(idx).setSouth(roomList.get(idx + numCol));
        roomList.get(idx + numCol).setNorth(roomList.get(idx));
        addSet(roomList.get(idx), roomList.get(idx + numCol));
        return TRUE;
      } else if (!checkExist(roomList.get(idx), roomList.get(idx - 1))) {
        roomList.get(idx).setWest(roomList.get(idx - 1));
        roomList.get(idx - 1).setEast(roomList.get(idx));
        addSet(roomList.get(idx), roomList.get(idx - 1));
        return TRUE;
      }
    } else if (j == 0) {
      // left edge
      if (!checkExist(roomList.get(idx), roomList.get(idx - numCol))) {
        roomList.get(idx).setNorth(roomList.get(idx - numCol));
        roomList.get(idx - numCol).setSouth(roomList.get(idx));
        addSet(roomList.get(idx), roomList.get(idx - numCol));
        return TRUE;
      } else if (!checkExist(roomList.get(idx), roomList.get(idx + 1))) {
        roomList.get(idx).setEast(roomList.get(idx + 1));
        roomList.get(idx + 1).setWest(roomList.get(idx));
        addSet(roomList.get(idx), roomList.get(idx + 1));
        return TRUE;
      } else if (!checkExist(roomList.get(idx), roomList.get(idx + numCol))) {
        roomList.get(idx).setSouth(roomList.get(idx + numCol));
        roomList.get(idx + numCol).setNorth(roomList.get(idx));
        addSet(roomList.get(idx), roomList.get(idx + numCol));
        return TRUE;
      }
    } else if (i == numRow - 1) {
      // bottom edge
      if (!checkExist(roomList.get(idx), roomList.get(idx - 1))) {
        roomList.get(idx).setWest(roomList.get(idx - 1));
        roomList.get(idx - 1).setEast(roomList.get(idx));
        addSet(roomList.get(idx), roomList.get(idx - 1));
        return TRUE;
      } else if (!checkExist(roomList.get(idx), roomList.get(idx - numCol))) {
        roomList.get(idx).setNorth(roomList.get(idx - numCol));
        roomList.get(idx - numCol).setSouth(roomList.get(idx));
        addSet(roomList.get(idx), roomList.get(idx - numCol));
        return TRUE;
      } else if (!checkExist(roomList.get(idx), roomList.get(idx + 1))) {
        roomList.get(idx).setEast(roomList.get(idx + 1));
        roomList.get(idx + 1).setWest(roomList.get(idx));
        addSet(roomList.get(idx), roomList.get(idx + 1));
        return TRUE;
      }
    } else if (j == numCol - 1) {
      // right edge
      if (!checkExist(roomList.get(idx), roomList.get(idx + numCol))) {
        roomList.get(idx).setSouth(roomList.get(idx + numCol));
        roomList.get(idx + numCol).setNorth(roomList.get(idx));
        addSet(roomList.get(idx), roomList.get(idx + numCol));
        return TRUE;
      } else if (!checkExist(roomList.get(idx), roomList.get(idx - 1))) {
        roomList.get(idx).setWest(roomList.get(idx - 1));
        roomList.get(idx - 1).setEast(roomList.get(idx));
        addSet(roomList.get(idx), roomList.get(idx - 1));
        return TRUE;
      } else if (!checkExist(roomList.get(idx), roomList.get(idx - numCol))) {
        roomList.get(idx).setNorth(roomList.get(idx - numCol));
        roomList.get(idx - numCol).setSouth(roomList.get(idx));
        addSet(roomList.get(idx), roomList.get(idx - numCol));
        return TRUE;
      }
    }
    return FALSE;
  }

  /**
   * Build a door between one room which is inside the maze with another.
   *
   * @param i row index of room
   * @param j column index of room
   * @return if they're connected successfully
   */
  protected boolean connectWhenNormal(int i, int j) {
    int idx = i * numCol + j;
    if (!checkExist(roomList.get(idx), roomList.get(idx - numCol))) {
      // upper room
      roomList.get(idx).setNorth(roomList.get(idx - numCol));
      roomList.get(idx - numCol).setSouth(roomList.get(idx));
      addSet(roomList.get(idx), roomList.get(idx - numCol));
      return TRUE;
    } else if (!checkExist(roomList.get(idx), roomList.get(idx + 1))) {
      // right room
      roomList.get(idx).setEast(roomList.get(idx + 1));
      roomList.get(idx + 1).setWest(roomList.get(idx));
      addSet(roomList.get(idx), roomList.get(idx + 1));
      return TRUE;
    } else if (!checkExist(roomList.get(idx), roomList.get(idx + numCol))) {
      // bottom room
      roomList.get(idx).setSouth(roomList.get(idx + numCol));
      roomList.get(idx + numCol).setNorth(roomList.get(idx));
      addSet(roomList.get(idx), roomList.get(idx + numCol));
      return TRUE;
    } else if (!checkExist(roomList.get(idx), roomList.get(idx - 1))) {
      // left room
      roomList.get(idx).setWest(roomList.get(idx - 1));
      roomList.get(idx - 1).setEast(roomList.get(idx));
      addSet(roomList.get(idx), roomList.get(idx - 1));
      return TRUE;
    }
    return FALSE;
  }

  /**
   * Check is there is already a door between two rooms.
   *
   * @param a the first room
   * @param b the second room
   * @return if they're already connected
   */
  protected boolean checkExist(Room a, Room b) {
    if (ifPerfect) {
      for (HashSet each : connectedSetList) {
        if (each.contains(a) && each.contains(b)) {
          return true;
        }
      }
      return false;
    } else {
      for (HashSet each : setList) {
        if (each.contains(a) && each.contains(b)) {
          return true;
        }
      }
      return false;
    }
  }

  /**
   * Help to build a door between two rooms.
   *
   * @param a the first room
   * @param b the second room
   */
  protected void addSet(Room a, Room b) {
    if (ifPerfect) {
      addSet1(a, b);
    } else {
      addSet2(a, b);
    }
  }

  /**
   * Help to build a door between two rooms when build perfect maze.
   * If a connects to b, b connects to c, then they are stored as
   * (a, b, c) in connectedSetList.
   *
   * @param a the first room
   * @param b the second room
   */
  protected void addSet1(Room a, Room b) {
    for (HashSet each : connectedSetList) {
      if (each.contains(a)) {
        for (HashSet each2 : connectedSetList) {
          if (each2.contains(b)) {
            HashSet<Room> mergeSet = new HashSet<>();
            mergeSet.addAll(each);
            mergeSet.addAll(each2);
            setList.add(connectRooms(a, b));
            connectedSetList.add(mergeSet);
            connectedSetList.remove(each);
            connectedSetList.remove(each2);
            return;
          }
        }
        each.add(b);
        setList.add(connectRooms(a, b));
        return;
      }
      if (each.contains(b)) {
        for (HashSet each2 : connectedSetList) {
          if (each2.contains(a)) {
            HashSet<Room> mergeSet = new HashSet<>();
            mergeSet.addAll(each);
            mergeSet.addAll(each2);
            connectedSetList.add(mergeSet);
            connectedSetList.remove(each);
            connectedSetList.remove(each2);
            setList.add(connectRooms(a, b));
            return;
          }
        }
        each.add(a);
        setList.add(connectRooms(a, b));
        // System.out.println("add"+a+each);
        return;
      }
    }
    setList.add(connectRooms(a, b));
    connectedSetList.add(connectRooms(a, b));
    // System.out.println(connectedRooms);
  }

  /**
   * Help to build a door between two rooms when building non-perfect maze.
   * Two rooms connected are stored as one set in setList.
   * @param a the first room
   * @param b the second room
   */
  protected void addSet2(Room a, Room b) {
    setList.add(connectRooms(a, b));
    // System.out.println(connectRooms(a, b));
  }

  /**
   * Put two rooms inside one set.
   *
   * @param a the first room
   * @param b the second room
   * @return the set contains two rooms
   */
  protected HashSet<Room> connectRooms(Room a, Room b) {
    HashSet<Room> connectedRooms = new HashSet<Room>();
    connectedRooms.add(a);
    connectedRooms.add(b);
    return connectedRooms;
  }

  /**
   * Set some rooms to tunnels and others to caves.
   */
  protected void findTunnel() {
    for (Room each : roomList) {
      if (((each.getNorth() != null) ? 1 : 0)
              + ((each.getSouth() != null) ? 1 : 0)
              + ((each.getWest() != null) ? 1 : 0)
              + ((each.getEast() != null) ? 1 : 0) == 2) {
        this.tunnelList.add(each.getIndex());
        each.setTunnel();
      } else {
        this.caveList.add(each.getIndex());
      }
    }
  }

  /**
   * Reconnect caves in the game.
   * Since room maybe cave or tunnel, this method will connect two caves
   * if there exists tunnel between them.
   * Aim of this method was changed due to the aim of GUI display in HW6.
   * We now save those information in roomForSmellList and use it for smell.
   */
  protected void reconnect() {
    for (int index : caveList) {
      Room each = roomList.get(index);
      Room tmp = roomForSmellList.get(index);
      tmp.setNorth(each.getNorth());
      tmp.setSouth(each.getSouth());
      tmp.setWest(each.getWest());
      tmp.setEast(each.getEast());
      if ((each.getNorth() != null) && each.getNorth().isTunnel()) {
        tmp.setNorth(this.findNewNeighbor(each.getNorth(), Direction.S, true));
      }
      if ((each.getSouth() != null) && each.getSouth().isTunnel()) {
        tmp.setSouth(this.findNewNeighbor(each.getSouth(), Direction.N, true));
      }
      if ((each.getWest() != null) && each.getWest().isTunnel()) {
        tmp.setWest(this.findNewNeighbor(each.getWest(), Direction.E, true));
      }
      if ((each.getEast() != null) && each.getEast().isTunnel()) {
        tmp.setEast(this.findNewNeighbor(each.getEast(), Direction.W, true));
      }
    }
  }

  /**
   * Help to reconnect room.
   * New parameter forSmell add for the purpose of smell danger in HW6.
   * Due to the GUI display purpose, this method is also used when move player.
   *
   * @param cur        current room
   * @param inDir      which direction current room comes from
   * @param forSmell   true when used for smelling and false when for moving
   * @return           new neighbor
   */
  private Room findNewNeighbor(Room cur, Direction inDir, boolean forSmell) {
    if (! forSmell) {
      cur.setVisited();
    }
    // incoming direction is not North and current outgoing to north
    if ((! inDir.equals(Direction.N)) && (cur.getNorth() != null)) {
      if (cur.getNorth().isTunnel()) {
        return this.findNewNeighbor(cur.getNorth(), Direction.S, forSmell);
      } else {
        return cur.getNorth();
      }
    } else if ((! inDir.equals(Direction.S)) && (cur.getSouth() != null)) {
      if (cur.getSouth().isTunnel()) {
        return this.findNewNeighbor(cur.getSouth(), Direction.N, forSmell);
      } else {
        return cur.getSouth();
      }
    } else if ((! inDir.equals(Direction.W)) && (cur.getWest() != null)) {
      if (cur.getWest().isTunnel()) {
        return this.findNewNeighbor(cur.getWest(), Direction.E, forSmell);
      } else {
        return cur.getWest();
      }
    } else {
      if (cur.getEast().isTunnel()) {
        return this.findNewNeighbor(cur.getEast(), Direction.W, forSmell);
      } else {
        return cur.getEast();
      }
    }
  }

  /**
   * Set caves with pits.
   */
  protected void setPit() {
    Random r = new Random();
    r.setSeed(21);
    while (pitSet.size() < numPit) {
      pitSet.add(caveList.get(r.nextInt(caveList.size())));
    }
  }

  /**
   * Set caves with bats.
   */
  protected void setBat() {
    Random r = new Random();
    r.setSeed(22);
    while (batSet.size() < numBat) {
      batSet.add(caveList.get(r.nextInt(caveList.size())));
    }
  }

  /**
   * Set caves with wumpus.
   */
  protected void setWumpus() {
    Random r = new Random();
    r.setSeed(23);
    wumpusLoc = caveList.get(r.nextInt(caveList.size()));
  }

  /**
   * Get one room from roomForSmellList given index.
   * This method was set for HW6 only, and for smell purpose only.
   *
   * @param index room index
   * @return room with index input
   */
  private Room getSmellRoom(int index) {
    if (index < 0 || index >= roomList.size()) {
      throw new IllegalArgumentException("Index is illegal.");
    }
    return roomForSmellList.get(index);
  }

  @Override
  public List<Boolean> smell() {
    List<Boolean> danger = new ArrayList<>();
    danger.add(false);
    danger.add(false);
    int pLayerLoc = this.getPlayerLocation();
    if (this.getSmellRoom(pLayerLoc).getNorth() != null) {
      if (! danger.get(0)) {
        danger.set(0, wumpusLoc == this.getSmellRoom(pLayerLoc).getNorth().getIndex());
      }
      if (! danger.get(1)) {
        danger.set(1, pitSet.contains(this.getSmellRoom(pLayerLoc).getNorth().getIndex()));
      }
    }
    if (this.getSmellRoom(pLayerLoc).getEast() != null) {
      if (! danger.get(0)) {
        danger.set(0, wumpusLoc == this.getSmellRoom(pLayerLoc).getEast().getIndex());
      }
      if (! danger.get(1)) {
        danger.set(1, pitSet.contains(this.getSmellRoom(pLayerLoc).getEast().getIndex()));
      }
    }
    if (this.getSmellRoom(pLayerLoc).getSouth() != null) {
      if (! danger.get(0)) {
        danger.set(0, wumpusLoc == this.getSmellRoom(pLayerLoc).getSouth().getIndex());
      }
      if (! danger.get(1)) {
        danger.set(1, pitSet.contains(this.getSmellRoom(pLayerLoc).getSouth().getIndex()));
      }
    }
    if (this.getSmellRoom(pLayerLoc).getWest() != null) {
      if (! danger.get(0)) {
        danger.set(0, wumpusLoc == this.getSmellRoom(pLayerLoc).getWest().getIndex());
      }
      if (! danger.get(1)) {
        danger.set(1, pitSet.contains(this.getSmellRoom(pLayerLoc).getWest().getIndex()));
      }
    }
    return danger;
  }

  @Override
  public List<String> getPossibleStep() {
    List<String> move = new ArrayList<>();
    int pLayerLoc = this.getPlayerLocation();
    if (this.getRoom(pLayerLoc).getNorth() != null) {
      move.add("N");
    }
    if (this.getRoom(pLayerLoc).getEast() != null) {
      move.add("E");
    }
    if (this.getRoom(pLayerLoc).getSouth() != null) {
      move.add("S");
    }
    if (this.getRoom(pLayerLoc).getWest() != null) {
      move.add("W");
    }
    return move;
  }

  @Override
  public void movePlayer(Direction nextStep) {
    int pLayerLoc = this.getPlayerLocation();
    Room curRoom = this.getRoom(pLayerLoc);
    Room newRoom = curRoom;
    switch (nextStep) {
      case N:
        if (curRoom.getNorth() != null) {
          if (curRoom.getNorth().isTunnel()) {
            newRoom = this.findNewNeighbor(curRoom.getNorth(), Direction.S, false);
          } else {
            newRoom = curRoom.getNorth();
          }
          this.movePlayerHelper(newRoom.getIndex());
        }
        break;
      case S:
        if (curRoom.getSouth() != null) {
          if (curRoom.getSouth().isTunnel()) {
            newRoom = this.findNewNeighbor(curRoom.getSouth(), Direction.N, false);
          } else {
            newRoom = curRoom.getSouth();
          }
          this.movePlayerHelper(newRoom.getIndex());
        }
        break;
      case W:
        if (curRoom.getWest() != null) {
          if (curRoom.getWest().isTunnel()) {
            newRoom = this.findNewNeighbor(curRoom.getWest(), Direction.E, false);
          } else {
            newRoom = curRoom.getWest();
          }
          this.movePlayerHelper(newRoom.getIndex());
        }
        break;
      case E:
        if (curRoom.getEast() != null) {
          if (curRoom.getEast().isTunnel()) {
            newRoom = this.findNewNeighbor(curRoom.getEast(), Direction.W, false);
          } else {
            newRoom = curRoom.getEast();
          }
          this.movePlayerHelper(newRoom.getIndex());
        }
        break;
      default:
        throw new IllegalStateException("Next step should be N,  E, S or W!");
    }
  }

  /**
   * Help to move player and find if player lose.
   *
   * @param location        aimed room
   */
  private void movePlayerHelper(int location) {
    this.player.setPlayerLocation(location);
    this.getRoom(this.getPlayerLocation()).setVisited();
    //System.out.println("SEE HERE");
    //System.out.println(this.getRoom(this.getPlayerLocation()).ifVisited());
    this.player.setMeetBat(batSet.contains(location));
    this.judgeLose();
  }

  /**
   * Decide if player loses in the moment.
   */
  private void judgeLose() {
    if (this.player.getMeetBat()) {
      this.throwPlayer();
    }
    if (this.getPlayerLocation() == wumpusLoc) {
      this.player.setLose();
    }
    if (pitSet.contains(this.getPlayerLocation())) {
      this.player.setLose();
    }
    if (this.player.getArrow() == 0) {
      this.player.setLose();
    }
  }

  /**
   * Throw player to another cave when player moves into a cave with bat.
   */
  private void throwPlayer() {
    while (batSet.contains(this.getPlayerLocation())) {
      Random r = new Random();
      // r.setSeed(10);
      int throwOrNot = r.nextInt(2);
      if (throwOrNot == 1) {
        // THROW when 1
        int newLoc = this.getPlayerLocation();
        while (newLoc == this.getPlayerLocation()) {
          Random r2 = new Random();
          // r2.setSeed(321);
          newLoc = caveList.get(r2.nextInt(caveList.size()));
        }
        this.player.setPlayerLocation(newLoc);
        this.getRoom(this.getPlayerLocation()).setVisited();
      }
      else {
        return;
      }
    }
  }

  @Override
  public void shootArrow(Direction direction, int distance) {
    if ((distance < 1) || (distance > min(numRow, numCol))) {
      throw new IllegalArgumentException("illegal distance of arrow");
    }
    int pLayerLoc = this.getPlayerLocation();
    Room cur = this.getSmellRoom(pLayerLoc);
    int count = 1;
    if (direction.equals(Direction.E) && cur.getEast() != null) {
      shootHelper(cur.getEast(), cur, count, distance);
    } else if (direction.equals(Direction.W) && cur.getWest() != null) {
      shootHelper(cur.getWest(), cur, count, distance);
    } else if (direction.equals(Direction.N) && cur.getNorth() != null) {
      shootHelper(cur.getNorth(), cur, count, distance);
    } else if (direction.equals(Direction.S) && cur.getSouth() != null) {
      shootHelper(cur.getSouth(), cur, count, distance);
    }
    this.judgeLose();
  }

  /**
   * Help to shoot and update arrow information.
   *
   * @param cur        current room
   * @param pre        previous room before moving to current room
   * @param count      specified distance
   * @param distance   specified distance
   */
  private void shootHelper(Room cur, Room pre, int count, int distance) {
    while (count < distance) {
      if (cur.getNorth() == pre) {
        if (cur.getSouth() != null) {
          pre = cur;
          cur = cur.getSouth();
          count += 1;
        } else {
          this.player.consumeArrow();
          return;
        }
      } else if (cur.getSouth() == pre) {
        if (cur.getNorth() != null) {
          pre = cur;
          cur = cur.getNorth();
          count += 1;
        } else {
          this.player.consumeArrow();
          return;
        }
      } else if (cur.getWest() == pre) {
        if (cur.getEast() != null) {
          pre = cur;
          cur = cur.getEast();
          count += 1;
        } else {
          this.player.consumeArrow();
          return;
        }
      } else {
        if (cur.getWest() != null) {
          pre = cur;
          cur = cur.getWest();
          count += 1;
        } else {
          this.player.consumeArrow();
          return;
        }
      }
    }
    if (cur.getIndex() == wumpusLoc) {
      this.player.setWin();
    }
    this.player.consumeArrow();
  }

  @Override
  public Room getRoom(int index) {
    if (index < 0 || index >= roomList.size()) {
      throw new IllegalArgumentException("Index is illegal.");
    }
    return roomList.get(index);
  }

  @Override
  public List<Room> getRoomList() {
    return roomList;
  }

  @Override
  public List<Integer> getCaveList() {
    return caveList;
  }

  @Override
  public List<Integer> getTunnelList() {
    return tunnelList;
  }

  @Override
  public Set<Integer> getPitList() {
    return pitSet;
  }

  @Override
  public Set<Integer> getBatList() {
    return batSet;
  }

  @Override
  public Integer getWumpusLoc() {
    return wumpusLoc;
  }

  @Override
  public int getPlayerLocation() {
    return this.player.getPlayerLocation();
  }

  @Override
  public boolean anyWin() {
    return this.playerQueue.stream().anyMatch(Player::getWin);
  }

  @Override
  public boolean allLose() {
    return this.playerQueue.stream().allMatch(Player::getLose);
  }

  @Override
  public int getArrowLimit() {
    return min(numRow, numCol);
  }

  @Override
  public Queue<Player> getPlayerQueue() {
    return this.playerQueue;
  }

  @Override
  public void setPlayerQueue() {
    Random r = new Random();
    r.setSeed(21);
    Queue<Player> playerQueue = new LinkedList<>();
    for (int i = 0; i < numPlayer; i++) {
      int start = this.getSafeLocation().get(r.nextInt(this.getSafeLocation().size()));
      Player player = new Player(i, numArrow, start);
      playerQueue.add(player);
      this.playerQueue = playerQueue;
    }
    this.player = Objects.requireNonNull(playerQueue.poll());
    playerQueue.add(player);
  }

  /**
   * Generate a list which stores the safe room.
   * Safe room means a cave where is no wumpus, bat or pit.
   *
   * @return   generated safe room list
   */
  private List<Integer> getSafeLocation() {
    List<Integer> dangerLoc = new ArrayList<>();
    List<Integer> safeLoc = new ArrayList<>();
    safeLoc.addAll(caveList);
    dangerLoc.addAll(batSet);
    dangerLoc.addAll(pitSet);
    dangerLoc.add(wumpusLoc);
    safeLoc.removeAll(dangerLoc);
    return safeLoc;
  }

  @Override
  public int getRow() {
    return numRow;
  }

  @Override
  public int getCol() {
    return numCol;
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public void nextPlayer() {
    Player player = Objects.requireNonNull(this.playerQueue.poll());
    while (player.getLose()) {
      this.playerQueue.add(player);
      player = Objects.requireNonNull(this.playerQueue.poll());
    }
    this.player = player;
    this.playerQueue.add(player);
  }
}

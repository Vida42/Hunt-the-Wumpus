package mazes.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.OverlayLayout;
import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.LayoutManager;
import java.awt.Label;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import mazes.model.MazeType;
import mazes.model.Player;
import mazes.model.Room;

/**
 * This class represents the implementation of the view.
 */
public class GameView extends JFrame implements IView {
  private static final long serialVersionUID = 5883479994622814210L;

  private JLabel playerLabel;
  private JButton shootButton;
  private JButton startButton;
  private JButton reStartButton;
  private JButton upButton;
  private JButton leftButton;
  private JButton downButton;
  private JButton rightButton;
  private JTextField directionInput;
  private JTextField distanceInput;
  private MenuView menu;
  private JPanel mazeMap;
  private JTextPane promptPane;
  private JPanel controlPanel;
  private Set<Integer> pitList;
  private Set<Integer> batList;
  private Integer wumpusLoc;
  private List<Room> roomList;
  private List<Boolean> danger;
  private int playerLoc;
  private Queue<Player> playerQueue;

  /**
   * Constructor of GameView object. And initializes with basic control panel.
   *
   * @param caption the caption for the view
   */
  public GameView(String caption) {
    super(caption);
    setSize(500, 300);
    setLocation(200, 200);
    this.menu = new MenuView();
    setJMenuBar(menu);
    setFocusable(true);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.playerLabel = this.getImageLabel("player", 60, 60);

    startButton = new JButton("START GAME");
    startButton.setActionCommand("Start Button");
    this.add(startButton);

    reStartButton = new JButton("RESTART");
    reStartButton.setActionCommand("Restart Button");

    shootButton = new JButton("press S to shoot");
    ImageIcon uk = new ImageIcon("image\\target.png");
    this.shootButton.setIcon(uk);
    shootButton.setActionCommand("Shoot Button");

    upButton = new BasicArrowButton(BasicArrowButton.NORTH);
    upButton.setActionCommand("Up Button");
    leftButton = new BasicArrowButton(BasicArrowButton.WEST);
    leftButton.setActionCommand("Left Button");
    downButton = new BasicArrowButton(BasicArrowButton.SOUTH);
    downButton.setActionCommand("Down Button");
    rightButton = new BasicArrowButton(BasicArrowButton.EAST);
    rightButton.setActionCommand("Right Button");

    controlPanel = new JPanel(new GridLayout(3, 1));

    // first panel: shoot input and start/restart
    JPanel shootPanel = new JPanel(new GridLayout(3, 2));
    shootPanel.add(new Label("Shoot Direction(N, W, S, E)"));
    // the direction text field
    directionInput = new JTextField(10);
    shootPanel.add(directionInput);
    shootPanel.add(new Label("Shoot Distance(1-3)"));
    // the distance text field
    distanceInput = new JTextField(10);
    shootPanel.add(distanceInput);
    shootPanel.add(startButton);
    shootPanel.add(reStartButton);
    // second panel: move button
    JPanel movePanel = new JPanel(new GridLayout(2, 3));
    movePanel.add(new Label(""));
    movePanel.add(upButton);
    movePanel.add(new Label(""));
    movePanel.add(leftButton);
    movePanel.add(downButton);
    movePanel.add(rightButton);
    // add to controlPanel
    controlPanel.add(shootPanel);
    controlPanel.add(shootButton);
    controlPanel.add(movePanel);
    this.add(controlPanel);

    pack();
    setVisible(true);

  }

  @Override
  public void generateGameView(int row, int col) {

    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = GridBagConstraints.VERTICAL;
    JPanel base = new JPanel(gridBagLayout);
    this.mazeMap = new JPanel(new GridLayout(row, col));
    gridBagLayout.setConstraints(this.mazeMap, gridBagConstraints);
    base.add(controlPanel);

    mazeMap.setBackground(Color.black);
    //System.out.println(row);
    //System.out.println(col);

    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        JPanel panel = new JPanel();
        LayoutManager overlay = new OverlayLayout(panel);
        panel.setLayout(overlay);
        panel.add(this.getImageLabel("black", 100, 100));
        this.mazeMap.add(panel);

      }
    }
    base.add(this.mazeMap);

    this.promptPane = new JTextPane();
    gridBagLayout.setConstraints(this.promptPane, gridBagConstraints);
    base.add(this.promptPane);


    JScrollPane myJScrollPane = new JScrollPane(base,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    setContentPane(myJScrollPane);
    base.revalidate();
    pack();
  }

  @Override
  public void refreshView(int row, int col) {
    //System.out.println(maze.getPlayerLocation());
    //System.out.println(maze.getPossibleStep());
    this.promptPane.revalidate();
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        int loc = i * col + j;
        JPanel panel = (JPanel) this.mazeMap.getComponent(loc);
        panel.removeAll();
        Room room = this.roomList.get(loc);
        if (loc == this.playerLoc) {
          panel.add(this.playerLabel);
        } else if (this.playerQueue.stream()
                .anyMatch(p -> loc == p.getPlayerLocation())) {
          panel.add(this.getImageLabel("player", 60, 60));
        }
        if (room.ifVisited()) {
          if (this.pitList.contains(loc)) {
            panel.add(this.getImageLabel("pit", 80, 80));
          }
          if (this.batList.contains(loc)) {
            panel.add(this.getImageLabel("bats", 70, 70));
          }
          if (this.wumpusLoc == loc) {
            panel.add(this.getImageLabel("wumpus", 60, 60));
          }
          if ((loc == this.playerLoc) && (this.danger.get(0) || this.danger.get(1))) {
            panel.add(this.getImageLabel("stench", 60, 60));
          }
          panel.add(this.getImageLabel(this.setRoomImage(room), 100, 100));
        } else {
          panel.add(this.getImageLabel("black", 100, 100));
        }
        panel.revalidate();
      }
    }
    this.mazeMap.getParent().revalidate();
    pack();
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Rectangle bounds = env.getMaximumWindowBounds();
    int width = Math.min(getWidth(), bounds.width);
    int height = Math.min(getHeight(), bounds.height);
    setSize(new Dimension(width, height));
    revalidate();
    repaint();
  }

  /**
   * Generate a new label with selected image.
   *
   * @param type   name of image
   * @param width  width of image
   * @param height height of image
   * @return a new label with image
   */
  private JLabel getImageLabel(String type, int width, int height) {
    JLabel label = new JLabel();
    String location = "image\\%s.png";
    ImageIcon icon = new ImageIcon(String.format(location, type));
    label.setSize(width, height);
    label.setIcon(icon);
    return label;
  }

  /**
   * Return the image name of one room.
   *
   * @param room current room
   * @return image name of this room
   */
  private String setRoomImage(Room room) {
    String imageType = "";
    if (room.isTunnel()) {
      if (room.getEast() != null && room.getWest() != null) {
        imageType = "EW";
      } else if (room.getNorth() != null && room.getEast() != null) {
        imageType = "NE";
      } else if (room.getNorth() != null && room.getSouth() != null) {
        imageType = "NS";
      } else if (room.getNorth() != null && room.getWest() != null) {
        imageType = "NW";
      } else if (room.getSouth() != null && room.getEast() != null) {
        imageType = "SE";
      } else {
        imageType = "SW";
      }
    } else {
      if (room.getNorth() != null
              && room.getSouth() != null
              && room.getEast() != null
              && room.getWest() != null) {
        imageType = "NSEW";
      } else if (room.getNorth() != null && room.getEast() != null && room.getWest() != null) {
        imageType = "NEW";
      } else if (room.getNorth() != null && room.getEast() != null && room.getSouth() != null) {
        imageType = "NSE";
      } else if (room.getNorth() != null && room.getSouth() != null && room.getWest() != null) {
        imageType = "NSW";
      } else if (room.getSouth() != null && room.getEast() != null && room.getWest() != null) {
        imageType = "SEW";
      } else if (room.getNorth() != null) {
        imageType = "N";
      } else if (room.getSouth() != null) {
        imageType = "S";
      } else if (room.getEast() != null) {
        imageType = "E";
      } else {
        imageType = "W";
      }
    }
    return imageType;
  }

  @Override
  public MazeType getMazeType() {
    return menu.getGameSetting().getMazeType();
  }

  @Override
  public GameSettingPanel getGameSetting() {
    return menu.getGameSetting();
  }

  @Override
  public String getDirectionInput() {
    return this.directionInput.getText();
  }

  @Override
  public String getDistanceInput() {
    return this.distanceInput.getText();
  }

  @Override
  public JTextPane getPromptPane() {
    return this.promptPane;
  }

  @Override
  public void setListeners(ActionListener clicks, KeyListener keys) {
    this.addKeyListener(keys);
    this.startButton.addActionListener(clicks);
    this.reStartButton.addActionListener(clicks);
    this.upButton.addActionListener(clicks);
    this.leftButton.addActionListener(clicks);
    this.downButton.addActionListener(clicks);
    this.rightButton.addActionListener(clicks);
    this.shootButton.addActionListener(clicks);
    this.directionInput.addActionListener(clicks);
    this.distanceInput.addActionListener(clicks);
  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  @Override
  public void removeRestartListener(ActionListener clicks) {
    this.reStartButton.removeActionListener(clicks);
  }

  @Override
  public void removeAllListeners(ActionListener clicks) {
    for (KeyListener k : this.getKeyListeners()) {
      this.removeKeyListener(k);
    }
    this.downButton.removeActionListener(clicks);
    this.startButton.removeActionListener(clicks);
    this.upButton.removeActionListener(clicks);
    this.leftButton.removeActionListener(clicks);
    this.downButton.removeActionListener(clicks);
    this.rightButton.removeActionListener(clicks);
    this.shootButton.removeActionListener(clicks);
    this.directionInput.removeActionListener(clicks);
    this.distanceInput.removeActionListener(clicks);
  }

  @Override
  public void clearInputString() {
    directionInput.setText("");
    distanceInput.setText("");
  }

  @Override
  public void setUnchangedMark(Set<Integer> pitList, Set<Integer> batList, Integer wumpusLoc) {
    this.pitList = pitList;
    this.batList = batList;
    this.wumpusLoc = wumpusLoc;
  }

  @Override
  public void setChangingMark(List<Room> roomList, List<Boolean> danger,
                       int playerLoc, Queue<Player> playerQueue) {
    this.roomList = roomList;
    this.danger = danger;
    this.playerLoc = playerLoc;
    this.playerQueue = playerQueue;
  }

}

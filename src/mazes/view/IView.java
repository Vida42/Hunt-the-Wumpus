package mazes.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.swing.JTextPane;

import mazes.model.Maze;
import mazes.model.MazeType;
import mazes.model.Player;
import mazes.model.Room;

/**
 * The interface for our view class.
 */
public interface IView {

  /**
   * Generate an initial game view.
   *
   * @param row   the number of row of the maze
   * @param col   the number of col of the maze
   */
  void generateGameView(int row, int col);

  /**
   * Refresh the game view.
   *
   * @param row   the number of row of the maze
   * @param col   the number of col of the maze
   */
  void refreshView(int row, int col);

  /**
   * Get the maze type.
   *
   * @return the type of maze
   */
  MazeType getMazeType();

  /**
   * Get the game setting panel.
   *
   * @return the game setting panel
   */
  GameSettingPanel getGameSetting();

  /**
   * Get the direction string from the text field and return it.
   *
   * @return the direction string
   */
  String getDirectionInput();

  /**
   * Get the distance string from the text field and return it.
   *
   * @return the distance string
   */
  String getDistanceInput();

  /**
   * Get the prompt pane which show the current status of the game.
   *
   * @return the prompt pane
   */
  JTextPane getPromptPane();

  /**
   * Setters for the listeners.
   *
   * @param clicks the listener for the button clicks
   * @param keys   the listener for the keys
   */
  void setListeners(ActionListener clicks, KeyListener keys);

  /**
   * Reset the focus on the appropriate part of the view that has the keyboard
   * listener attached to it, so that keyboard events will still flow through.
   */
  void resetFocus();

  /**
   * Remover for the listeners of RestartButton.
   *
   * @param clicks the listener for the button clicks
   */
  void removeRestartListener(ActionListener clicks);

  /**
   * Remover for all listeners.
   *
   * @param clicks the listener for the button clicks
   */
  void removeAllListeners(ActionListener clicks);

  /**
   * Clear the text field.
   */
  void clearInputString();

  /**
   * Setter for some unchangeable items in the game.
   * Provide View ability to refresh view.
   *
   * @param pitList     pitList of the game
   * @param batList     batList of the game
   * @param wumpusLoc   wumpusLoc of the game
   */
  void setUnchangedMark(Set<Integer> pitList, Set<Integer> batList, Integer wumpusLoc);

  /**
   * Setter for some changeable items in the game.
   * Provide View ability to refresh view.
   *
   * @param roomList      roomList of current run
   * @param danger        danger of current run
   * @param playerLoc     playerLoc of current run
   * @param playerQueue   playerQueue of current run
   */
  void setChangingMark(List<Room> roomList, List<Boolean> danger,
                       int playerLoc, Queue<Player> playerQueue);
}

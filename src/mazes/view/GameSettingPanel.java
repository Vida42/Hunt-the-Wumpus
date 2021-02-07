package mazes.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import mazes.model.MazeType;

/**
 * This class represents a panel that user can specify all game settings.
 */
public class GameSettingPanel extends JDialog {

  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextField rowInput;
  private JTextField colInput;
  private JTextField wallInput;
  private JTextField arrowInput;
  private JTextField batInput;
  private JTextField pitInput;
  private JTextField numPlayerInput;
  private JCheckBox nonperfect;
  private JCheckBox wrapping;
  private int mazeRow;
  private int mazeCol;
  private int mazeArrow;
  private int mazePlayer;
  private int mazePit;
  private int mazeBat;
  private int mazeWall;

  /**
   * Constructor of GameSettingPanel object.
   */
  public GameSettingPanel() {
    setTitle("Game Setting");
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onOK();
      }
    });

    buttonCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    });

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    contentPane.registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
      }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onOK() {
    setMazeType();
    setMazeArrow();
    setMazeRow();
    setMazeCol();
    setMazePlayer();
    setMazePit();
    setMazeBat();
    setMazeWall();
    dispose();
  }

  private void onCancel() {
    dispose();
  }

  /**
   * Display a modal dialog.
   */
  public void displayDialog() {
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * Getter for type of maze.
   *
   * @return type of maze
   */
  public MazeType getMazeType() {
    if (nonperfect.isSelected() && ! wrapping.isSelected()) {
      return MazeType.IMPERFECT;
    }
    if (wrapping.isSelected() && ! nonperfect.isSelected()) {
      return MazeType.WRAPPING;
    }
    return MazeType.PERFECT;
  }

  /**
   * Getter for number of row of maze.
   *
   * @return number of row of maze
   */
  public int getMazeRow() {
    return this.mazeRow;
  }

  /**
   * Getter for number of col of maze.
   *
   * @return number of col of maze
   */
  public int getMazeCol() {
    return this.mazeCol;
  }

  /**
   * Getter for number of arrow of maze.
   *
   * @return number of arrow of maze
   */
  public int getMazeArrow() {
    return this.mazeArrow;
  }

  /**
   * Getter for number of players of maze.
   *
   * @return number of players of maze
   */
  public int getMazePlayer() {
    return this.mazePlayer;
  }

  /**
   * Getter for number of pits of maze.
   *
   * @return number of pits of maze
   */
  public int getMazePit() {
    return this.mazePit;
  }

  /**
   * Getter for number of bats of maze.
   *
   * @return number of bats of maze
   */
  public int getMazeBat() {
    return this.mazeBat;
  }

  /**
   * Getter for number of walls of maze.
   *
   * @return number of walls of maze
   */
  public int getMazeWall() {
    return this.mazeWall;
  }

  /**
   * Setter for type of maze.
   */
  public void setMazeType() {
    MazeType mazeType;
    if (nonperfect.isSelected() && ! wrapping.isSelected()) {
      mazeType = MazeType.IMPERFECT;
    }
    if (wrapping.isSelected() && ! nonperfect.isSelected()) {
      mazeType =  MazeType.WRAPPING;
    }
    mazeType =  MazeType.PERFECT;
  }

  /**
   * Setters for the number of rows of maze using user specified input.
   */
  public void setMazeRow() {
    mazeRow = Integer.parseInt(rowInput.getText());
  }

  /**
   * Setters for the number of cols of maze using user specified input.
   */
  public void setMazeCol() {
    mazeCol = Integer.parseInt(colInput.getText());
  }

  /**
   * Setters for the number of arrows of maze using user specified input.
   */
  public void setMazeArrow() {
    mazeArrow = Integer.parseInt(arrowInput.getText());
  }

  /**
   * Setters for the number of players of maze using user specified input.
   */
  public void setMazePlayer() {
    mazePlayer = Integer.parseInt(numPlayerInput.getText());
  }

  /**
   * Setters for the number of pits of maze using user specified input.
   */
  public void setMazePit() {
    mazePit = Integer.parseInt(pitInput.getText());
  }

  /**
   * Setters for the number of bats of maze using user specified input.
   */
  public void setMazeBat() {
    mazeBat = Integer.parseInt(batInput.getText());
  }

  /**
   * Setters for the number of walls of maze using user specified input.
   */
  public void setMazeWall() {
    mazeWall = Integer.parseInt(wallInput.getText());
  }
}

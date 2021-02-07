package mazes.view;

import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;


/**
 * This class represents a menu that user can select some items.
 */
public class MenuView extends JMenuBar {

  private final GameSettingPanel gameSettingPanel;

  /**
   * Constructor of MenuView object.
   */
  public MenuView() {
    this.gameSettingPanel = new GameSettingPanel();
    JMenu menu = new JMenu("Game Menu");
    JMenuItem gameSetting = new JMenuItem(new AbstractAction("Setting") {
      @Override
      public void actionPerformed(ActionEvent e) {
        gameSettingPanel.displayDialog();
      }
    });
    menu.add(gameSetting);
    menu.addSeparator();
    this.add(menu);
  }

  /**
   * Return game setting panel.
   *
   * @return game setting panel
   */
  public GameSettingPanel getGameSetting() {
    return gameSettingPanel;
  }
}

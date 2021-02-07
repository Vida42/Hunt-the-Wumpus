# Hunt the Wumpus

![2players mode](https://github.com/Vida42/Hunt-the-Wumpus/blob/main/res/2players-imperfect.png)


## Purpose of this project

- Design a [Hunt the Wumpus](https://youtu.be/xGVOw8gXl6Y) game which could be played in both text-based mode and GUI based mode.

> Navigate a player through a series of caves hunting a monster called the Wumpus. The game consists of a maze of caves connected by tunnels where several of the caves contain obstacles in the form of bottomless pits and superbats, as well as the Wumpus who is hiding out in one of the caves ready to eat the player.


## Functions it completed

### Model part

- Exactly 1 cave has the Wumpus. The Wumpus doesn't move but can be smelled in adjacent caves.

- There are n bottomless pits that provide a draft that can be felt in adjacent caves.

- There are m caves with superbats. Entering a cave with superbats means that there is a 50% chance that the superbats will pick up the player and drop them in another place in the maze. If there is a pit or a Wumpus there, well, you are in Fate's hands.

- If a cave has both a bottomless pit and the superbats, the bats have a 50% chance of picking up the player before they fall into the bottomless pit.

- A player can attempt to slay the Wumpus by specifying a direction and distance in which to shoot their crooked arrow. Arrows travel freely down tunnels (even crooked ones) but only travel a straight line through a cave.

- Distance must be exact. 3rd dis won't kill 2nd Wumps.

- Tunnels do not count as caves (use the definitions from the Mazes assignment remember that rooms are caves and hallways are tunnels).

- A player wins by slaying the Wumpus.

- A player loses by falling into a bottomless pit, being eaten by the Wumpus, or running out of arrows.

### Driver Part

- The driver will allow to specify the maze properties (wrapping/non-wrapping, start position, etc), the difficulty (i.e., number of pits, number of superbats, number of arrows) and any other configuration options as needed.

- The driver will then launch the controller.

### Controller Part

- Controller will work after launched by driver. It takes keyboard input, and outputs the state of the game to the screen. 

- The controller must be able to:

    - Use the input from the user to:

        - navigate the player through the maze.

        - shoot an arrow in a given direction.

    - Give clues about the nearby caves and other relevant aspects of current game state.

### View Part

- expose all game settings through menus or other controls.

- provide an option for restarting the game as a new game or as the same game.

- allow the maze to be bigger than the area allocated to it on the screen providing the ability to scroll the view.

- allow the player to move through the maze using a mouse click in addition to the keyboard arrow keys. A click on an invalid space, or pressing an invalid key would not advance the game.

- provide an option for two players where players take turns making moves or shooting arrows as they race to be the first to kill the Wumpus. In this mode, the rules of the game remain the same as before. The game ends when one player has killed the Wumpus, or both players have died. Arrows shot by a player miraculously miss the other player.

- provide a clear indication of the results of each action a player takes as well as whose turn it is.

- use Java Swing to build your graphical user interface.

- The view should begin with a mostly blank screen and display only the pieces of the maze that have been revealed by the user's exploration of the caves and tunnels. In two-player mode, the areas explored by both players should be visible.

- If the game is in two-player mode, the view must identify which player is currently taking their turn.

- Each user interaction or user input must be reasonably user-friendly.


## Assumption

- for the text based game, number of players is limited to 1

- for gui based game, number of players is limited to 1 or 2

- gui based game generate perfect maze by default(if you don't click any checkbox during setting)

- user should input reasonable number when setting, otherwise game won't start

- when user decide to shoot, input distance and direction as prompted in the game panel

- user could restart game when the game is end or whenever he/she wants

- user can directly restart the same game or restart a new game by setting new parameters


## How to Use

### General Usage

if you want to start a new text based game, locate to `.\res` and run

```
java -jar wumpus.jar --text
```

if you want to start a new gui based game, locate to `.\res` and run

```
java -jar wumpus.jar --gui
```

- When you run the command listed above, you will see a panel, click `Game Menu` then click `Setting` to input your setting. When it's done, click `OK`.

- Click `START` to start the game.

- In each user's turn, you can move or shoot.

- Use ↑, ↓, ←, → panel or type ↑, ↓, ←, → to move.

- Input distance and direction and press Shoot button to shoot.

- Which player is currently taking their turn and the current information of the user is shown in the prompt panel. When the room becomes green, it means there exists danger around current user.

- When the game ends, you can choose to directly restart the same game or restart a new game by setting new parameters through setting menu.

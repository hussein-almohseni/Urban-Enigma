package adventure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MazeGUI {
    int[][] maze1;
    int[][] maze2;
    int[][] maze3;
    int[][] maze4;
    int[][] maze5;
    int[][] selectedMaze;
    String[] startingPositions,endingPositions;
    int[][][] mazes;
    JButton[][] grid;
    JButton currentPosition,goalPosition;
    JFrame window, frame;
    JLabel statsLabel;
    IntWrapper strength, intelligence;
    boolean boost;

    MazeGUI(JFrame mainFrame,IntWrapper strengthRef,IntWrapper intelligenceRef,JLabel statsLabel,boolean hasBoost) {
        frame = mainFrame; // reference main window
        this.statsLabel = statsLabel;
        strength = strengthRef;
        intelligence = intelligenceRef;
        boost = hasBoost;
        window = new JFrame("Pathfinder");
        window.setLayout(new GridLayout(12, 12)); // each maze is 12x12
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.setVisible(true);
                window.dispose();
            }
        });
        grid = new JButton[12][12];
        currentPosition = new JButton(); // reference to the current position of the user
        goalPosition = new JButton(); // reference to the goal position
        startingPositions = new String[5]; // array to hold all 5 starting positions of the mazes
        endingPositions = new String[5]; // array to hold all 5 goal positions of the mazes
        //each of the mazes is 12x12 and consist of 1s and 0s where 1s are open path while 0s are walls
        maze1 = new int[][]{
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0},
                {0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0},
                {0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0},
                {0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0},
                {0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0},
                {0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0},
                {0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}
        };
        // set up the starting and ending position of maze1
        startingPositions[0] = "0,1";
        endingPositions[0] = "11,10";

        maze2 = new int[][]{
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0},
                {0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 0},
                {0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0},
                {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0},
                {0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0},
                {0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        // set up the starting and ending position of maze2
        startingPositions[1] = "0,5";
        endingPositions[1] = "6,0";

        maze3 = new int[][]{
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0},
                {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}
        };
        // set up the starting and ending position of maze3
        startingPositions[2] = "0,5";
        endingPositions[2] = "11,6";

        maze4 = new int[][]{
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
                {0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0},
                {0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0},
                {0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0},
                {0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        // set up the starting and ending position of maze4
        startingPositions[3] = "0,6";
        endingPositions[3] = "1,11";

        maze5 = new int[][]{
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1},
                {0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0},
                {0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                {0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        // set up the starting and ending position of maze5
        startingPositions[4] = "0,1";
        endingPositions[4] = "4,11";
        // add all 5 mazes into an array of mazes
        mazes = new int[][][]{maze1, maze2, maze3, maze4, maze5};
        int randomIndex = new Random().nextInt(5);
        selectedMaze = mazes[randomIndex]; // select a random maze
        
        // for loop to create all buttons and change their color to white if they're an available path or black 
        // if they're a wall and sets the ActionCommand equal to the coordinates for easier lookup later
        for (int i = 0; i < selectedMaze.length; i++) {
            for (int j = 0; j < selectedMaze[i].length; j++) {
                grid[i][j]=new JButton();
                if(selectedMaze[i][j]==0)
                    grid[i][j].setBackground(Color.BLACK);
                else
                    grid[i][j].setBackground(Color.WHITE);
                grid[i][j].addActionListener(new buttonListener());
                grid[i][j].setActionCommand(i+","+j);
                window.add(grid[i][j]);
            }
        }
        String[] startingCoordinates = startingPositions[randomIndex].split(",");
        int row = Integer.parseInt(startingCoordinates[0]);
        int col = Integer.parseInt(startingCoordinates[1]);
        currentPosition=grid[row][col];
        startingCoordinates = endingPositions[randomIndex].split(",");
        row = Integer.parseInt(startingCoordinates[0]);
        col = Integer.parseInt(startingCoordinates[1]);
        goalPosition=grid[row][col];
        currentPosition.setBackground(Color.BLUE);
        goalPosition.setBackground(Color.RED);
        window.setSize(600,600);
        window.setVisible(true);
        window.setLocationRelativeTo(mainFrame); // center the window
        window.setFocusable(true); // make the window focusable to implement hotkeys
        // key listener for the four arrows to navigate the maze
        window.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                String[] coordinates=currentPosition.getActionCommand().split(",");
                int row = Integer.parseInt(coordinates[0]);
                int col = Integer.parseInt(coordinates[1]);

                if(e.getKeyCode()==KeyEvent.VK_UP){ //  handles the user clicking the up arrow
                    row--;
                    if(row<0||selectedMaze[row][col]==0)
                        return;
                } else if(e.getKeyCode()==KeyEvent.VK_DOWN){ //  handles the user clicking the down arrow
                    row++;
                    if(row>11||selectedMaze[row][col]==0)
                        return;
                } else if(e.getKeyCode()==KeyEvent.VK_RIGHT){ //  handles the user clicking the right arrow
                    col++;
                    if(col>11||selectedMaze[row][col]==0)
                        return;
                } else if(e.getKeyCode()==KeyEvent.VK_LEFT){ //  handles the user clicking the left arrow
                    col--;
                    if(col<0||selectedMaze[row][col]==0)
                        return;
                }
                if(grid[row][col]==goalPosition){
                    handleWin();
                    return;
                }
                currentPosition.setBackground(Color.WHITE);
                currentPosition=grid[row][col];
                currentPosition.setBackground(Color.BLUE);
            }
        });
        //Source: https://stackoverflow.com/questions/14011492/text-wrap-in-joptionpane
        JOptionPane.showMessageDialog(
                window,
                "<html><body><p style='width: 300px;'>"+"Welcome to Pathfinder. Your goal is to find the " +
                        "path to the red block. You are only permitted to move up, down, right, or left of your " +
                        "current position which is a blue block as long as it's not a wall(black block)." +
                        " You may move by either pressing the spot you're looking to go to or using the arrows on " +
                        "your keyboard." +
                        "</p></body></html>",
                "Rules",
                JOptionPane.PLAIN_MESSAGE);
        window.requestFocus();
    }
    private class buttonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            checkDirection(e.getActionCommand());
            window.requestFocus();
        }
    }
    private void checkDirection(String coords){
        String[] coordinates = coords.split(",");
        int chosenRow = Integer.parseInt(coordinates[0]);
        int chosenCol = Integer.parseInt(coordinates[1]);
        if(selectedMaze[chosenRow][chosenCol]==0){
            JOptionPane.showMessageDialog(window,"You can't go on walls");
            return;
        }
        coordinates=currentPosition.getActionCommand().split(",");
        int row = Integer.parseInt(coordinates[0]);
        int col = Integer.parseInt(coordinates[1]);
        String up=(row-1)+","+col;
        String down=(row+1)+","+col;
        String right=row+","+(col+1);
        String left=row+","+(col-1);
        if(!coords.equals(up)&&!coords.equals(down)&&!coords.equals(right)&&!coords.equals(left))
            JOptionPane.showMessageDialog(window,"You can only go up, down, right, " +
                    "or left of your current position");
        else if(coords.equals(goalPosition.getActionCommand())){
            handleWin();
        }
        else{
            currentPosition.setBackground(Color.WHITE);
            currentPosition=grid[chosenRow][chosenCol];
            currentPosition.setBackground(Color.BLUE);
        }
    }
    private void handleWin(){
        //ask if they want a new game
        currentPosition.setBackground(Color.WHITE);
        goalPosition.setBackground(new Color(128, 0, 128));// change the color of the goalPosition to purple when the user wins
        JOptionPane.showMessageDialog(window,"You have won!");
        if(boost)
            intelligence.value+=75;
        else
            intelligence.value+=50;
        statsLabel.setText("Strength: "+strength.value+"          Intelligence: "+intelligence.value);
        frame.setVisible(true);
        window.dispose();
    }
}
package adventure;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class FourxFourTicTacToe {
    JFrame window;
    JFrame frame; //reference main frame
    JPanel topPanel, playersPanel, turnPanel, centerPanel, buttonsPanel, bottomPanel;
    JLabel player1Label, player2Label, player1WinLabel, player2WinLabel, turnLabel;
    JLabel player1, player2;
    String X, O;
    JButton[][] buttons;
    JButton exit;
    JTextArea gameLog;
    JScrollPane gameLogPanel;
    String[][] board = new String[4][4];
    int gameNum=1, player1Wins = 0, player2Wins = 0;
    IntWrapper strength,intelligence; //reference the strength stat
    JLabel statsLabel;
    public FourxFourTicTacToe(JFrame mainFrame,IntWrapper strengthRef,IntWrapper intelligenceRef,JLabel statsLabel) {
        window = new JFrame("Tic Tac Toe Game");
        //if the user trys to exit the mini-game, disposes of the window and makes the main frame visible
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.setVisible(true);
                window.dispose();
            }
        });
        window.setLayout(new BorderLayout());
        topPanel = new JPanel(new GridLayout(1, 2));
        playersPanel = new JPanel(new GridLayout(2, 3));
        turnPanel = new JPanel();
        centerPanel = new JPanel(new BorderLayout());
        buttonsPanel = new JPanel(new GridLayout(4, 4));
        gameLogPanel = new JScrollPane();
        bottomPanel = new JPanel(new GridLayout(1, 1));

        strength = strengthRef;
        intelligence = intelligenceRef;
        frame=mainFrame;
        X="You";
        O="Computer";
        this.statsLabel=statsLabel;

        //players panel components
        //Player1
        player1Label = new JLabel("Player 1(X)");
        player1WinLabel = new JLabel("Wins: 0");
        //Player2
        player2Label = new JLabel("Player 2(O)");
        player2WinLabel = new JLabel("Wins: 0");
        //players panel setup
        playersPanel.setBorder(BorderFactory.createTitledBorder("Players"));
        playersPanel.add(player1Label);
        player1 = new JLabel(X);
        playersPanel.add(player1);
        playersPanel.add(player1WinLabel);
        playersPanel.add(player2Label);
        player2 = new JLabel(O);
        playersPanel.add(player2);
        playersPanel.add(player2WinLabel);

        //turn panel setup
        turnLabel = new JLabel("");
        turnPanel.setBorder(BorderFactory.createTitledBorder("Turn"));
        turnPanel.add(turnLabel);

        //top panel setup
        topPanel.add(playersPanel);
        topPanel.add(turnPanel);
        //bottons panel setup
        //initialize buttons
        //set the action command of the buttons to their row,column
        //add the buttons to the buttons panel
        buttons = new JButton[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = new JButton("");  // Initialize the button
                buttons[i][j].setActionCommand(i + "," + j);  // Set the action command
                buttons[i][j].addActionListener(new FourxFourTicTacToe.buttonListener());  // Add action listener
                buttonsPanel.add(buttons[i][j]);  // Add button to panel
            }
        }


        //game log panel setup
        gameLogSetup();

        //center panel setup
        centerPanel.add(buttonsPanel, BorderLayout.CENTER);
        centerPanel.add(gameLogPanel, BorderLayout.EAST);

        //bottom panel setup
        exit = new JButton("Exit");
        bottomPanel.add(exit);
        exit.addActionListener(e ->{
            frame.setVisible(true);
            window.dispose();
        });

        //frame setup
        window.add(topPanel, BorderLayout.NORTH);
        window.add(centerPanel, BorderLayout.CENTER);
        window.add(bottomPanel, BorderLayout.SOUTH);
        window.setSize(700, 500);
        window.setLocationRelativeTo(frame);
        window.setVisible(true);

        //Display a welcome message to explain the rules
        JOptionPane.showMessageDialog(
                window,
                "<html><body><p style='width: 300px;'>"+"Welcome to Tic Tac Toe. Your goal is win " +
                        "against a computer in a 4x4 grid. You can win by marking four spots that are aligned " +
                        "horizontally, vertically, or diagonally. At anytime in the game, you may go back to the main" +
                        " game by closing the window or clicking \"Exit.\" </p></body></html>",
                "Rules",
                JOptionPane.PLAIN_MESSAGE);

        //randomly determine who starts
        Random rand = new Random();
        int chosen=rand.nextInt(2);
        if(chosen==0) {
            turnLabel.setText(X);
            gameLog.append("You start\n");
        }else {
            turnLabel.setText(O);
            gameLog.append("Computer starts\n");
        }
        if(turnLabel.getText().equals("Computer")){
            computerTurn();
            handleWinner(checkForWinner());
        }

    }

    //ActionListener class for the buttons
    private class buttonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            //Since I deliberately set up the action command of the buttons, I can parse the row and col
            int row = Integer.parseInt(command.substring(0, 1));
            int col = Integer.parseInt(command.substring(2));
            if (board[row][col] == null) {
                if (turnLabel.getText().equals(X))
                    board[row][col] = "X";
                else if(!O.equals("Computer"))
                    board[row][col] = "O";
                //update the appropriate button
                if(turnLabel.getText().equals(X)||!O.equals("Computer")) {
                    buttons[row][col].setText(board[row][col]);
                } else
                    computerTurn();
                handleWinner(checkForWinner());
                if(turnLabel.getText().equals("Computer")) {
                    computerTurn();
                    handleWinner(checkForWinner());
                }

            }
        }

    }
    private void gameLogSetup() {
        gameLog = new JTextArea();
        gameLog.setEditable(false);
        gameLogPanel.setBorder(BorderFactory.createTitledBorder("Game Log:"));
        gameLogPanel.setViewportView(gameLog);
        gameLogPanel.setPreferredSize(new Dimension(200, 0));
        //adding a horizontal and vertical scroll bars
        gameLogPanel.setHorizontalScrollBar(new JScrollBar(Adjustable.HORIZONTAL));
        gameLogPanel.setVerticalScrollBar(new JScrollBar(Adjustable.VERTICAL));
        //makes the scrollbars always visible
        gameLogPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        gameLogPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gameLogPanel.scrollRectToVisible(gameLog.getBounds());
    }
    private String checkForWinner() {
        //default value for draw
        String winner = "Draw";
        for (int i = 0; i < board.length; i++) {
            if (board[i][0] != null && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])&&board[i][2].equals(board[i][3]))
                winner = board[i][0];
            else if (board[0][i] != null && board[0][i].equals(board[1][i])&&board[1][i].equals(board[2][i]) && board[2][i].equals(board[3][i]))
                winner = board[0][i];
        }
        //diagonal checks
        if (board[0][0] != null && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])&&board[2][2].equals(board[3][3]))
            winner = board[0][0];
        else if (board[0][3] != null && board[0][3].equals(board[1][2]) && board[1][2].equals(board[2][1])&&board[2][1].equals(board[3][0]))
            winner = board[0][3];
        if (winner.equals("X"))
            winner = X;
        else if (winner.equals("O"))
            winner = O;
        if (!winner.equals(X) && !winner.equals(O)) {
            for (String[] strings : board) {
                for (int j = 0; j < board[0].length; j++) {
                    if (strings[j] == null)
                        return "";
                }
            }
        }
        return winner;
    }
    //function to update the game log based on the winner
    //given a string with the winner
    private void handleWinner(String winner){
        if (!winner.isEmpty()) {
            if (winner.equals("Draw")){
                gameLog.append("Game " + gameNum + ": Tie\n");
                JOptionPane.showMessageDialog(window, "Draw!");
                clearArrays();
                gameNum++;
            }else {
                gameLog.append("Game " + gameNum + ": " + winner + " Won\n");
                if (winner.equals(X)) {
                    player1Wins++;
                    player1WinLabel.setText("Wins: " + player1Wins);
                    if(player1Wins ==1){
                        //increase intelligence by 40
                        window.dispose();
                        strength.value+=100;
                        statsLabel.setText("Strength: "+strength.value+"          Intelligence: "+intelligence.value);
                        frame.setVisible(true);
                        window.dispose();
                    }
                } else {
                    player2Wins++;
                    player2WinLabel.setText("Wins: " + player2Wins);
                }
                JOptionPane.showMessageDialog(window, winner + " Won!");
                //clear buttons and array
                clearArrays();
                gameNum++;
                if (turnLabel.getText().equals(X)) {
                    turnLabel.setText(O);
                } else
                    turnLabel.setText(X);
            }
        } else if (turnLabel.getText().equals(O))
            turnLabel.setText(X);
        else
            turnLabel.setText(O);
    }

    private void clearArrays() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = null;
                buttons[i][j].setText("");
            }
        }
    }
    private void computerTurn(){
        int numOfAvailableOptions=0;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j]==null)
                    numOfAvailableOptions++;
            }
        }
        String[] availableOptions = new String[numOfAvailableOptions];
        int index=0, randomIndex;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j]==null){
                    availableOptions[index]=buttons[i][j].getActionCommand();
                    index++;
                }
            }
        }
        Random rand = new Random();
        randomIndex = rand.nextInt(numOfAvailableOptions);
        //change the Text of the appropriate button
        String command = availableOptions[randomIndex];
        int row = Integer.parseInt(command.substring(0, 1));
        int col = Integer.parseInt(command.substring(2));
        buttons[row][col].setText("O");
        board[row][col] = "O";
    }
}

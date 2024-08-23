import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GomokuXO {
    int boardWidth = 800;
    int boardHeight = 850; //extra 50px for the panel on top

    JFrame frame = new JFrame("Gomoku (5 in a row)");
    Color wood = new Color(210, 180, 140);
    JLabel label = new JLabel();
    JPanel titlePanel = new JPanel();
    JPanel boardPanel = new JPanel();

    JButton[][] board = new JButton[15][15]; //keep tracks of placement of each button
    String playerX = "X";
    String playerY = "O";
    String currentPlayer = playerX;

    boolean GameEnd = false;
    int turns = 0;

    GomokuXO()
    {
        frame.setVisible(true); //makes the frame visible
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); //opens the window at the center of the screen
        frame.setResizable(false); //users can change it dimension by dragging the edges
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //terminate the program if the frame is closed
        frame.setLayout(new BorderLayout());//divide container into N, W, E, S, and Center
        
        label.setBackground(wood);
        label.setForeground(Color.white);
        label.setFont(new Font("Arial", Font.BOLD, 50));
        label.setHorizontalAlignment(JLabel.CENTER); 
        label.setText("Gomoku (5 in a row)");
        label.setOpaque(true); //so you can see the background color

        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(label);
        frame.add(titlePanel, BorderLayout.NORTH); //dont forget to add it into the frame

        boardPanel.setLayout(new GridLayout(15, 15)); //divide container into grids
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);

        //making the 2d array represent the board, and keep track of the button
        for (int row = 0; row < 15; row++)
        {
            for (int col = 0; col < 15; col++)
            {
                JButton tile = new JButton();
                board[row][col] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 23));
                tile.setFocusable(false); //makes it so that the user cannot interact with the buttons through keyboard

                tile.addActionListener(new ActionListener() { //addActionListener is when you press the button functions executed
                    public void actionPerformed(ActionEvent e)
                    {
                        if (GameEnd) return;
                        JButton tile = (JButton)e.getSource(); //we know its a button and not different sources
                        if (tile.getText() == "") //locks each tile so it cant be replaced
                        {
                            tile.setText(currentPlayer);
                            turns++;
                            GameEnd = checkWinners();
                            if (!GameEnd)
                            {
                                currentPlayer = currentPlayer == playerX ? playerY : playerX; //alternate turns
                                label.setText(currentPlayer + "'s turn");

                            }

                        }

                    }
                }); 
            }
        }

    }

    public boolean checkWinners()
    {
        //check each cell one by one
        for (int row = 0; row < 15; row++)
        {
            for (int col = 0; col < 15; col++)
            {
                String currentPlayer = board[row][col].getText();

                if (currentPlayer != "") //skip empty cells
                { //0 first slot means we do not move vertically, 1 means we move down. 0 second slot no move horizontaly, 1 is do, and -1 is backwards
                    if (checkDirection(row, col, 0, 1, currentPlayer) || //horizontally
                        checkDirection(row, col, 1, 0, currentPlayer) || //vertically
                        checkDirection(row, col, 1, 1, currentPlayer) || //diagonal down right
                        checkDirection(row, col, 1, -1, currentPlayer))//diagonal down left
                        {
                           return true;
                        }
                }

            }
        }
        if (turns == 225) //for tie condition
        {
            for (int row = 0; row < 15; row++)
            {
                for (int col = 0; col < 15; col++)
                {
                    setTie(board[row][col]);
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkDirection(int row, int col, int rowMove, int colMove, String currentPlayer) {
        int count = 0;
        JButton[] winningTiles = new JButton[5]; // Array to store the winning tiles

        for (int i = 0; i < 5; i++) {
            int currentRow = row + i * rowMove; //the "Move" will adjust the tile to choose the correct one to check
            int currentCol = col + i * colMove;

            if (currentRow >= 0 && currentRow < 15 && //within bounds and it remains the same as the player
                currentCol >= 0 && currentCol < 15 &&
                board[currentRow][currentCol].getText().equals(currentPlayer))
            {
                winningTiles[count] = board[currentRow][currentCol];
                count++;
            } else {
                break;
            }
        }

        if (count == 5) { // If we found a winning combination, highlight it
            for (JButton tile : winningTiles) {
                if (tile != null) {
                    tile.setBackground(Color.GREEN);
                    label.setText(currentPlayer + " is the winner");
                }
            }
            return true;
        }

        return false;
    }

    public void setTie(JButton tile)
    {
        tile.setForeground(Color.orange);
        tile.setBackground(Color.gray);
        label.setText("It was a Tie! No more space is available.");
    }

}

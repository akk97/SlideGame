/**
 * Class for 2048 game
 *
 * @author Anika Kaur
 */


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.util.Random;

public class SlideGame extends Application implements EventHandler<ActionEvent> {

    //fields for gameBoard, gridPane, buttons
    private int[][] gameBoard;
    private GridPane g1;
    private Button[][] b1;
    private int numColumns;
    private int numRows;


    public void start(Stage stage1) {

        //parameters for command line arguments
        Parameters params = getParameters();

        //gets the arguments
        java.util.List<String> args = params.getRaw();

        //parameters
        int numRows;
        int numColumns;

        // checks for 2 arguments in command line
        if (args.size() >= 2 && Integer.parseInt(args.get(0)) <= 10 && Integer.parseInt(args.get(1)) <= 10) {

            numColumns = Integer.parseInt(args.get(0));
            numRows = Integer.parseInt(args.get(1));

        }

        else  {

            //default dimensions
            numColumns = 4;
            numRows = 4;

        }

        //creates gridpane and buttons
        gameBoard = new int[numRows][numColumns];
        g1 = new GridPane();
        b1 = new Button[numRows][numColumns];




        //starts game by calling game method
        gameStart();


        //scene setup
        Scene scene = new Scene(g1);
        stage1.setScene(scene);
        stage1.show();


    }

    /**
     * gameStart method creates a default grid with 0s on all buttons and randomly makes one button 1
     *
     */

    public void gameStart() {

        //random button as 1
        Random initial = new Random();
        int rowIndex = initial.nextInt(gameBoard.length);
        int columnIndex = initial.nextInt(gameBoard[0].length);


        //for loop for creating array of buttons
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if(i == rowIndex && j == columnIndex)  {
                    gameBoard[i][j] = 1;
                }
                else {
                    gameBoard[i][j] = 0;
                }
                b1[i][j] = new Button(Integer.toString(gameBoard[i][j]));
                g1.add(b1[i][j], j, i);
                b1[i][j].setOnAction(this);
            }
        }


    }

    /**
     * Handle method starts the game(overidden) and uses different helper methods based on
     *
     * @param actionevent e that is used to start the game methods
     */

    @Override

    public void handle(ActionEvent e) {
        Button clickedButton = (Button)e.getSource();

        // field represents column of button clicked
        int columnIndex2 = g1.getColumnIndex(clickedButton);
        //field represents row of button clicked
        int rowIndex2 =  g1.getRowIndex(clickedButton);

        // keeps track of number of rows
        int numColumns = b1[0].length;
        // keeps track of number of columns
        int numRows = b1.length;

        // calls leftSwipe method if button is on left column that is not a corner is called
        if (columnIndex2 == 0 && rowIndex2 > 0 && rowIndex2 < numRows - 1) {
            leftSwipe(rowIndex2);
        }

        //calls rightSwipe method if button is on right column that is not a corner is called
        if (columnIndex2 == numColumns - 1 && rowIndex2 > 0 && rowIndex2 < numRows - 1)  {
            rightSwipe(rowIndex2);
        }

        //calls downSwipe method if button is on bottom row and is not a corner
        if (rowIndex2 == numRows - 1 && columnIndex2 > 0 && columnIndex2 < numColumns - 1)  {
            downSwipe(columnIndex2);
        }

        //calls upSwipe method if button is on the top row is not a corner
        if (rowIndex2 == 0 && columnIndex2 > 0 && columnIndex2 < numColumns - 1)  {
            upSwipe(columnIndex2);
        }

        //calls tlSwipe if the clicked button is the top left corner
        if (columnIndex2 == 0 && rowIndex2 == 0)  {
            TLSwipe(rowIndex2);
        }

        //calls br swipe if the clicked button is the bottom right corner
        if (columnIndex2 == numColumns - 1 && rowIndex2 == numRows - 1) {
            BRSwipe(rowIndex2);
        }

        //calls the lbSwipe if the clicked button is the left bottom corner
        if (columnIndex2 == 0 && rowIndex2 == numRows - 1)   {
            LBSwipe(rowIndex2);
        }

        //calls the trSwipe method if the top right button is clicked
        if (columnIndex2 == numColumns - 1 && rowIndex2 == 0)    {
            TRSwipe(rowIndex2);
        }
    }

    /**
     * Method randomly generated indices on board and places 1 there
     *
     * @param clicked button indices
     */

    private void generateRandom(int rowIndex, int columnIndex) {
        Random rand = new Random();

        //randomly generated indices for column and row of the button
        int rowIndex2 = rand.nextInt(gameBoard.length);
        int columnIndex2 = rand.nextInt(gameBoard[0].length);

        // while loop checks if randomly generated indices are not 0 and then placed 1 at that point
        while (gameBoard[rowIndex2][columnIndex2] != 0)  {
            rowIndex2 = rand.nextInt(gameBoard.length);
            columnIndex2 = rand.nextInt(gameBoard[0].length);
        }
        gameBoard[rowIndex2][columnIndex2] = 1;

    }



    /**
     * method for leftSwipe move
     *
     * @param clicked row index
     */

    public void leftSwipe(int rowIndex2)  {

        // represents row of clicked button
        int[] currentRow = gameBoard[rowIndex2];
        // boolean to keep track of whether same tiles are merged
        boolean merged = false;

        //loop to increment through the row based on left move , no precondition
        for (int i = 1; i < currentRow.length; i++) {

            if (currentRow[i] != 0) {
                // will try to merge or move tile if there is a button that is not 0, button in row needs to not be 0
                for (int j = i - 1; j >= 0; j--) {
                    if (currentRow[j] == 0) {

                        // shift each value left
                        currentRow[j] = currentRow[j + 1];
                        currentRow[j + 1] = 0;
                    }
                    else if (currentRow[j] == currentRow[j + 1] && !merged) {
                        // merging cells
                        currentRow[j] *= 2;
                        currentRow[j + 1] = 0;
                        merged = true;
                    }
                }
            }

            merged = false;
        }


        // update buttons
        updateGameBoard();

        // generate a random position for the new 1(calls random method)
        generateRandom(rowIndex2, 0);
    }

    /**
     * method for rightSwipe move
     *
     * @param clicked row index
     */
    public void rightSwipe(int rowIndex2)   {
        //represents row of clicked button
        int[] currentRow = gameBoard[rowIndex2];
        // boolean checks if tiles are merged
        boolean merged = false;

        //loop increments through row based on right swipe, no precondition
        for (int i = currentRow.length - 2; i >= 0; i--)  {

            if (currentRow[i] != 0)   {
                // will try to merge or move tile if current tile is not 0, tile must not be 0
                for (int j = i + 1; j < currentRow.length; j++) {
                    if (currentRow[j] == 0)  {
                        // shift each value right
                        currentRow[j] = currentRow[j-1];
                        currentRow[j - 1] = 0;
                    }
                    else if (currentRow[j] == currentRow[j - 1] && !merged)  {
                        //merges tiles
                        currentRow[j] *= 2;
                        currentRow[j - 1] = 0;
                        merged = true;
                    }
                }
            }
            merged = false;

        }
        //updates gameBoard
        updateGameBoard();
        //generates a random index for 1 and puts 1 on the board(calls random method)
        generateRandom(rowIndex2, 0);
    }


    /**
     * method for downSwipe move
     *
     * @param clicked column index
     */
    public void downSwipe(int columnIndex2)  {
        //represents current row of clicked button
        int[] currentColumn = new int[gameBoard.length];

        // increments through column to keep track of numbers in currentColumn array, no precondition
        for (int i = 0; i < gameBoard.length; i++)  {
            currentColumn[i] = gameBoard[i][columnIndex2];
        }
        //keeps tracked of whether similar tiles are merged
        boolean merged = false;

        // increments through currentColumn for downSwipe(uses both i and j as 2d array)
        for (int i = currentColumn.length - 2; i >= 0; i--)  {
            if (currentColumn[i] != 0) {
                // will try to merge or shift tile that is not 0, must have tile that is not 0
                for (int j = i + 1; j  < currentColumn.length; j++) {
                    if (currentColumn[j] == 0)  {
                        //shifts tiles down
                        currentColumn[j] = currentColumn[j - 1];
                        currentColumn[j - 1] = 0;
                    }
                    else if (currentColumn[j] == currentColumn[j - 1] && !merged)  {
                        //merges tiles
                        currentColumn[j] *= 2;
                        currentColumn[j - 1] = 0;
                        merged = true;
                    }
                }
            }
            merged = false;
        }

        //updates the column on gameBoard, no precondition
        for (int i = 0; i < gameBoard.length; i ++) {
            gameBoard[i][columnIndex2] = currentColumn[i];
        }
        //updates gameBoard
        updateGameBoard();
        //generates random indices and puts 1 there(calls random method)
        generateRandom(git0, columnIndex2);
    }

    /**
     * method for upSwipe move
     *
     * @param clicked column index
     */
    public void upSwipe(int columnIndex2)  {
        //represents column of clicked button
        int[] currentColumn = new int[gameBoard.length];

        // increments through column to keep track of numbers in currentColumn array, no precondition
        for (int i = 0; i < gameBoard.length; i++)  {
            currentColumn[i] = gameBoard[i][columnIndex2];
        }
        // boolean keeps track of whether tiles are merged
        boolean merged = false;

        // increments through currentColumn for upSwipe(uses i and j for 2d array), no precondition
        for (int i = 1; i < currentColumn.length; i++)  {
            if (currentColumn[i] != 0)  {

                //// will try to merge or shift tile that is not 0, must have tile that is not 0
                for (int j = i - 1; j >= 0; j --) {
                    if (currentColumn[j] == 0) {
                        //shifts tiles upward
                        currentColumn[j] = currentColumn[j + 1];
                        currentColumn[j + 1] = 0;
                    }

                    else if (currentColumn[j] == currentColumn[j + 1] && !merged)  {
                        //merges similar tiles downward
                        currentColumn[j] *= 2;
                        currentColumn[j + 1] = 0;
                        merged = true;
                    }
                }
            }
            merged = false;
        }
        //updates column gameBoard , no precondition
        for (int i = 0; i < gameBoard.length; i++) {
            gameBoard[i][columnIndex2] = currentColumn[i];
        }
        //calls update Gameboard method
        updateGameBoard();
        //generates random indices for 1(calls random method)
        generateRandom(0, columnIndex2);
    }


    /**
     * method for tlSwipe move
     *
     * @param clicked column index
     */
    public void TLSwipe(int columnIndex2) {

        // Checks diagonal from bottom right to top left, no precondition(left)
        for (int i = gameBoard.length - 1; i >= 0; i--) {
            //checks diagonal from botto  right to tope left(up), no precondition
            for (int j = gameBoard[i].length - 1; j >= 0; j--) {
                // represents the button of the clicked button
                int currentButton = gameBoard[i][j];

                // Move right if button there is not 0
                if (currentButton != 0) {
                    int k = 1;
                    //keeps track of diagonal tiles, precondition is tile is not 0
                    while (i - k >= 0 && j - k >= 0 && gameBoard[i - k][j - k] == 0) {
                        k++;
                    }

                    // Check if diagonal movement occurred and if the next tile can be merged
                    if (i - k >= 0 && j - k >= 0 && gameBoard[i - k][j - k] == currentButton) {
                        // Merge the tiles and shifts tiles
                        gameBoard[i - k][j - k] *= 2;
                        gameBoard[i][j] = 0;
                        //calls update GameBoard
                        updateGameBoard();

                        return;
                    }
                }
            }
        }

        // Updates gameBoard
        updateGameBoard();
        // generates random button for 1(random)
        generateRandom(0, columnIndex2);

    }


    /**
     * method for BRSwipe move
     *
     * @param clicked column index
     */
    public void BRSwipe(int columnIndex2) {
        // Check diagonal from top left to bottom right(right), no precondition
        for (int i = 0; i < gameBoard.length; i++) {
            //Checks diagonal from left top to bottom right(up) , no precondition
            for (int j = 0; j < gameBoard[i].length; j++) {
                // represents the button of the clicked button
                int currentButton = gameBoard[i][j];

                // Move left if button there is not 0
                if (currentButton != 0) {
                    int k = 1;
                    //keeps track of diagonal tiles, precondition is tile is not 0
                    while (i + k < gameBoard.length && j + k < gameBoard[i].length && gameBoard[i + k][j + k] == 0) {
                        k++;
                    }

                    // Check if diagonal movement occurred and if the next tile can be merged
                    if (i + k < gameBoard.length && j + k < gameBoard[i].length && gameBoard[i + k][j + k] == currentButton) {
                        // Merge the tiles and shifts tiles
                        gameBoard[i + k][j + k] *= 2;
                        gameBoard[i][j] = 0;
                        //calls updateGameBoard method
                        updateGameBoard();
                        return;
                    }
                }
            }
        }

        // UpdateGameBoard method
        updateGameBoard();
        //randomly generates random button as 1(calls generate random method)
        generateRandom(0, columnIndex2);

    }

    /**
     * method for TRSwipe move
     *
     * @param clicked column index
     */
    public void TRSwipe(int columnIndex2) {

        // Check diagonal from top right to bottom left, no precondition(right)
        for (int i = 0; i < gameBoard.length - 1; i++) {
            // Check diagonal from top right to bottom left, no precondition(up)
            for (int j = gameBoard[i].length - 1; j >= 0; j--) {
                // represents the button of the clicked button
                int currentButton = gameBoard[i][j];

                // Move right if button there is not 0
                if (currentButton != 0) {
                    int k = 1;
                    //keeps track of diagonal buttons,precondition tile is not 0
                    while (i + k < gameBoard.length && j - k >= 0 && gameBoard[i + k][j - k] == 0) {
                        k++;
                    }

                    // Check if diagonal movement occurred and if the next tile can be merged
                    if (i + k < gameBoard.length && j - k >= 0 && gameBoard[i + k][j - k] == currentButton) {
                        // Merge the tiles and shifts tiles
                        gameBoard[i + k][j - k] *= 2;
                        gameBoard[i][j] = 0;
                        //calls upDate GameBoard
                        updateGameBoard();

                        return;
                    }
                }
            }
        }

        // Update  game board
        updateGameBoard();
        //generates random button for 1(random method)
        generateRandom(0, columnIndex2);

    }

    /**
     * method for LBSwipe move
     *
     * @param clicked column index
     */
    public void LBSwipe(int columnIndex2) {

        // Check diagonal from top left to bottom right(left), no precondition
        for (int i = 0; i < gameBoard.length - 1; i++) {
            // Check diagonal from top left to bottom right(up), no precondition
            for (int j = 0; j < gameBoard[i].length - 1; j++) {
                // represents the button of the clicked button
                int currentButton = gameBoard[i][j];

                // move left if button is not 0
                if (currentButton != 0) {
                    int k = 1;
                    //keeps track of diagonal buttons , current tile should not be 0
                    while (i + k < gameBoard.length && j + k < gameBoard[i].length && gameBoard[i + k][j + k] == 0) {
                        k++;
                    }

                    // Check if diagonal movement occurred and if the next tile can be merged
                    if (i + k < gameBoard.length && j + k < gameBoard[i].length && gameBoard[i + k][j + k] == currentButton) {
                        // Merge the tiles and shifts tiles
                        gameBoard[i + k][j + k] *= 2;
                        gameBoard[i][j] = 0;

                        //calls updateGameBoard method
                        updateGameBoard();

                        return;
                    }
                }
            }
        }

        // Update Game Board method
        updateGameBoard();
        //generates a random button as 1(calls random method)
        generateRandom(0, columnIndex2);

    }



    /**
     * method for updating gameBoard based on the method selected
     */
    private void updateGameBoard()  {
        //keeps track of tiles in the method, no precondition(left and right)
        for(int i = 0; i < gameBoard.length; i++) {
            //keeps track of tiles in the method, no precondition(up and down)
            for(int j = 0; j < gameBoard[i].length; j++) {

                //sets number on buttons and adds color
                b1[i][j].setText(Integer.toString(gameBoard[i][j]));
                addColor(i ,j);
            }
        }



    }

    /**
     * method for changing color of button based on number
     */
    public void addColor(int i, int j){
        //keeps track of what button to change color
        int colorButton = gameBoard[i][j];
        //keeps track of what color to add to the button
        String colorStyle = "-fx-background-color:";

        //case switch to change color based on number
        switch(colorButton) {
            case 1:
                colorStyle += "#FFFFFF";
                break;
            case 2:
                colorStyle += "#FF0000";
                break;
            case 4:
                colorStyle += "#FFA500";
                break;
            case 8:
                colorStyle += "#FFFF00";
                break;
            case 16:
                colorStyle += "#00FF00";
                break;
            case 32:
                colorStyle += "#00FF00";
                break;
            case 64:
                colorStyle += "#00FFFF";
                break;
            case 128:
                colorStyle += "#0000FF";
                break;
            case 256:
                colorStyle += "#FF0FF";
                break;
            case 512:
                colorStyle += "#800080";
                break;
            case 2048:
                colorStyle += "#9932CC";
                break;
            default:
                colorStyle += "FFFFFF";
                break;
        }

        b1[i][j].setStyle(colorStyle);

    }









    /**
     * main method so program will run
     *
     * @param string argument of the class
     */
    public static void main(String[] args)  {
        Application.launch(args);
    }

}
  
  

  

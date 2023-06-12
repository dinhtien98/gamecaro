package com.example.gamecaro;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class pvc extends AppCompatActivity {

    private static final int BOARD_SIZE = 14;
    private static final int EMPTY = 0;
    private static final int PLAYER_X = 1;
    private static final int PLAYER_O = 2;

    private int currentPlayer = PLAYER_X;
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    private GridLayout gridLayout;
    private Button Back;
    private Button resetButton;

    private TextView textView;

    private AIPlayer aiPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvc);
        gridLayout = findViewById(R.id.gridLayout);
        Back = findViewById(R.id.BTback);
        resetButton = findViewById(R.id.resetButton);

        initializeBoard();
        initializeGrid();
        textView = findViewById(R.id.textView1);
        aiPlayer = new AIPlayer(PLAYER_O);
        textView.setText("Người chơi đi trước đánh X và máy đánh O");
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pvc.this, MainActivity.class);
                startActivity(intent);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    private void initializeGrid() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.banco);
                imageView.setOnClickListener(new CellClickListener(i, j));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = getResources().getDimensionPixelSize(R.dimen.image_width);
                params.height = getResources().getDimensionPixelSize(R.dimen.image_height);
                gridLayout.addView(imageView, params);
            }
        }
    }

    private class CellClickListener implements View.OnClickListener {
        private int row;
        private int col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            if (!gameOver && board[row][col] == EMPTY && currentPlayer == PLAYER_X) {
                board[row][col] = currentPlayer;
                updateCellImage(row, col);
                checkWin(row, col);
                switchPlayer();

                if (!gameOver && currentPlayer == PLAYER_O) {
                    makeAIMove();
                }
            }
        }
    }

    private void updateCellImage(int row, int col) {
        ImageView imageView = (ImageView) gridLayout.getChildAt(row * BOARD_SIZE + col);
        int imageResource = (currentPlayer == PLAYER_X) ? R.drawable.x1 : R.drawable.o1;
        imageView.setImageResource(imageResource);
    }

    private boolean gameOver = false;

    private void checkWin(int row, int col) {
        // Kiểm tra chiến thắng theo hàng ngang
        int horizontalCount = 1;
        int i = 1;
        while (col - i >= 0 && board[row][col - i] == currentPlayer) {
            horizontalCount++;
            i++;
        }
        i = 1;
        while (col + i < BOARD_SIZE && board[row][col + i] == currentPlayer) {
            horizontalCount++;
            i++;
        }

        // Kiểm tra chiến thắng theo hàng dọc
        int verticalCount = 1;
        i = 1;
        while (row - i >= 0 && board[row - i][col] == currentPlayer) {
            verticalCount++;
            i++;
        }
        i = 1;
        while (row + i < BOARD_SIZE && board[row + i][col] == currentPlayer) {
            verticalCount++;
            i++;
        }

        // Kiểm tra chiến thắng theo đường chéo chính
        int diagonalCount1 = 1;
        i = 1;
        while (row - i >= 0 && col - i >= 0 && board[row - i][col - i] == currentPlayer) {
            diagonalCount1++;
            i++;
        }
        i = 1;
        while (row + i < BOARD_SIZE && col + i < BOARD_SIZE && board[row + i][col + i] == currentPlayer) {
            diagonalCount1++;
            i++;
        }

        // Kiểm tra chiến thắng theo đường chéo phụ
        int diagonalCount2 = 1;
        i = 1;
        while (row - i >= 0 && col + i < BOARD_SIZE && board[row - i][col + i] == currentPlayer) {
            diagonalCount2++;
            i++;
        }
        i = 1;
        while (row + i < BOARD_SIZE && col - i >= 0 && board[row + i][col - i] == currentPlayer) {
            diagonalCount2++;
            i++;
        }

        // Kiểm tra chiến thắng
        if (horizontalCount >= 5 || verticalCount >= 5 || diagonalCount1 >= 5 || diagonalCount2 >= 5) {
            String winner = (currentPlayer == PLAYER_X) ? "Người chơi" : "Máy";
            textView.setText(winner + " chiến thắng");
            gameOver = true;
        } else if (isBoardFull()) {
            textView.setText("Hòa");
            gameOver = true;
        }
    }

    private boolean isBoardFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }

    private void makeAIMove() {
        // Find the position where player1 has made a move
        int player1Row = -1;
        int player1Col = -1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == PLAYER_X) {
                    player1Row = i;
                    player1Col = j;
                    break;
                }
            }
            if (player1Row != -1 && player1Col != -1) {
                break;
            }
        }

        // Simple AI logic: block player1's winning move if possible, else randomly select an empty cell
        int aiRow = -1;
        int aiCol = -1;

        // Check if player1 has three or more consecutive moves horizontally and block it
        if (player1Row >= 0 && player1Row < BOARD_SIZE && player1Col >= 0 && player1Col < BOARD_SIZE - 2) {
            int consecutiveCount = 1;
            for (int j = player1Col + 1; j < BOARD_SIZE; j++) {
                if (board[player1Row][j] == PLAYER_X) {
                    consecutiveCount++;
                } else {
                    break;
                }
            }

            if (consecutiveCount >= 3) {
                if (player1Col > 0 && board[player1Row][player1Col - 1] == EMPTY) {
                    aiRow = player1Row;
                    aiCol = player1Col - 1;
                } else if (player1Col + consecutiveCount < BOARD_SIZE && board[player1Row][player1Col + consecutiveCount] == EMPTY) {
                    aiRow = player1Row;
                    aiCol = player1Col + consecutiveCount;
                }
            }
        }

        // Check if player1 has three or more consecutive moves vertically and block it
        if (aiRow == -1 && aiCol == -1 && player1Row >= 0 && player1Row < BOARD_SIZE - 2 && player1Col >= 0 && player1Col < BOARD_SIZE) {
            int consecutiveCount = 1;
            for (int i = player1Row + 1; i < BOARD_SIZE; i++) {
                if (board[i][player1Col] == PLAYER_X) {
                    consecutiveCount++;
                } else {
                    break;
                }
            }

            if (consecutiveCount >= 3) {
                if (player1Row > 0 && board[player1Row - 1][player1Col] == EMPTY) {
                    aiRow = player1Row - 1;
                    aiCol = player1Col;
                } else if (player1Row + consecutiveCount < BOARD_SIZE && board[player1Row + consecutiveCount][player1Col] == EMPTY) {
                    aiRow = player1Row + consecutiveCount;
                    aiCol = player1Col;
                }
            }
        }

        // Check if player1 has four consecutive moves horizontally and block both ends
        if (aiRow == -1 && aiCol == -1 && player1Row >= 0 && player1Row < BOARD_SIZE && player1Col >= 0 && player1Col < BOARD_SIZE - 3) {
            int consecutiveCount = 1;
            for (int j = player1Col + 1; j < BOARD_SIZE; j++) {
                if (board[player1Row][j] == PLAYER_X) {
                    consecutiveCount++;
                } else {
                    break;
                }
            }

            if (consecutiveCount == 4) {
                if (player1Col > 0 && board[player1Row][player1Col - 1] == EMPTY) {
                    aiRow = player1Row;
                    aiCol = player1Col - 1;
                } else if (player1Col + consecutiveCount < BOARD_SIZE && board[player1Row][player1Col + consecutiveCount] == EMPTY) {
                    aiRow = player1Row;
                    aiCol = player1Col + consecutiveCount;
                }
            }
        }

        // If no winning move to block, randomly select an empty cell around player1's move
        if (aiRow == -1 && aiCol == -1) {
            Random random = new Random();
            int[] directions = {-1, 0, 1};
            ArrayList<Integer> availableRows = new ArrayList<>();
            ArrayList<Integer> availableCols = new ArrayList<>();

            for (int i : directions) {
                for (int j : directions) {
                    int newRow = player1Row + i;
                    int newCol = player1Col + j;
                    if (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE && board[newRow][newCol] == EMPTY) {
                        availableRows.add(newRow);
                        availableCols.add(newCol);
                    }
                }
            }

            if (!availableRows.isEmpty() && !availableCols.isEmpty()) {
                int randomIndex = random.nextInt(availableRows.size());
                aiRow = availableRows.get(randomIndex);
                aiCol = availableCols.get(randomIndex);
            } else {
                // If no available cell around player1's move, select a random empty cell on the board
                do {
                    aiRow = random.nextInt(BOARD_SIZE);
                    aiCol = random.nextInt(BOARD_SIZE);
                } while (board[aiRow][aiCol] != EMPTY);
            }
        }

        board[aiRow][aiCol] = currentPlayer;
        updateCellImage(aiRow, aiCol);
        checkWin(aiRow, aiCol);
        switchPlayer();
    }


    private void resetGame() {
        initializeBoard();
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageView.setImageResource(R.drawable.banco);
        }
        gameOver = false;
        textView.setText("Người chơi đi trước đánh X và máy đánh O");
        currentPlayer = PLAYER_X;
    }

    class AIPlayer {
        private int player;

        public AIPlayer(int player) {
            this.player = player;
        }

        public int getPlayer() {
            return player;
        }
    }
}


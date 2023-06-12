package com.example.gamecaro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class pvp extends AppCompatActivity {

    private static final int BOARD_SIZE = 14;
    private static final int EMPTY = 0;
    private static final int PLAYER_X = 1;
    private static final int PLAYER_O = 2;

    private int currentPlayer = PLAYER_X;
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    private GridLayout gridLayout;
    private Button resetButton;
    private Button Back;
    private int winner;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvp);
        gridLayout = findViewById(R.id.gridLayout);
        resetButton = findViewById(R.id.resetButton);
        Back = findViewById(R.id.BTback);

        initializeBoard();
        initializeGrid();
        textView = findViewById(R.id.textView1);
        textView.setText("Người chơi X đi trước");

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pvp.this, MainActivity.class);
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
            if (!gameOver && board[row][col] == EMPTY) {
                board[row][col] = currentPlayer;
                updateCellImage(row, col);
                checkWin(row, col);
                switchPlayer();
            }
        }
    }

    private void updateCellImage(int row, int col) {
        ImageView imageView = (ImageView) gridLayout.getChildAt(row * BOARD_SIZE + col);
        if(currentPlayer == PLAYER_X){
            textView.setText("Lượt đánh của người chơi O");
        }else {
            textView.setText("Lượt đánh của người chơi X");
        }
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
            String winner1 = (currentPlayer == PLAYER_X) ? "Người chơi X" : "Người chơi O";
            winner = currentPlayer;
            textView.setText(winner1 + " chiến thắng");
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

    private void resetGame() {
        initializeBoard();
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageView.setImageResource(R.drawable.banco);
        }
        gameOver = false;

        // Thay đổi lượt đánh
        if (winner == PLAYER_X) {
            currentPlayer = PLAYER_O;
            textView.setText("Người chơi O đi trước");
        } else {
            currentPlayer = PLAYER_X;
            textView.setText("Người chơi X đi trước");
        }
    }
}

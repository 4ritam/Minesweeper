package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

    private final char[][] mainField;
    private final char[][] displayField;
    private final int row;
    private final int column;
    public static char UNEXPLORED_CELL = '.';
    public static char EXPLORED_FREE_CELL = '/';
    public static char MINE_CELL = 'X';
    public static char UNEXPLORED_MARKED_CELL = '*';

    public Minesweeper(int row, int column) {
        this.row = row;
        this.column = column;
        this.mainField = new char[this.row][this.column];
        this.displayField = new char[this.row][this.column];

        System.out.print("How many mines do you want on the field? ");
        Scanner scanner = new Scanner(System.in);

        cellInitializer(scanner.nextInt());

    }

    private void cellInitializer(int numberOfMines) {

        while (numberOfMines > 0) {
            for (int i = 0; i < this.row; i++) {
                Random random = new Random();
                int mineIndex = random.nextInt(this.row + 1);
                for (int j = 0; j < this.column; j++) {
                    if (this.mainField[i][j] != MINE_CELL)
                        this.mainField[i][j] = UNEXPLORED_CELL;
                }
                if (numberOfMines == 0) {
                    continue;
                }
                if (mineIndex != this.row) {
                    if (this.mainField[i][mineIndex] == UNEXPLORED_CELL) {
                        this.mainField[i][mineIndex] = MINE_CELL;
                        numberOfMines--;
                    }
                }
            }
        }

        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.column; j++) {
                if (this.mainField[i][j] != MINE_CELL) {
                    this.mainField[i][j] = checkAround(this.mainField, i, j);
                }
            }
        }

        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.column; j++) {
                this.displayField[i][j] = UNEXPLORED_CELL;
            }
        }
    }

    private char checkAround(char[][] field, int x, int y) {
        int numberOfMines = 48;

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == x && j == y) continue;
                try {
                    if (field[i][j] == MINE_CELL) {
                        numberOfMines++;
                    }
                } catch (Exception ignored) { }

            }
        }

        return numberOfMines > 48 ? ((char) numberOfMines) : UNEXPLORED_CELL;

    }

    public int getColumn() {
        return this.column;
    }

    public int getRow() {
        return this.column;
    }

    public char[][] getMainField() {
        return this.mainField;
    }

    public char[][] getDisplayField() {
        return this.displayField;
    }

}

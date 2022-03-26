package minesweeper;

public class Main {

    public static void main(String[] args) {

        Minesweeper field = new Minesweeper(9,9);

        Game g = new Game(field);

    }

}
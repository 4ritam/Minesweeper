package minesweeper;

import java.util.*;

public class Game {

    private final Minesweeper field;
    private WinStatus currentStatus = WinStatus.STILL_PLAYING;

    private final List<Integer[]> mineLocation = new ArrayList<>();
    private final LinkedList<Integer[]> markLocation = new LinkedList<>();

    public Game(Minesweeper field) {

        this.field = field;

        Scanner s = new Scanner(System.in);

        displayField();

        mineLocationAdd();

        while(true) {
            System.out.print("Set/unset mines marks or claim a cell as free: ");

            int x = s.nextInt() - 1;
            int y = s.nextInt() - 1;
            manipulateField( y, x, s.next());

            displayField();

            gameStatusCheck();

            if (this.currentStatus != WinStatus.STILL_PLAYING) {
                switch (this.currentStatus) {
                    case WON:
                        System.out.println("Congratulations! You found all the mines!");
                        break;
                    case LOST:
                        System.out.println("You stepped on a mine and failed!");
                        break;
                }
                break;
            }
        }

    }

    private void manipulateField(int x, int y, String type) {

        try {
            switch (ManipulationType.findByType(type)) {
                case FREE:
                    freeCell(x, y);
                    break;
                case MINE:
                    setOrUnsetMine(x, y);
                    break;
            }
        } catch (NullPointerException ignored) { }

    }

    private void freeCell(int x, int y) {
        if (field.getMainField()[x][y] == Minesweeper.MINE_CELL) {
            this.currentStatus = WinStatus.LOST;
            return;
        }

        checkAround(x, y);


    }

    private void checkAround(int x, int y) {

        if (field.getMainField()[x][y] == Minesweeper.MINE_CELL) {
            return;
        }

        if (field.getMainField()[x][y] > 48 && field.getMainField()[x][y] < 58) {
            field.getDisplayField()[x][y] = field.getMainField()[x][y];
        } else field.getDisplayField()[x][y] = Minesweeper.EXPLORED_FREE_CELL;

        for (int i = x - 1; i <= x + 1; i++) {
            if (i == x) {
                continue;
            }
            try {
                if ((field.getMainField()[x][y] > 48 && field.getMainField()[x][y] < 58) &&
                        (field.getMainField()[i][y] > 48 && field.getMainField()[i][y] < 58)){
                    field.getDisplayField()[i][y] = field.getMainField()[i][y];
                    continue;
                }

                if (field.getDisplayField()[i][y] != Minesweeper.EXPLORED_FREE_CELL) {
                    checkAround(i,y);
                }
            } catch (Exception ignored) { }
        }

        for (int j = y - 1; j <= y + 1; j++) {
            if (j == y) {
                continue;
            }
            try {

                if ((field.getMainField()[x][y] > 48 && field.getMainField()[x][y] < 58) &&
                        (field.getMainField()[x][j] > 48 && field.getMainField()[x][j] < 58)) {
                    field.getDisplayField()[x][j] = field.getMainField()[x][j];
                    continue;
                }

                if (field.getDisplayField()[x][j] != Minesweeper.EXPLORED_FREE_CELL) {
                    checkAround(x,j);
                }
            } catch (Exception ignored) { }
        }

    }

    private void setOrUnsetMine(int x, int y) {

        if (field.getDisplayField()[x][y] == '.') {
            field.getDisplayField()[x][y] = '*';
            this.markLocation.add(new Integer[]{x, y});
        } else if (field.getDisplayField()[x][y] == '*') {

            field.getDisplayField()[x][y] = '.';

            deleteNode(this.markLocation, x, y);

        } else {
            System.out.println("It is already explored!");
        }

    }

    public void deleteNode(LinkedList<Integer[]> markLocation, int x, int y) {
        for (int i = 0; i < markLocation.size(); i++) {
            if (Arrays.equals(markLocation.get(i), new Integer[]{x, y})) {
                markLocation.remove(i);
                return;
            }
        }
    }



    private void gameStatusCheck() {
        mineMarkCheck();
        freeMarkCheck();
    }

    private void mineMarkCheck() {

        if (markLocation.size() < mineLocation.size()) return;

        for (Integer[] marks : markLocation) {
            for (Integer[] mines : mineLocation) {
                if (checkSame(marks, mines)) {
                    break;
                }
                if (Arrays.equals(mines, mineLocation.get(mineLocation.size() - 1))) {
                    return;
                }
            }
        }
        this.currentStatus = WinStatus.WON;
    }

    public void freeMarkCheck() {

    }

    public boolean checkSame(Integer[] marks, Integer[] mines) {

        for (int i = 0; i < marks.length; i++) {
            if (!Objects.equals(marks[i], mines[i])) break;

            if (i == marks.length - 1) return true;
        }

        return false;

    }


    private void displayField() {

        System.out.println();
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        byte i = 1;

        for (char[] rows : field.getDisplayField()) {

            System.out.print(i++ + "|");

            for (char cell : rows) {

                System.out.print(cell);

            }

            System.out.println("|");

        }

        System.out.println("-|---------|");
    }

    private void mineLocationAdd() {
        char[][] field = this.field.getMainField();

        int indexi = 0;
        for (char[] row : field) {
            int indexj = 0;
            for (char element : row) {
                if (element == Minesweeper.MINE_CELL) {
                    mineLocation.add(new Integer[]{indexi, indexj});
                }
                indexj++;
            }
            indexi++;
        }
    }

}

enum WinStatus {
    WON, LOST, STILL_PLAYING
}

enum ManipulationType {

    FREE("free"),
    MINE("mine");

    final String type;

    ManipulationType(String type) {
        this.type = type;
    }

    public static ManipulationType findByType(String type) {
        for (ManipulationType value: values())
            if (value.type.equals(type)) return value;
        return null;
    }

}
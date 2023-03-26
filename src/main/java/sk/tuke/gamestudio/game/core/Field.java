package sk.tuke.gamestudio.game.core;

public class Field {

    public static int boardSize;
    public static int[][] board;
    private final GameState state;
    private final long startMillis;

    public Field(int size) {
        boardSize = size;
        state = GameState.PLAYING;
        board = new int[boardSize][boardSize];
        startMillis = System.currentTimeMillis();

    }

    //function to see if block can be placed
    public static boolean canPlaceBlock(int choice, int row, int col) {
        int[][] block = Block.blocksL1[choice];
        if (row < 0 || col < 0 || row + block.length > boardSize || col + block[0].length > boardSize) {
            return false;
        }
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] == 1 && board[row + i][col + j] != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    //function to place the block
    public static void placeBlock(int choice, int row, int col) {
        int[][] block = Block.blocksL1[choice];
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] == 1) {
                    board[row + i][col + j] = choice + 1;
                }
            }
        }
    }

    //function to remove the block
    public static void removeBlock(int choice, int row, int col) {
        int[][] block = Block.blocksL1[choice];
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] == 1) {
                    board[row + i][col + j] = -1;
                }
            }
        }
    }

    //looking at game state to know if game is over
    public static GameState isGameOver() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == -1) {
                    return GameState.PLAYING;
                }
            }
        }
        return GameState.SOLVED;
    }

    //setting board tiles to -1(empty)
    public void initBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = -1;
            }
        }
    }

    public int getScore() {
        return boardSize * boardSize * Block.blockCount - getPlayingTime();
    }

    private int getPlayingTime() {
        return ((int) (System.currentTimeMillis() - startMillis)) / 1000;
    }
}
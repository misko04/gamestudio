package sk.tuke.gamestudio.game.core;


public class Block {
    public static final int[][][] blocksL1 = {
            // Block 1
            {
                    {1, 1},
                    {1, 1}
            },
            // Block 2
            {
                    {1, 1, 1},
                    {0, 1, 0}
            },
            // Block 3
            {
                    {0, 1},
                    {1, 1}
            },
            // Block 4
            {
                    {1, 1, 1},
                    {1, 1, 1},
                    {1, 1, 1}
            },
            // Block 5
            {
                    {1, 0},
                    {1, 1}
            },
            // Block 6
            {
                    {1, 1}
            }
    };
    public static final int[][][] blocksL2 = {
            // Block 1
            {
                    {1, 1},
                    {1, 1}
            },
            // Block 2
            {
                    {1, 1, 1},
                    {0, 1, 0}
            },
            // Block 3
            {
                    {0, 1},
                    {1, 1}
            },
            // Block 4
            {
                    {1, 1, 1},
                    {1, 1, 1},
                    {1, 1, 1}
            },
            // Block 5
            {
                    {1, 0},
                    {1, 1}
            },
            // Block 6
            {
                    {1, 1}
            }
    };
    public static int blockCount = 6;
    public static boolean[] used = new boolean[blockCount];

    public Block(int count) {
        initBlocks();
        blockCount = count;
    }

    //function to set all blocks as not used at start
    public void initBlocks() {
        for (int i = 0; i < blockCount; i++) {
            used[i] = false;
        }
    }


}

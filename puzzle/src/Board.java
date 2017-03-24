import java.util.ArrayList;
import java.util.List;

/**
 * Project: Sonar-Gerrit Plugin
 * Author:  Tatiana Didik
 * Created: 08.03.2016 20:51
 * <p/>
 * $Id$
 */
public class Board {
    private int[][] blocks;
    private Iterable<Board> neighbors;
//    public static int calls = 0;

    public Board(int[][] blocks)           // construct a board from an N-by-N array of blocks
    {
        this.blocks = copyBoard(blocks, blocks.length);
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // board dimension N
    {
        return blocks.length;
    }

    public int hamming()                   // number of blocks out of place
    {
        int hamming = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] != 0) {
                    int expectedI = (blocks[i][j] - 1) / dimension();
                    int expectedJ = (blocks[i][j] - 1) % dimension();
                    if (i != expectedI || j != expectedJ) {
                        hamming++;
                    }
                }
            }
        }
        return hamming;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int manhattan = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] != 0) {
                    int expectedI = (blocks[i][j] - 1) / dimension();
                    int expectedJ = (blocks[i][j] - 1) % dimension();
                    manhattan += Math.abs(i - expectedI) + Math.abs(j - expectedJ);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
//        calls ++;
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
//        calls++;
        int i1 = 0;
        int j1 = 0;
        int i2 = 0;
        int j2 = 0;
        boolean firstFound = false;
        boolean secondFound = false;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] != 0) {
                    int expectedI = (blocks[i][j] - 1) / dimension();
                    int expectedJ = (blocks[i][j] - 1) % dimension();
                    if (i != expectedI || j != expectedJ) {
                        if (firstFound) {
                            i2 = i;
                            j2 = j;
                            secondFound = true;
                            break;
                        } else {
                            i1 = i;
                            j1 = j;
                            firstFound = true;
                        }
                    }
                }
            }
            if (secondFound) {
                break;
            }
        }
        if (!secondFound) {
            boolean is0 = blocks[0][0] == 0;
            i1 = 0;
            j1 = is0 ? 1 : 0;
            is0 |= blocks[0][1] == 0;
            i2 = is0 ? 2 / dimension() : 0;
            j2 = is0 ? 2 % dimension() : 1;
        }
        return swap(i1, j1, i2, j2);
    }

    private Board swap(int i1, int j1, int i2, int j2) {
        int[][] twin = copyBoard(blocks, dimension());
        twin[i1][j1] = blocks[i2][j2];
        twin[i2][j2] = blocks[i1][j1];
        return new Board(twin);
    }

    private static int[][] copyBoard(int[][] blocks, int dim) {
        int[][] twin = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            twin[i] = new int[dim];
            System.arraycopy(blocks[i], 0, twin[i], 0, dim);
        }
        return twin;
    }


    // does this board equal y?
    public boolean equals(Object y) {
//        calls++;
        if (this == y) {
            return true;
        }
        if (y instanceof Board) {
            Board that = (Board) y;
            if (this.dimension() == that.dimension()) {
                for (int i = 0; i < dimension(); i++) {
                    for (int j = 0; j < dimension(); j++) {
                        if (this.blocks[i][j] != that.blocks[i][j]) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
//        calls++;
        if (neighbors == null) {
            List<Board> boards = new ArrayList<>();
            // let's find 0. todo make it BST???
            int i0 = 0;
            int j0 = 0;
            for (int i = 0; i < dimension(); i++) {
                boolean found = false;
                for (int j = 0; j < dimension(); j++) {
                    if (blocks[i][j] == 0) {
                        i0 = i;
                        j0 = j;
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
            if (i0 > 0) {
                boards.add(swap(i0, j0, i0 - 1, j0));
            }
            if (i0 < dimension() - 1) {
                boards.add(swap(i0, j0, i0 + 1, j0));
            }
            if (j0 > 0) {
                boards.add(swap(i0, j0, i0, j0 - 1));
            }
            if (j0 < dimension() - 1) {
                boards.add(swap(i0, j0, i0, j0 + 1));
            }
            neighbors = boards;
        }
        return neighbors;
    }


    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension()).append("\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                sb.append(String.format("%2d", blocks[i][j])).append(" ");
            }
            sb.replace(sb.length() - 1, sb.length(), "\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int[][] board = new int[][]{{0, 1}, {3, 2}};
//        int[][] board = new int[][]{{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
//        int[][] board = new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board b = new Board(board);
        System.out.println(b);
        System.out.println("hamming = " + b.hamming());
        System.out.println("manhattan = " + b.manhattan());
        System.out.println("twin:");
        System.out.println(b.twin());
        System.out.println("neighbors:");
        {
            for (Board brd : b.neighbors()) {
                System.out.println();
                System.out.println(brd.toString());
            }
        }
    }


}

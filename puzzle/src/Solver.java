import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Project: Sonar-Gerrit Plugin
 * Author:  Tatiana Didik
 * Created: 08.03.2016 20:51
 * <p/>
 * $Id$
 */
public class Solver {
    private TreeNode solution;

    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        MinPQ<TreeNode> solution = new MinPQ<TreeNode>(1, getBoardTreeComparator());
        solution.insert(new TreeNode(initial, null, 0));
        MinPQ<TreeNode> twinSolution = new MinPQ<TreeNode>(1, getBoardTreeComparator());
        twinSolution.insert(new TreeNode(initial.twin(), null, 0));

        TreeNode current = solution.delMin();
        TreeNode twinCurrent = twinSolution.delMin();
        while (!current.board.isGoal() && !twinCurrent.board.isGoal()) {
            for (Board n : current.board.neighbors()) {
                if (unique(n, current.parent)) {
                    solution.insert(new TreeNode(n, current, current.moves + 1));
                }
            }
            current = solution.delMin();

            for (Board n : twinCurrent.board.neighbors()) {
                if (unique(n, twinCurrent.parent)) {
                    twinSolution.insert(new TreeNode(n, twinCurrent, twinCurrent.moves + 1));
                }
            }
            twinCurrent = twinSolution.delMin();
        }
        this.solution = current.board.isGoal() ? current : null;
    }

    private boolean unique(Board node, TreeNode parent) {
        while (parent != null) {
            if (node.equals(parent.board)) {
                return false;
            }
            parent = parent.parent;
        }
        return true;
    }

    private static Comparator<Board> getBoardComparator() {
        return new Comparator<Board>() {
            @Override
            public int compare(Board o1, Board o2) {
                return o1.manhattan() - o2.manhattan();
            }
        };
    }

    private static Comparator<TreeNode> getBoardTreeComparator() {
        return new Comparator<TreeNode>() {
            @Override
            public int compare(TreeNode o1, TreeNode o2) {
                return o1.board.manhattan() + o1.moves - o2.board.manhattan() - o2.moves;
            }
        };
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
//        return solution.size() - 1;
        return this.solution == null ? -1 : this.solution.moves;
    }

    private static List<Board> asList(MinPQ<Board> boards) {
        List<Board> copy = new ArrayList<>(boards.size());
        for (Board board : boards) {
            copy.add(board);
        }
        return copy;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        List<Board> list = null;
        if (solution != null) {
            list = new LinkedList<Board>();
            TreeNode current = solution;
            do {
                list.add(current.board);
                current = current.parent;
            } while (current != null);
            Collections.reverse(list);
        }  // else this.solution = null;
        return list;
    }

    public static void main(String[] args) // solve a slider puzzle (given below)
    {
        int[][] blocks = new int[][]{{1, 3, 5}, {7, 2, 6}, {8, 0, 4}};
//        int[][] blocks = new int[][]{{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
//        int[][] blocks = new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
//        int[][] blocks = new int[][]{{1, 3}, {2, 0}};
//        int[][] blocks = new int[][]{{1, 2}, {0, 3}};
//        int[][] blocks = new int[][]{{3, 1}, {2, 0}};


//         create initial board from file
//        In in = new In("./8puzzle/puzzle2x2-unsolvable1.txt");
//        int N = in.readInt();
//        int[][] blocks = new int[N][N];
//        for (int i = 0; i < N; i++)
//            for (int j = 0; j < N; j++)
//                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
//        System.out.println(Board.calls);
    }

    private static class TreeNode {

        Board board;
        TreeNode parent;
        int moves;

        TreeNode(Board board, TreeNode parent, int moves) {
            this.board = board;
            this.parent = parent;
            this.moves = moves;
        }

    }
}
